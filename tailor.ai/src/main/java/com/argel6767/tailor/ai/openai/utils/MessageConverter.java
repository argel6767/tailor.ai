package com.argel6767.tailor.ai.openai.utils;

import com.argel6767.tailor.ai.message.Author;
import com.argel6767.tailor.ai.message.Message;
import org.springframework.ai.openai.api.OpenAiApi;

import java.util.ArrayList;
import java.util.List;

public class MessageConverter {

    public static List<OpenAiApi.ChatCompletionMessage> convertToChatCompletion(List<Message> messages) {
        List<OpenAiApi.ChatCompletionMessage> chatCompletionMessages = new ArrayList<>();
        for (Message message : messages) {
            String body = message.getBody();
            OpenAiApi.ChatCompletionMessage.Role role = convertAuthorToRole(message.getAuthor());
            chatCompletionMessages.add(new OpenAiApi.ChatCompletionMessage(body, role));
        }
        return chatCompletionMessages;
    }

    public static OpenAiApi.ChatCompletionMessage.Role convertAuthorToRole(Author author) {
        return OpenAiApi.ChatCompletionMessage.Role.valueOf(author.name());
    }
}
