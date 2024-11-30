package com.argel6767.tailor.ai.openai;

import com.argel6767.tailor.ai.linkedin.LinkedInService;
import com.argel6767.tailor.ai.message.Author;
import com.argel6767.tailor.ai.message.Message;
import com.argel6767.tailor.ai.message.MessageService;
import com.argel6767.tailor.ai.message.requests.NewMessageRequest;
import com.argel6767.tailor.ai.message.responses.AiResponse;
import com.argel6767.tailor.ai.message.utils.MessageHistoryFlattener;
import com.argel6767.tailor.ai.pdf.PdfService;
import com.argel6767.tailor.ai.pdf.utils.FileConverter;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;


/**
 * Houses business logic for handling interactions with AI
 */
@Service
public class OpenAiService {
    private final String systemMessage = "You are a professional resume assistant AI specialized in tailoring resumes to meet the requirements of specific jobs " +
            "or professions. Your role is to analyze user-provided resumes, identify strengths, and suggest targeted improvements aligned with the " +
            "job description or industry standards. Emphasize relevant skills, experiences, and keywords to enhance the user’s appeal to recruiters " +
            "and hiring managers. Provide concise, professional suggestions and encourage follow-up questions to ensure tailored support.";

    private final ChatClient chatClient;
    private final MessageService messageService;
    private final PdfService pdfService;
    private final LinkedInService linkedInService;

    public OpenAiService(ChatClient chatClient, MessageService messageService, PdfService pdfService, LinkedInService linkedInService) {
        this.chatClient = chatClient;
        this.messageService = messageService;
        this.pdfService = pdfService;
        this.linkedInService = linkedInService;
    }

    /*
     * sends a request to OpenAi and returns on frontend
     * then uses  creating a new message Entity in the db, that being the Ai response
     */
    public ResponseEntity<AiResponse> getAiResponse(Long id, String message) {
        List<Message> messageHistory = messageService.getAllChatSessionMessages(id).getBody();
        String flattenedHistory = MessageHistoryFlattener.flattenMessageHistory(messageHistory);
        String completeHistory = MessageHistoryFlattener.addNewUserMessage(message, flattenedHistory);
       String response = chatClient.prompt()
                .user(completeHistory)
                .call()
                .content(); //TODO implement streaming eventually on backend, LEARN ASYNC WITH SPRINGBOOT SECURITY
        messageService.createMessage(new NewMessageRequest(response, Author.ASSISTANT), id);
        return ResponseEntity.ok(new AiResponse(response));
    }

    /*
     *  converts resume sent to a String and then sends the string representation of the resume along with profession and System instructions
     * to Chat Completions API to have resume tailored with suggestions from AI
     * then save the response to Chat session history
     */
    public ResponseEntity<AiResponse> sendPDFForReading(MultipartFile file, String profession, Long id) throws IOException {
      File rebuiltFile = FileConverter.convertMultipartFileToFile(file);
      String fileContent = pdfService.readFile(rebuiltFile);
      String prompt = "System: " + systemMessage + "\nUser: Tailor the following resume for the profession "+ profession + "\n" + fileContent;
      String response = chatClient.prompt().user(prompt).call().content();
      createFirstMessages(response, id);
      return ResponseEntity.ok(new AiResponse(response));
    }

    /*
     *  converts resume sent to a String and then sends the string representation of the resume along with the desired job's description and
     * System instructions to Chat Completions API to have resume tailored with suggestions from AI
     * then save the response to Chat session history
     */
    public ResponseEntity<AiResponse> sendPDFForReadingWithJob(MultipartFile file, String jobUrl, Long id) throws IOException {
        File rebuiltFile = FileConverter.convertMultipartFileToFile(file);
        String fileContent = pdfService.readFile(rebuiltFile);
        String jobDescription = linkedInService.getJobDetails(jobUrl);
        String prompt = "System: " + systemMessage + "\nUser: Tailor the following resume to the following job description and desired skills, if any: "+
                jobDescription + "\n" + fileContent;
        String response = chatClient.prompt().user(prompt).call().content();
        createFirstMessages(response, id);
        return ResponseEntity.ok(new AiResponse(response));
    }

    /*
     * save initial messages
     */
    private void createFirstMessages(String response, Long id) {
        messageService.createMessage(new NewMessageRequest("System message", Author.SYSTEM), id);
        messageService.createMessage(new NewMessageRequest(response, Author.ASSISTANT), id);
    }
}
