package com.example.myhome.home.configuration;

import com.example.myhome.home.dto.SecretData;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import software.amazon.awssdk.services.secretsmanager.model.SecretsManagerException;
//@Component
public class AwsS3Config {
//    String secretName = "credentials";
//    String awsAccessKey;
//    String awsSecretKey;
//    Region region = Region.EU_NORTH_1;
//
//    @Value("${aws.bucket.name}")
//    private String awsBucket;
//    @Bean
//    public S3Client s3Client() {
//        getSecters();
//        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(awsAccessKey, awsSecretKey);
//        return S3Client.builder()
//                .credentialsProvider(() -> awsCredentials)
//                .region(region)
//                .build();
//    }
//
//    private void getSecters() {
//        SecretsManagerClient secretsManagerClient = SecretsManagerClient.builder()
//                .region(region)
//                .build();
//
//        GetSecretValueRequest secretValueRequest = GetSecretValueRequest.builder()
//                .secretId(secretName)
//                .build();
//
//        try {
//            GetSecretValueResponse secretValueResponse = secretsManagerClient.getSecretValue(secretValueRequest);
//            String secretString = secretValueResponse.secretString();
//
//            System.out.println("Secret: " + secretString);
//
//            Gson gson = new Gson();
//            SecretData secretData = gson.fromJson(secretString, SecretData.class);
//
//            awsSecretKey = secretData.getSecretAccessKey();
//            awsAccessKey = secretData.getAccessKey();
//
//            System.out.println("SecretAccessKey: " + awsSecretKey);
//            System.out.println("AccessKey: " + awsAccessKey);
//
//        } catch (SecretsManagerException e) {
//            System.err.println("Error can`t get secrets: " + e.getMessage());
//        }
//    }
}
