package com.argel6767.tailor.ai.openai;

import com.argel6767.tailor.ai.message.responses.AiResponse;
import com.argel6767.tailor.ai.openai.requests.AiMessageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
 * controller for ai endpoints
 */
@RequestMapping("/ai")
@RestController
public class OpenAiController {

    private final OpenAiService openAiService;

    public OpenAiController(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @PostMapping("/response")
    public ResponseEntity<AiResponse> getAiResponse(@RequestBody AiMessageRequest request) {
        return openAiService.getAiResponse(request.getChatSessionId(), request.getUserMessage());
    }

}
