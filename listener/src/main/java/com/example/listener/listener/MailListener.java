package com.example.listener.listener;

import com.example.listener.model.AttachmentInfo;
import com.example.listener.service.MailService;
import jakarta.mail.*;
import jakarta.mail.search.FlagTerm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.io.IOException;

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
    private final MailService mailService;

    Flags seen = new Flags(Flags.Flag.SEEN);

    public MailListener(JavaMailSender javaMailSender, MailService mailService){
        this.javaMailSender = javaMailSender;
        this.mailService = mailService;
    }

    public boolean newMailControl() throws MessagingException {
        JavaMailSenderImpl senderImpl = (JavaMailSenderImpl) javaMailSender;
        Session session = senderImpl.getSession();

        Store store = session.getStore("imap");
        store.connect(host, user, password);

        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_WRITE);

        FlagTerm unseenFlagTerm = new FlagTerm(seen, false);

        unreadMessages = inbox.search(unseenFlagTerm);

        currentValue = unreadMessages.length;

        if((previousValue != currentValue) && (currentValue != 0)){
            previousValue = currentValue;
            return true;
        }else {
            previousValue = currentValue;
            return false;
        }
    }

    public void extractMail() throws MessagingException, IOException {
        for (Message message : unreadMessages) {
            if (message.isMimeType("multipart/*")){
                Multipart multipart = (Multipart) message.getContent();
                for (int i = 0; i < multipart.getCount(); i++) {
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    if (bodyPart.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())){
                        log.info("find attachment in mail");
                        //TODO dosya var ise ilk önce attachmentInfo nesnesine çeviricez sonra bu nesneleri MailDto içindeki list'e aktarıcaz.
                        AttachmentInfo attachmentInfo = new AttachmentInfo();
                        attachmentInfo.setFileName(bodyPart.getFileName());
                        attachmentInfo.setFileData(bodyPart.getInputStream().readAllBytes());
                        mailService.sendMailAttachment(attachmentInfo);
                        log.info("Send message");
                    }
                }
            } else {
                log.info("Does not have incoming mail attachment content");
            }
        }
    }

}
