package com.example.demo.util;

import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.example.demo.properties.AwsProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;


@Component
@ConditionalOnProperty(prefix = "aws.s3", name = "enabled", havingValue = "true")
public class AwsSecretUtil {

    private final AwsProperties awsProperties;
    private final ObjectMapper mapper = new ObjectMapper();
    private final SecretsManagerClient client;

    public AwsSecretUtil(AwsProperties awsProperties) {

        this.awsProperties = awsProperties;

        this.client = SecretsManagerClient.builder()
                .region(Region.of(awsProperties.getRegion()))
                .build();
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> getSecret() {
        String secretName = awsProperties.getSecret().getName();
        if (secretName == null || secretName.isBlank()) {
            throw new IllegalStateException("Missing required configuration property: aws.secret.name");
        }

        GetSecretValueResponse response = client.getSecretValue(
                GetSecretValueRequest.builder()
                        .secretId(secretName)
                        .build()
        );

        try {
            return mapper.readValue(response.secretString(), Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Secret parsing failed", e);
        }
    }
}
