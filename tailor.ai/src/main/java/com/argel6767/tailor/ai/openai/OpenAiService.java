package com.argel6767.tailor.ai.openai;

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
 * Houses business logic for
 */
@Service
public class OpenAiService {
    private final String systemMessage = "You are a professional resume assistant AI specialized in tailoring resumes to match the requirements and expectations of specific professions. Your role is to analyze user-provided resumes, identify strengths, " +
            "and suggest targeted improvements based on the desired job or profession. Focus on emphasizing relevant skills, experiences, and keywords that align with the job description or industry standards. Ensure your responses are concise, professional, " +
            "and tailored to enhance the user’s chances of standing out to recruiters and hiring managers. Make sure to be give suggestions and open the conversation for follow up questions.";
    private final ChatClient chatClient;
    private final MessageService messageService;
    private final PdfService pdfService;



    public OpenAiService(ChatClient chatClient, MessageService messageService, PdfService pdfService) {
        this.chatClient = chatClient;
        this.messageService = messageService;
        this.pdfService = pdfService;
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
      messageService.createMessage(new NewMessageRequest("System Message", Author.SYSTEM), id);
      String response = chatClient.prompt().user(prompt).call().content();
      messageService.createMessage(new NewMessageRequest(response, Author.ASSISTANT), id);
      return ResponseEntity.ok(new AiResponse(response));
    }

    /*
     * save initial messages
     */
    private void createFirstMessages(String fileContent, Long id) {
        messageService.createMessage(new NewMessageRequest(systemMessage, Author.SYSTEM), id);
        messageService.createMessage(new NewMessageRequest(fileContent, Author.USER), id);
    }
}
