package com.argel6767.tailor.ai.chat_session;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/chatsession")
public class ChatSessionController {

    private final ChatSessionService chatSessionService;

    public ChatSessionController(ChatSessionService chatSessionService) {
        this.chatSessionService = chatSessionService;
    }

    @PostMapping("/{email}")
    public ResponseEntity<ChatSession> createChatSession(@RequestParam("file") MultipartFile file, @PathVariable String email) {
        return ResponseEntity.ok(chatSessionService.createChatSession(file, email));
    }

    @GetMapping("/pdf/{id}")
    public ResponseEntity<?> getChatSessionPDF(@PathVariable Long id) {
        return chatSessionService.getChatSessionPDF(id);
    }

    @GetMapping("/{email}")
    public ResponseEntity<List<ChatSession>> getUserChatSessions(@PathVariable String email) {
        return chatSessionService.getAllUserChatSessions(email);
    }


}