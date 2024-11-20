package com.argel6767.tailor.ai.openai;

import com.argel6767.tailor.ai.message.Author;
import com.argel6767.tailor.ai.message.Message;
import com.argel6767.tailor.ai.message.MessageService;
import com.argel6767.tailor.ai.message.requests.NewMessageRequest;
import com.argel6767.tailor.ai.message.responses.AiResponse;
import com.argel6767.tailor.ai.message.utils.MessageHistoryFlattener;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Houses business logic for
 */
@Service
public class OpenAiService {
    private final ChatClient chatClient;
    private final MessageService messageService;



    public OpenAiService(ChatClient chatClient, MessageService messageService) {
        this.chatClient = chatClient;
        this.messageService = messageService;
    }

    /*
     * sends a request to OpenAi and returns as a stream to allow for streaming effect on frontend
     * then uses .reduce() to asynchronously creating a new message Entity in the db, that being the Ai response
     */
    public ResponseEntity<AiResponse> getAiResponse(Long id, String message) {
        List<Message> messageHistory = messageService.getAllChatSessionMessages(id).getBody();
        String flattenedHistory = MessageHistoryFlattener.flattenMessageHistory(messageHistory);
        String completeHistory = MessageHistoryFlattener.addNewUserMessage(message, flattenedHistory);
       String response = chatClient.prompt()
                .user(completeHistory)
                .call()
                .content(); //TODO implement streaminf evtually on backend, LEARN ASYNC WITH SPRINGBOOT SECURITY
        messageService.createMessage(new NewMessageRequest(response, Author.ASSISTANT), id);
        return ResponseEntity.ok(new AiResponse(response));
    }

}
