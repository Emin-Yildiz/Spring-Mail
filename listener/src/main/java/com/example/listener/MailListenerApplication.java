package com.example.listener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MailListenerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MailListenerApplication.class, args);
	}

}
