package com.example.smtp.service;

import com.example.smtp.domain.EmailDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public String sendEmail(EmailDetails emailDetails) {

        try{
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

            simpleMailMessage.setFrom(sender);
            simpleMailMessage.setTo(emailDetails.getRecipient());
            simpleMailMessage.setText(emailDetails.getMsgBody());
            simpleMailMessage.setSubject(emailDetails.getSubject());

            javaMailSender.send(simpleMailMessage);
            return "Mail send succes";
        }catch(Exception e){
            return "Mail is not sending";
        }

    }

    @Override
    public String sendEmailWithAttachment(EmailDetails emailDetails) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try{

            mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(emailDetails.getRecipient());
            mimeMessageHelper.setText(emailDetails.getMsgBody());
            mimeMessageHelper.setSubject(emailDetails.getSubject());

            FileSystemResource fileSystemResource = new FileSystemResource(new File(emailDetails.getAttachment()));

            mimeMessageHelper.addAttachment(fileSystemResource.getFilename(),fileSystemResource);

            javaMailSender.send(mimeMessage);
            return "Mail send succes";
        }catch(Exception e){
            return "Mail is not sending";
        }
    }

    @Override
    public String sendMailWithFile(MultipartFile file, EmailDetails emailDetails) throws IOException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        byte[] fileBytes = file.getBytes();

        try{
            mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(emailDetails.getRecipient());
            mimeMessageHelper.setText(emailDetails.getMsgBody());
            mimeMessageHelper.setSubject(emailDetails.getSubject());

            mimeMessageHelper.addAttachment(file.getOriginalFilename(), new ByteArrayDataSource(fileBytes, "application/octet-stream"));

            javaMailSender.send(mimeMessage);
            return "Mail send succes";
        }catch(Exception e){
            return "Mail is not sending";
        }
    }

    @Override
    public String sendMailWithFiles(List<MultipartFile> files, EmailDetails emailDetails) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try{
            mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(emailDetails.getRecipient());
            mimeMessageHelper.setText(emailDetails.getMsgBody());
            mimeMessageHelper.setSubject(emailDetails.getSubject());

            files.forEach(
                    file -> {
                        byte[] fileBytes;

                        try {
                            fileBytes = file.getBytes();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        try {
                            mimeMessageHelper.addAttachment(file.getOriginalFilename(), new ByteArrayDataSource(fileBytes, "application/octet-stream"));
                        } catch (MessagingException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );

            javaMailSender.send(mimeMessage);
            return "Mail send succes";
        }catch(Exception e){
            return "Mail is not sending";
        }
    }
}
