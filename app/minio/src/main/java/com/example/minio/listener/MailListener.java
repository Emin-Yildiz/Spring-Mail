package com.example.minio.listener;

import com.example.minio.config.ArtemisConfig;
import com.example.minio.model.AttachmentInfo;
import com.example.minio.service.MailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MailListener {

    private final MailService mailService;
    private final ObjectMapper objectMapper;
    private static AttachmentInfo attachmentInfo = new AttachmentInfo();

    public MailListener(MailService mailService, ObjectMapper objectMapper) {
        this.mailService = mailService;
        this.objectMapper = objectMapper;
    }

    @JmsListener(destination = ArtemisConfig.MAIL_QUEUE)
    public void getMailData(String message){
        // burda mapper işlemi yapılmıyor.
        AttachmentInfo attachmentInfo = objectMapper.convertValue(message, AttachmentInfo.class);
        mailService.extractMailAttachment(attachmentInfo);
    }

    @JmsListener(destination = ArtemisConfig.MAIL_NAME_QUEUE)
    public void getMailNameData(String fileName){
        // burda mapper işlemi yapılmıyor.
        attachmentInfo.setFileName(fileName);
        sendMinio();
    }

    @JmsListener(destination = ArtemisConfig.MAIL_DATA_QUEUE)
    public void getMailFileData(byte[] byteData){
       attachmentInfo.setFileData(byteData);
       sendMinio();
    }

    private void sendMinio() {
        if (attachmentInfo.getFileData() != null && attachmentInfo.getFileName() != null) {
            mailService.extractMailAttachment(attachmentInfo);
            attachmentInfo.setFileName(null);
            attachmentInfo.setFileData(null);
        }
    }

}
