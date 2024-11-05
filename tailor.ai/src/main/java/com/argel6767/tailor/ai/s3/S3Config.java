package com.argel6767.tailor.ai.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.regions.Region;

/*
 * configures and builds an S3 client with the desired region
 */
@Configuration
public class S3Config {

    @Value("${spring.cloud.aws.region.static}")
    private String region;

    @Bean
    public S3Client s3Client() {
        return  S3Client.builder().region(Region.of(region)).build();
    }
}
