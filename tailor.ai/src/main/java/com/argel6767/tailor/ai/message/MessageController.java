package com.argel6767.tailor.ai.message;

import com.argel6767.tailor.ai.message.requests.NewMessageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("message/")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<Message> addMessage(@RequestBody Message message) {
        return messageService.addMessage(message);
    }

    @PostMapping("create/{chatSessionId}")
    public ResponseEntity<Message> createMessage(@PathVariable Long chatSessionId, @RequestBody NewMessageRequest newMessageRequest) {
        return messageService.createMessage(newMessageRequest, chatSessionId);
    }

    @GetMapping("{chatSessionId}")
    public ResponseEntity<List<Message>> getAllChatMessages(@PathVariable Long chatSessionId) {
        return messageService.getAllChatSessionMessages(chatSessionId);
    }

}
