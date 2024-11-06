package com.argel6767.tailor.ai.s3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;


import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;

import java.io.InputStream;


/**
 * handles the service logic for uploading and downloading files from the s3 bucket
 */
@Service
public class S3Service {

    @Autowired
    private S3Client s3client;

    @Value("${bucket.name}")
    private String bucket;

     String generateKey(Long chatSessionId) {
        return chatSessionId + "-resume";
    }

    /*
    * uploads given file to the s3 bucket
     */
    public void uploadFile(String key, File userPdf) {
         s3client.putObject(PutObjectRequest.builder().bucket(bucket).key(key).build(), RequestBody.fromFile(userPdf));
    }

    /*
     * grabs pdf file as a stream to allow for rendering on the fronted.
     */
    public ResponseEntity<InputStreamResource> downloadFile(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucket).key(key).build();
        InputStream inputStream = s3client.getObject(getObjectRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + key + ".pdf\"");
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(inputStream);
    }

    /*
     * getters and setters
     */
    public S3Client getS3client() {
        return s3client;
    }

    public void setS3client(S3Client s3client) {
        this.s3client = s3client;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }


}
