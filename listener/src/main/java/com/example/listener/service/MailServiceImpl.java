package com.example.listener.service;

import com.example.listener.config.ArtemisConfig;
import com.example.listener.model.AttachmentInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MailServiceImpl implements MailService{

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    public MailServiceImpl(JmsTemplate jmsTemplate, ObjectMapper objectMapper) {
        this.jmsTemplate = jmsTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void sendMailAttachment(AttachmentInfo attachmentInfo) {
        try{
            //String message = objectMapper.writeValueAsString(attachmentInfo);
            jmsTemplate.convertAndSend(ArtemisConfig.MAIL_NAME_QUEUE,attachmentInfo.getFileName());
            jmsTemplate.convertAndSend(ArtemisConfig.MAIL_DATA_QUEUE,attachmentInfo.getFileData());
            log.info("Message is sending");
        } catch (Exception e){
            log.error("Message is not sending");
        }

    }
}
