package com.argel6767.tailor.ai.chat_session;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/chatsession")
public class ChatSessionController {

    private final ChatSessionService chatSessionService;

    public ChatSessionController(ChatSessionService chatSessionService) {
        this.chatSessionService = chatSessionService;
    }

    @PostMapping("/session")
    public ResponseEntity<ChatSession> createChatSession(@RequestParam("file") MultipartFile file, @RequestBody String email) {
        return ResponseEntity.ok(chatSessionService.createChatSession(file, email));
    }

}
