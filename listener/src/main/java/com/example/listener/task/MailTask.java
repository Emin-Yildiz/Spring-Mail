package com.example.listener.task;

import com.example.listener.listener.MailListener;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class MailTask {

    private final MailListener mailListener;

    public MailTask(MailListener mailListener) {
        this.mailListener = mailListener;
    }

    @Scheduled(fixedRate = 30000)
    public void mailListenerTask() throws MessagingException, IOException {
        log.info("task is running");
        if (Boolean.TRUE.equals(mailListener.newMailControl())) {
            log.info("new mail is coming");
            mailListener.extractMail();
        }
    }
}
