package com.argel6767.tailor.ai.message;

import com.argel6767.tailor.ai.chat_session.ChatSession;
import com.argel6767.tailor.ai.chat_session.ChatSessionService;
import com.argel6767.tailor.ai.message.requests.NewMessageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Houses all business logic of messages
 */
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatSessionService chatSessionService;

    public MessageService(MessageRepository messageRepository, ChatSessionService chatSessionService) {
        this.messageRepository = messageRepository;
        this.chatSessionService = chatSessionService;
    }

    /*
     * add a message
     */
    public ResponseEntity<Message> addMessage(Message message) {
        return ResponseEntity.ok(messageRepository.save(message));
    }

    /*
     * create a message
     */
    public ResponseEntity<Message> createMessage(NewMessageRequest newMessageRequest, Long chatSessionId) {
        ChatSession chatSession = chatSessionService.getChatSession(chatSessionId);
        if (chatSession != null) {
            return createNewMessageAndAttachToParentChatSession(newMessageRequest, chatSession);
        }
        return ResponseEntity.notFound().build();
    }

    /*
     * get all the messages from a chat session
     */
    public ResponseEntity<List<Message>> getAllChatSessionMessages(Long chatSessionId) {
        ChatSession chatSession = chatSessionService.getChatSession(chatSessionId);
        if (chatSession != null) {
            return ResponseEntity.ok(chatSession.getMessages());
        }
        return ResponseEntity.notFound().build();
    }


    /*
     * save both the message entity and parent chat session entity
     * TODO Get rid of this, no longer needed, as ChatSession has cascading
     */
    private ResponseEntity<Message> createNewMessageAndAttachToParentChatSession(NewMessageRequest newMessageRequest, ChatSession chatSession) {
        Message message = new Message();
        message.setAuthor(newMessageRequest.getAuthor());
        message.setBody(newMessageRequest.getMessage());
        message.setChatSession(chatSession);
        chatSession.getMessages().add(message);
        chatSessionService.saveChatSession(chatSession);
        return ResponseEntity.ok(messageRepository.save(message));
    }
}
