package com.example.minio.config;

import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.RemoveBucketArgs;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class MinioConfig {

    @Value("${spring.minio.url}")
    private String url;
    @Value("${spring.minio.accessKey}")
    private String userName;
    @Value("${spring.minio.secretKey}")
    private String password;
    @Value("${spring.minio.image-bucket}")
    private String imgBucketName;
    @Value("${spring.minio.video-bucket}")
    private String videoBucketName;
    @Value("${spring.minio.pdf-bucket}")
    private String pdfBucketName;

    // Minio client'i oluşturduk bu client üzerinden minio işlemlerini gerçekleştiriyoruz.
    @Bean
    public MinioClient client(){
        return MinioClient.builder()
                .endpoint(url)
                .credentials(userName,password)
                .build();
    }

//    @Bean
//    public void makeImageBucket() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
//        client().makeBucket(
//                MakeBucketArgs
//                        .builder()
//                        .bucket(imgBucketName)
//                        .build()
//        );
//
//    }
//
//    // bucket oluşturma
//    @Bean
//    public void makeVideoBucket() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
//        client().makeBucket(
//                MakeBucketArgs
//                        .builder()
//                        .bucket(videoBucketName)
//                        .build()
//        );
//
//    }
//
//    // bucket oluşturma
//    @Bean
//    public void makePdfBucket() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
//        client().makeBucket(
//                MakeBucketArgs
//                        .builder()
//                        .bucket(pdfBucketName)
//                        .build()
//        );
//
//    }
//
//    // bucke silme
//    @Bean
//    public void deleteBucket() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
//        client().removeBucket(
//                RemoveBucketArgs
//                        .builder()
//                        .bucket("deneme")
//                        .build()
//        );
//    }
}
