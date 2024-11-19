package com.argel6767.tailor.ai.openai;

import com.argel6767.tailor.ai.message.Author;
import com.argel6767.tailor.ai.message.Message;
import com.argel6767.tailor.ai.message.MessageService;
import com.argel6767.tailor.ai.message.requests.NewMessageRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Houses business logic for
 */
@Service
public class OpenAiService {
    private final ChatClient chatClient;
    private final MessageService messageService;

    @Value("${OPEN_AI_API}")
    private String apiKey;


    public OpenAiService(ChatClient chatClient, MessageService messageService) {
        this.chatClient = chatClient;
        this.messageService = messageService;
    }

    /*
     * sends a request to OpenAi and returns as a stream to allow for streaming effect on frontend
     * then uses .reduce() to asynchronously creating a new message Entity in the db, that being the Ai response
     */
    public Flux<String> getAiResponse(Long id, String message) {
        return chatClient.prompt()
                .user(message)
                .stream()
                .content()
                .publishOn(Schedulers.boundedElastic())
                .collectList()
                .flatMap(chunks -> {
                    // Concatenate all chunks
                    String fullMessage = String.join("", chunks);

                    // Create message request
                    NewMessageRequest request = new NewMessageRequest(fullMessage, Author.ASSISTANT);

                    // Save to database and return original stream
                    return Mono.fromCallable(() -> messageService.createMessage(request, id))
                            .thenReturn(Flux.fromIterable(chunks));
                })
                .flatMapMany(Function.identity());
    }



}
