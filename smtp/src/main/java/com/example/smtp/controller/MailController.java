package com.example.smtp.controller;

import com.example.smtp.domain.EmailDetails;
import com.example.smtp.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/mail")
public class MailController {

    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    public MailController(EmailService emailService, ObjectMapper objectMapper) {
        this.emailService = emailService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/sendMail")
    public String sendMail(@RequestBody EmailDetails emailDetails){
        return emailService.sendEmail(emailDetails);
    }

    @PostMapping("/sendMailWithAttachment")
    public String sendMailWithAttechmnet(@RequestBody EmailDetails emailDetails){
        return emailService.sendEmailWithAttachment(emailDetails);
    }

    @PostMapping("/sendMailWithFile")
    public String sendMailWithFile(@RequestParam("file") MultipartFile file, @RequestParam("emailDetails") String emailDetailsJson) throws IOException {

        EmailDetails emailDetails = objectMapper.readValue(emailDetailsJson,EmailDetails.class);

        return emailService.sendMailWithFile(file,emailDetails);

    }

    @PostMapping("/sendMailWithFiles")
    public String sendMailWithFiles(@RequestParam("files") List<MultipartFile> files, @RequestParam("emailDetails") String emailDetailsJson) throws IOException {

        EmailDetails emailDetails = objectMapper.readValue(emailDetailsJson,EmailDetails.class);

        return emailService.sendMailWithFiles(files,emailDetails);

    }
}
