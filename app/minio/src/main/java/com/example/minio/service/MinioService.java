package com.example.minio.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@Slf4j
public class MinioService {

    @Value("${spring.minio.pdf-bucket}")
    private String pdfBucketName;

    @Value("${spring.minio.image-bucket}")
    private String imageBucketName;

    @Value("${spring.minio.video-bucket}")
    private String videoBucketName;

    private final MinioClient minioClient;

    public MinioService(MinioClient minioClient){
        this.minioClient = minioClient;
    }

    public void uploadPdf(String fileName, byte[] fileData){
        InputStream inputStream = new ByteArrayInputStream(fileData);
        try {
            minioClient.putObject(
                    PutObjectArgs
                            .builder()
                            .bucket(pdfBucketName)
                            .object(fileName)
                            .stream(inputStream, inputStream.available(),-1)
                            .build()
            );
            log.info("PDF upload success");
        }catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e){
            log.info("PDF upload is failed");
        }
    }

    public void uploadImage(String fileName, byte[] fileData){
        InputStream inputStream = new ByteArrayInputStream(fileData);
        try {
            minioClient.putObject(
                    PutObjectArgs
                            .builder()
                            .bucket(imageBucketName)
                            .object(fileName)
                            .stream(inputStream, inputStream.available(),-1)
                            .build()
            );
            log.info("Image upload success");
        }catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e){
            log.info("Image upload is failed");
        }
    }

    public void uploadVideo(String fileName, byte[] fileData) {
        InputStream inputStream = new ByteArrayInputStream(fileData);
        try {
            minioClient.putObject(
                    PutObjectArgs
                            .builder()
                            .bucket(videoBucketName)
                            .object(fileName)
                            .stream(inputStream, inputStream.available(),-1)
                            .build()
            );
            log.info("Video upload success");
        }catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e){
            log.info("Video upload is failed");
        }
    }
}
