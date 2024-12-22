package com.argel6767.tailor.ai.s3;

import com.argel6767.tailor.ai.chat_session.ChatSession;
import com.argel6767.tailor.ai.chat_session.ChatSessionService;
import com.argel6767.tailor.ai.pdf.utils.FileConverter;
import com.argel6767.tailor.ai.user.User;
import com.argel6767.tailor.ai.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * handles the service logic for uploading and downloading files from the s3 bucket
 */
@Service
public class S3Service {

    private final S3Client s3client;
    private final UserService userService;
    private final ChatSessionService chatSessionService;

    @Value("${bucket.name}")
    private String bucket;

    public S3Service(S3Client s3client, UserService userService, ChatSessionService chatSessionService) {
        this.s3client = s3client;
        this.userService = userService;
        this.chatSessionService = chatSessionService;
    }

    public ResponseEntity<?> uploadFile(MultipartFile file, Long chatSessionId) {
        File pdfFile = FileConverter.convertMultipartFileToFile(file);
        ChatSession chatSession = chatSessionService.getChatSession(chatSessionId);
        if (chatSession != null) {
            String key = generateKey(chatSessionId);
            chatSession.setS3FileKey(key);
            s3client.putObject(PutObjectRequest.builder().bucket(bucket).key(key).build(), RequestBody.fromFile(pdfFile));
            chatSessionService.saveChatSession(chatSession);
            return ResponseEntity.ok("Upload successful: " + key);
        }
        return ResponseEntity.notFound().build();
    }

    /*
     * generates key name for a chat session
     * the chat sessions id and resume
     */
    private String generateKey(Long chatSessionId) {
        return chatSessionId + "-resume";
    }

    /*
     * gets File from s3 to render on frontend
     */
    public ResponseEntity<?> getFile(Long chatSessionId) {
        try {
            ChatSession chatSession = chatSessionService.getChatSession(chatSessionId);
            if (chatSession != null) {
                return downloadFile(chatSession.getS3FileKey());
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (NoSuchKeyException shke) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(inputStream));
    }

    /*
     * deletes pdf file from S3 bucket
     * will be called when a chat session is deleted by user
     */
    public ResponseEntity<?> deleteFile(Long chatSessionId) {
        try {
            ChatSession chatSession = chatSessionService.getChatSession(chatSessionId);
            if (chatSession != null) {
                s3client.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(chatSession.getS3FileKey()).build());
                return new ResponseEntity<>("File deleted", HttpStatus.OK);
            }
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        }
        catch (NoSuchKeyException nske) {
            return ResponseEntity.notFound().build();
        }
    }

    /*
     * deletes all a user pdfs files from s3 bucket
     * this will be called when a user deletes their account
     */
    public ResponseEntity<?> deleteAllFiles(String userEmail) {
        try {
            User user = userService.getUserByEmail(userEmail);
            List<ChatSession> chatSessions = user.getChatSessions();
            if (chatSessions.isEmpty()) { //no files to delete
                return ResponseEntity.ok().build();
            }
            List<ObjectIdentifier> fileKeys = new ArrayList<>();
            chatSessions.forEach(chatSession -> {
                ObjectIdentifier objectKey = ObjectIdentifier.builder().key(chatSession.getS3FileKey()).build();
                fileKeys.add(objectKey);
            });
            var delete = Delete.builder().objects(fileKeys).build();
            s3client.deleteObjects(DeleteObjectsRequest.builder().bucket(bucket).delete(delete).build());
            return new ResponseEntity<>("Files deleted", HttpStatus.OK);
        }
        catch (UsernameNotFoundException unfe) {
            return ResponseEntity.notFound().build();
        }
        catch (SdkException se) {
            return ResponseEntity.internalServerError().body(se.getMessage());
        }
    }

    /*
     * getters and setters
     */
    public S3Client getS3client() {
        return s3client;
    }

    private UserService getUserService() {
        return userService;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

}
