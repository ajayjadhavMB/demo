package com.example.demo.service;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.FileAsset;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.properties.AwsProperties;
import com.example.demo.repository.FileAssetRepository;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final FileAssetRepository fileAssetRepository;
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final AwsProperties awsProperties;

    public FileAsset upload(MultipartFile file) throws IOException {
        String objectKey = storeInS3(
                safeName(file.getOriginalFilename()),
                file.getContentType(),
                file.getBytes()
        );

        FileAsset fileAsset = FileAsset.builder()
                .originalName(file.getOriginalFilename())
                .contentType(file.getContentType() == null ? "application/octet-stream" : file.getContentType())
                .fileSize(file.getSize())
                .objectKey(objectKey)
                .build();

        return fileAssetRepository.save(fileAsset);
    }

    public String storeInS3(String originalName, String contentType, byte[] fileBytes) {
        String objectKey = UUID.randomUUID() + "_" + safeName(originalName);

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(awsProperties.getS3().getBucket())
                        .key(objectKey)
                        .contentType(contentType == null ? "application/octet-stream" : contentType)
                        .build(),
                RequestBody.fromBytes(fileBytes)
        );

        return objectKey;
    }

    public List<FileAsset> findAll() {
        return fileAssetRepository.findAllByOrderByCreatedAtDesc();
    }

    public FileAsset getById(Long id) {
        return fileAssetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with id: " + id));
    }

    public void delete(Long id) {
        FileAsset fileAsset = getById(id);
        if (fileAsset.getObjectKey() != null && !fileAsset.getObjectKey().isBlank()) {
            s3Client.deleteObject(
                    DeleteObjectRequest.builder()
                            .bucket(awsProperties.getS3().getBucket())
                            .key(fileAsset.getObjectKey())
                            .build()
            );
        }
        fileAssetRepository.delete(fileAsset);
    }

    public String createAccessUrl(FileAsset fileAsset) {
        if (fileAsset.getObjectKey() == null || fileAsset.getObjectKey().isBlank()) {
            return null;
        }

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .getObjectRequest(GetObjectRequest.builder()
                        .bucket(awsProperties.getS3().getBucket())
                        .key(fileAsset.getObjectKey())
                        .build())
                .build();

        PresignedGetObjectRequest presigned = s3Presigner.presignGetObject(presignRequest);
        return presigned.url().toString();
    }

    private String safeName(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return "file";
        }
        return fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
