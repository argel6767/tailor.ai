package com.argel6767.tailor.ai.s3;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Holds all endpoints that interact wih S3 bucket
 */
@RequestMapping("/s3")
@RestController
public class S3Controller {

    private final S3Service s3Service;

    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping("/pdf/{chatSessionId}")
    public ResponseEntity<?> uploadPDF(@RequestParam("file") MultipartFile file, @PathVariable Long chatSessionId) {
        return s3Service.uploadFile(file, chatSessionId);
    }

    @GetMapping("/pdf/{chatSessionId}")
    public ResponseEntity<?> downloadPDF(@PathVariable Long chatSessionId) {
        return s3Service.getFile(chatSessionId);
    }

    @DeleteMapping("/pdf/{chatSessionId}")
    public ResponseEntity<?> deletePDF(@PathVariable Long chatSessionId) {
        return s3Service.deleteFile(chatSessionId);
    }

    @DeleteMapping("/{userEmail}")
    public ResponseEntity<?> deleteAllUserFiles(@PathVariable String userEmail) {
        return s3Service.deleteAllFiles(userEmail);
    }
}
