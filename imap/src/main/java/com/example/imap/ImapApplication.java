package com.example.imap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ImapApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImapApplication.class, args);
	}

}
