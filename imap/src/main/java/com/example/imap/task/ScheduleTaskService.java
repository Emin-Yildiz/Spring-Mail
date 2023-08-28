package com.example.imap.task;

import com.example.imap.listener.MailListener;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class ScheduleTaskService {
    private final MailListener mailListener;

    public ScheduleTaskService(MailListener mailListener) {
        this.mailListener = mailListener;
    }

    @Scheduled(fixedRate = 50000)
    public void mailListenerTask() throws MessagingException, IOException {
        log.info("task is running");
        if (Boolean.TRUE.equals(mailListener.newMailControl())){
            mailListener.extractMail();
        }
    }
}
