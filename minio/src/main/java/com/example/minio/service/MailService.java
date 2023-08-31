package com.example.minio.service;

import com.example.minio.model.AttachmentInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;


@Service
@Slf4j
public class MailService {

    private final MinioService minioService;

    public MailService(MinioService minioService) {
        this.minioService = minioService;
    }

    public void extractMailAttachment(AttachmentInfo attachmentInfo){
        List<String> imageFormats = Arrays.asList("jpeg","png","jpg","svg","heic");
        List<String> videoFormats = Arrays.asList("mp4","avi","mkv","flv","wmv");

        String[] fileNameParts = attachmentInfo.getFileName().split("\\.");
        if (fileNameParts.length > 0 && fileNameParts[fileNameParts.length - 1].equals("pdf")){
            log.info("Gelen dosya pdf uzantılı");
            minioService.uploadPdf(attachmentInfo.getFileName(),attachmentInfo.getFileData());
        } else if (imageFormats.contains(fileNameParts[fileNameParts.length - 1].toLowerCase())) {
            log.info("Gelen dosya resim formatı uzantılı");
            minioService.uploadImage(attachmentInfo.getFileName(),attachmentInfo.getFileData());
        }else if (videoFormats.contains(fileNameParts[fileNameParts.length - 1].toLowerCase())) {
            log.info("Gelen dosya video formatı uzantılı");
            minioService.uploadVideo(attachmentInfo.getFileName(),attachmentInfo.getFileData());
        }

//        mailDto.getFileData().forEach(
//                attachmentInfo -> {
////                    Boolean.TRUE.equals(attachmentInfo
////                                    .getFileName()
////                                    .chars()
////                                    .mapToObj(c -> (char) c)
////                                    .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
////                                    .toString()
////                                    .equalsIgnoreCase("pdf"));
//                    String[] fileNameParts = attachmentInfo.getFileName().split("\\.");
//                    if (fileNameParts.length > 0 && fileNameParts[fileNameParts.length - 1].equals("pdf")){
//                        log.info("Gelen dosya pdf uzantılı");
//                        minioService.uploadPdf(attachmentInfo.getFileName(),attachmentInfo.getFileData());
//                    } else if (imageFormats.contains(fileNameParts[fileNameParts.length - 1].toLowerCase())) {
//                        log.info("Gelen dosya resim formatı uzantılı");
//                        minioService.uploadImage(attachmentInfo.getFileName(),attachmentInfo.getFileData());
//                    }
//                }
//        );
    }
}
