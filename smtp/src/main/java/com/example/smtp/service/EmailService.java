package com.example.smtp.service;

import com.example.smtp.domain.EmailDetails;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface EmailService {

    String sendEmail(EmailDetails emailDetails);

    String sendEmailWithAttachment(EmailDetails emailDetails);

    String sendMailWithFile(MultipartFile file, EmailDetails emailDetails) throws IOException;

    String sendMailWithFiles(List<MultipartFile> files, EmailDetails emailDetails);
}
