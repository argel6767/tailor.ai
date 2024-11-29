package com.argel6767.tailor.ai.linkedin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(LinkedInService.class);
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
            log.info("Getting job details for {}", jobUrl);
            Map response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(endpoint)
                            .queryParam("job_url", jobUrl)
                            .queryParam("include_skills", "true")
                            .queryParam("include_hiring_team", "false")
                            .build())
                    .retrieve()
                    .body(Map.class);
            Map<String, Object> data = (Map<String, Object>) response.get("data"); //job info is an inner data object
            String jobDescription = (String) data.get("job_description");
            List<Object> skills = (List<Object>) data.get("skills");
            return "Here is the job description and skills for the desired job:\n" + jobDescription + "\n" + skills;
        }
        catch (Exception e) {
            log.info(e.getMessage(), e);
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
