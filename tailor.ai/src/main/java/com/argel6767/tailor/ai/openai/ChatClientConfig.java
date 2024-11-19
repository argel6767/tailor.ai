package com.argel6767.tailor.ai.openai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    /*
     * builds a chatClient bean that uses InMemoryChatMemory to allow for
     */
    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .build();
    }
}
