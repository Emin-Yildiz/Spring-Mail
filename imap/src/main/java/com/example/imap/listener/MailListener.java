package com.example.imap.listener;

import jakarta.mail.*;
import jakarta.mail.search.FlagTerm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@Slf4j
public class MailListener{

    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.username}")
    private String user;
    @Value("${spring.mail.password}")
    private String password;
    private final JavaMailSender javaMailSender;
    private int currentValue = 0, previousValue = 0;
    private Message[] unreadMessages;

    public MailListener(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }

    public boolean newMailControl() throws MessagingException {
        JavaMailSenderImpl senderImpl = (JavaMailSenderImpl) javaMailSender;
        Session session = senderImpl.getSession();

        Store store = session.getStore("imap");
        store.connect(host, user, password);

        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_WRITE); // hem okuyup hemde üzerinde düzenleme yapabilmek için inbox bu modda açıldı. READ_ONLY modunda açıdığı zaman okundu bilgisi düzenlenmiyor.

        // Sadece okunmamış mailleri almak için.
        Flags seen = new Flags(Flags.Flag.SEEN);
        FlagTerm unseenFlagTerm = new FlagTerm(seen, false);

        //Message[] messages = inbox.getMessages(); // bütün mailleri getirmek için
        unreadMessages = inbox.search(unseenFlagTerm); // okumamış mailleri getirmek için

        currentValue = unreadMessages.length;

        if((previousValue != currentValue) && (currentValue != 0)){
            previousValue = currentValue;
            return true;
        }else {
            return false;
        }
    }

    public void extractMail() throws MessagingException, IOException {
        for (Message message : unreadMessages) {
            //System.out.println(message.);
            if (message.isMimeType("multipart/*")){ // mesaj içeriğinde birden fazla kısım var mı kontrolü burada yapılır yok ise zaten text döner.
                Multipart multipart = (Multipart) message.getContent();
                // bu for döngüsü bir mail içindeki birden çok içerik içinde dönüyor buna mail içindeki text'de dahil
                for (int i = 0; i < multipart.getCount(); i++) {
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    if (bodyPart.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())){ // ATTACHMENT ile işaretli bir ek dosya var mı yok mu kontrolü yapılır.
                        log.info("New mail receiver with attachment: " + message.getSubject() + " " + message.getReceivedDate());
                        saveAttachment(bodyPart.getInputStream(),bodyPart.getFileName());
                    }else{
                        log.info("New mail receiver: " + message.getSubject() + " " + message.getReceivedDate());
                        message.setFlag(Flags.Flag.SEEN, true);
                    }
                }
            } else {
                System.out.println("New mail receiver: " + message.getSubject() + " " + message.getReceivedDate());
                message.setFlag(Flags.Flag.SEEN, true);
            }

        }
    }

    private void saveAttachment(InputStream inputStream,String fileName) throws IOException {

        Path savePath = Paths.get("./attachment/", fileName);
        Files.createDirectories(savePath.getParent());
        Files.copy(inputStream, savePath);
//        File savePath = new File("./attachment/");
//        if (!savePath.exists()) {
//            savePath.mkdirs();
//        }
//
//        File file = new File(savePath,fileName);
//
//        try {
//            FileOutputStream fileOutputStream = new FileOutputStream(file);
//            byte[] buffer = new byte[4096];
//            int bytesRead;
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                fileOutputStream.write(buffer, 0, bytesRead);
//
//            }
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
    }

}