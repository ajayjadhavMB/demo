package com.example.demo.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.properties.AwsProperties;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@ConditionalOnProperty(prefix = "aws.s3", name = "enabled", havingValue = "true")
public class S3Service {

    private final S3Client s3Client;
    private final AwsProperties props;

    public S3Service(S3Client s3Client, AwsProperties props) {
        this.s3Client = s3Client;
        this.props = props;
    }

    public String upload(MultipartFile file) throws IOException {

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(props.getS3().getBucket())
                        .key(fileName)
                        .contentType(file.getContentType())
                        .build(),
                RequestBody.fromBytes(file.getBytes())
        );

        return fileName;
    }

    public byte[] download(String fileName) {

        return s3Client.getObjectAsBytes(
                GetObjectRequest.builder()
                        .bucket(props.getS3().getBucket())
                        .key(fileName)
                        .build()
        ).asByteArray();
    }

    public void delete(String fileName) {

        s3Client.deleteObject(
                DeleteObjectRequest.builder()
                        .bucket(props.getS3().getBucket())
                        .key(fileName)
                        .build()
        );
    }
}
