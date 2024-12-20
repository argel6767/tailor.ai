package com.argel6767.tailor.ai.chat_session;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * houses ChatSession endpoints
 */
@RestController
@RequestMapping("/chatsession")
public class ChatSessionController {

    private final ChatSessionService chatSessionService;

    public ChatSessionController(ChatSessionService chatSessionService) {
        this.chatSessionService = chatSessionService;
    }

    @PostMapping("/{email}")
    public ResponseEntity<ChatSession> createChatSession(@PathVariable String email) {
        return ResponseEntity.ok(chatSessionService.createChatSession(email));
    }

    @GetMapping("/all/{email}")
    public ResponseEntity<List<ChatSession>> getUserChatSessions(@PathVariable String email) {
        return chatSessionService.getAllUserChatSessions(email);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatSession> getChatSession(@PathVariable Long id) {
        ChatSession chatSession = chatSessionService.getChatSession(id);
        if (chatSession == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(chatSession);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ChatSession> deleteChatSession(@PathVariable Long id) {
        return chatSessionService.deleteChatSession(id);
    }

    @PutMapping("/{id}/name")
    public ResponseEntity<?> updateChatSessionName(@PathVariable Long id, @RequestBody String name) {
        return chatSessionService.updateChatSessionName(id, name);
    }
}
