package com.argel6767.tailor.ai.linkedin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


import java.util.List;
import java.util.Map;

/**
 * holds the api calls made to the LinkedIn scraper API
 */
@Service
public class LinkedInService {

    @Value("${job.description.endpoint}")
    private String endpoint;

    private final RestClient restClient;

    public LinkedInService(RestClient restClient) {
        this.restClient = restClient;
    }

    /*
     * calls the get description endpoint to get the job details of the desired job
     */
    public String getJobDetails(String jobUrl) {
        try {
            Map response = restClient.get()
                    .uri(endpoint)
                    .retrieve()
                    .body(Map.class);
            String jobDescription = (String) response.get("job_description");
            List<Object> skills = (List<Object>) response.get("skills");
            return "Here is the job description and skills for the desired job:\n" + jobDescription + "\n" + skills;
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /*
     * package private getters and setters for testing
     */
    String getEndpoint() {
        return endpoint;
    }

    void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

}
