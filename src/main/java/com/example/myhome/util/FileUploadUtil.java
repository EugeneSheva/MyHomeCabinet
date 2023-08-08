package com.example.myhome.util;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
@Component
@Data
@RequiredArgsConstructor
@Log
public class FileUploadUtil {


    /*

    Класс для сохранения файлов от клиента

     */
    @Value("${aws.bucket.name}")
    private String awsBucket;

    @Value("${upload.path}")
    private String uploadPath;
//    @Autowired
//    private S3Client s3Client;

    public void saveFile(String uploadDir, String fileName, MultipartFile file) throws IOException {

        log.info(this.uploadPath);

        Path path = Paths.get(uploadPath + uploadDir);

        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = path.resolve(fileName);
            log.info("Path for saving the image: " + filePath);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }

//    public void saveFileS3(String fileName, MultipartFile file) throws IOException {
//        try {
//
//            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
//                    .bucket(awsBucket)
//                    .key(fileName)
//                    .build();
//
//            PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
//
//            System.out.println("Complete!");
////            return "File uploaded successfully. S3 Object URL: " + response.sdkHttpResponse().getUri();
//        } catch (Exception e) {
////            return "Error uploading the file: " + e.getMessage();
//        }
//    }


//    public void deleteFileS3(String fileName) {
//        try {
//            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
//                    .bucket(awsBucket)
//                    .key(fileName)
//                    .build();
//
//            DeleteObjectResponse response = s3Client.deleteObject(deleteObjectRequest);
//
//        } catch (Exception e) {
//
//        }
//    }
}
