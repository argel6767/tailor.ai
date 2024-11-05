package com.argel6767.tailor.ai.s3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.Response;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.nio.ByteBuffer;

@Service
public class S3Service {

    @Autowired
    private S3Client s3client;

    @Value("${bucket.name}")
    private String bucket;

     String generateKey(Long chatSessionId) {
        return chatSessionId + "-resume";
    }

    public void uploadFile(String key, File userPdf) {
         s3client.putObject(PutObjectRequest.builder().bucket(bucket).key(key).build(), RequestBody.fromFile(userPdf));
    }

    public ResponseEntity<InputStreamSource> downloadFile(String key) {
         
    }


}
