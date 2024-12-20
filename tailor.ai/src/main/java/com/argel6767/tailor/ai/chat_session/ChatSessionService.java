package com.argel6767.tailor.ai.chat_session;

import com.argel6767.tailor.ai.pdf.utils.FileConverter;
import com.argel6767.tailor.ai.s3.S3Service;
import com.argel6767.tailor.ai.user.User;
import com.argel6767.tailor.ai.user.UserRepository;
import com.argel6767.tailor.ai.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * has service logic for dealing with ChatSession Entity
 */
@Service
public class ChatSessionService {

    private final ChatSessionRepository chatSessionRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public ChatSessionService(ChatSessionRepository chatSessionRepository, UserService userService,  UserRepository userRepository) {
        this.chatSessionRepository = chatSessionRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public ChatSession addChatSession(ChatSession chatSession) {
        return chatSessionRepository.save(chatSession);
    }

    /*
     * creates new ChatSession
     * the user's email is also grabbed to allow for being able to link the user in the db, handling the relationship
     */
    public ChatSession createChatSession(String email) {
        ChatSession chatSession = new ChatSession();
        linkUserToChatSession(email, chatSession);
        return chatSessionRepository.save(chatSession);
    }


    /*
     * returns all chat sessions of a user
     */
    public ResponseEntity<List<ChatSession>> getAllUserChatSessions(String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user.getChatSessions());
    }

    /*
     * returns chat session
     */
    public ChatSession getChatSession(Long id) {
        return chatSessionRepository.findById(id).orElse(null);
    }

    /*
     * deletes chat session
     */
    public ResponseEntity<ChatSession> deleteChatSession(Long id) {
        ChatSession chatSession = chatSessionRepository.findById(id).orElse(null);
        if (chatSession != null) {
            chatSessionRepository.deleteById(id);
            return ResponseEntity.ok(chatSession);
        }
        return ResponseEntity.notFound().build();
    }

    public ChatSession saveChatSession(ChatSession chatSession) {
        return chatSessionRepository.save(chatSession);
    }



    /*
     * allows user to update chatSessionName
     */
    public ResponseEntity<?> updateChatSessionName(Long chatSessionId, String name) {
        ChatSession chatSession = chatSessionRepository.findById(chatSessionId).orElse(null);
        if (chatSession != null) {
            chatSession.setChatSessionName(name);
            return ResponseEntity.ok(chatSessionRepository.save(chatSession));
        }
        return ResponseEntity.notFound().build();
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