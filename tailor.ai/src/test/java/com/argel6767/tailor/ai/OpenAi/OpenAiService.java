package com.argel6767.tailor.ai.OpenAi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OpenAiService {

    @Value("${OPEN_AI_API}")
    private String apiKey;
}
