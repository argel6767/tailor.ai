package com.argel6767.tailor.ai.message;

import com.argel6767.tailor.ai.chat_session.ChatSession;
import com.argel6767.tailor.ai.chat_session.ChatSessionService;
import com.argel6767.tailor.ai.message.requests.NewMessageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatSessionService chatSessionService;

    public MessageService(MessageRepository messageRepository, ChatSessionService chatSessionService) {
        this.messageRepository = messageRepository;
        this.chatSessionService = chatSessionService;
    }

    public Message addMessage(Message message) {
        return messageRepository.save(message);
    }

    public ResponseEntity<Message> createMessage(NewMessageRequest newMessageRequest, Long chatSessionId) {
        ChatSession chatSession = chatSessionService.getChatSession(chatSessionId);
        if (chatSession != null) {
            return createNewMessageAndAttachToParentChatSession(newMessageRequest, chatSession);
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<List<Message>> getAllChatSessionMessages(Long chatSessionId) {
        ChatSession chatSession = chatSessionService.getChatSession(chatSessionId);
        if (chatSession != null) {
            return ResponseEntity.ok(chatSession.getMessages());
        }
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<Message> createNewMessageAndAttachToParentChatSession(NewMessageRequest newMessageRequest, ChatSession chatSession) {
        Message message = new Message();
        message.setAuthor(newMessageRequest.getAuthor());
        message.setBody(newMessageRequest.getMessage());
        message.setChatSession(chatSession);
        chatSession.getMessages().add(message);
        chatSessionService.saveChatSession(chatSession);
        return ResponseEntity.ok(message);
    }
}
