package com.argel6767.tailor.ai.openai;

import com.argel6767.tailor.ai.message.responses.AiResponse;
import com.argel6767.tailor.ai.openai.requests.AiMessageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    /*
     * endpoint for file reading to tailor resume with just the user's desired profession
     */
    @PostMapping("/file/{id}")
    public ResponseEntity<AiResponse> submitFileToAi(@RequestParam("file") MultipartFile file, @RequestParam String profession, @PathVariable Long id)  {
        try {
            return openAiService.sendPDFForReading(file, profession, id);
        } catch (IOException e) {
            ResponseEntity.internalServerError().body(e.getMessage());
        }
        return ResponseEntity.badRequest().build();
    }

    /*
     * endpoint for file reading to tailor resume with the specific job specified by its url
     */
    @PostMapping("/file/{id}/job")
    public ResponseEntity<AiResponse> submitFileToAiWithJob(@RequestParam("file") MultipartFile file, @RequestParam("jobUrl") String jobUrl, @PathVariable Long id)  {
        try {
            return openAiService.sendPDFForReadingWithJob(file, jobUrl, id);
        } catch (Exception e) {
           ResponseEntity.internalServerError().body(e.getMessage());
        }
        return ResponseEntity.badRequest().build();
    }

}
