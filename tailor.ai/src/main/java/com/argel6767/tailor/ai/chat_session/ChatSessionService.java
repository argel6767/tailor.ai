package com.argel6767.tailor.ai.chat_session;

import com.argel6767.tailor.ai.message.MessageService;
import com.argel6767.tailor.ai.s3.S3Service;
import com.argel6767.tailor.ai.user.User;
import com.argel6767.tailor.ai.user.UserRepository;
import com.argel6767.tailor.ai.user.UserService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * has service logic for dealing with ChatSession Entity
 */
@Service
public class ChatSessionService {

    private final ChatSessionRepository chatSessionRepository;
    private final MessageService messageService;
    private final UserService userService;
    private final S3Service s3Service;
    private final UserRepository userRepository;

    public ChatSessionService(ChatSessionRepository chatSessionRepository, MessageService messageService, UserService userService, S3Service s3Service, UserRepository userRepository) {
        this.chatSessionRepository = chatSessionRepository;
        this.messageService = messageService;
        this.userService = userService;
        this.s3Service = s3Service;
        this.userRepository = userRepository;
    }


    public ChatSession addChatSession(ChatSession chatSession) {
        return chatSessionRepository.save(chatSession);
    }

    /*
     * creates new ChatSession while uploading file that is attached to the chatSession to the S3 bucket
     * the user's email is also grabbed to allow for being able to link the user in the db, handling the relationship
     */
    public ChatSession createChatSession(MultipartFile pdfFileMulti, String email) {
        File pdfFile = convertMultipartFileToFile(pdfFileMulti);
        ChatSession chatSession = new ChatSession();
        linkUserToChatSession(email, chatSession);
        String key = s3Service.uploadFile(chatSession.getChatSessionId(), pdfFile);
        chatSession.setS3FileKey(key);
        return chatSessionRepository.save(chatSession);
    }

    /*
     * grabs pdf file attached to chat session given by the chatSession id
     */
    public ResponseEntity<?> getChatSessionPDF(Long chatSessionId) {
        ChatSession chatSession = chatSessionRepository.findById(chatSessionId).orElse(null);
        if (chatSession != null) {
            String key = chatSession.getS3FileKey();
            return s3Service.downloadFile(key);
        }
        return ResponseEntity.notFound().build();
    }

    /*
     * returns all chat sessions of a user
     */
    public ResponseEntity<List<ChatSession>> getAllUserChatSessions(String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user.getChatSessions());
    }

    /*
     * converts the file back into a regular File object
     */
    private File convertMultipartFileToFile(MultipartFile multipartFile)  {
        // Create a temporary file and copy the contents of the multipart file
        File file = null;
        try {
            file = File.createTempFile("uploaded-", multipartFile.getOriginalFilename());
            multipartFile.transferTo(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    /*
     * links the chatSession to the User that made the session
     */
    private void linkUserToChatSession(String email, ChatSession chatSession) {
        chatSessionRepository.save(chatSession);
        User user = userService.getUserByEmail(email);
        chatSession.setUser(user);
        user.getChatSessions().add(chatSession);
        userRepository.save(user);
    }

}