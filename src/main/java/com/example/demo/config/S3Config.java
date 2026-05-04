package com.example.demo.config;


import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import com.example.demo.properties.AwsProperties;
import com.example.demo.util.AwsSecretUtil;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
@ConditionalOnProperty(prefix = "aws.s3", name = "enabled", havingValue = "true")
public class S3Config {

    private static final Logger log = LoggerFactory.getLogger(S3Config.class);

    private final AwsSecretUtil secretsService;
    private final AwsProperties props;

    public S3Config(AwsSecretUtil secretsService, AwsProperties props) {
        this.secretsService = secretsService;
        this.props = props;
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(props.getRegion()))
                .credentialsProvider(credentialsProvider())
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .region(Region.of(props.getRegion()))
                .credentialsProvider(credentialsProvider())
                .build();
    }

    private AwsCredentialsProvider credentialsProvider() {
        try {
            Map<String, String> secrets = secretsService.getSecret();
            String accessKey = secrets.get("accessKey");
            String secretKey = secrets.get("secretKey");

            if (accessKey != null && !accessKey.isBlank() && secretKey != null && !secretKey.isBlank()) {
                AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
                return StaticCredentialsProvider.create(credentials);
            }

            log.warn("AWS secret '{}' did not contain accessKey/secretKey; falling back to default credentials provider",
                    props.getSecret().getName());
        } catch (Exception ex) {
            log.warn("Unable to load AWS credentials from Secrets Manager; falling back to default credentials provider: {}",
                    ex.getMessage());
        }

        return DefaultCredentialsProvider.create();
    }
}
