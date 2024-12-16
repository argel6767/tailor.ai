# Tailor.AI

Tailor.AI is an innovative application designed to help users customize their resumes for specific professions using the power of AI. With Tailor.AI, you can craft profession-specific resumes quickly and effectively, giving you the edge you need to stand out in the job market.

## Features

- Email verification using JavaMailSender for secure account activation.
- Upload your resume and specify a desired job or profession.
- Get tailored suggestions to align your resume with the job description.
- Easy-to-use interface for creating polished, professional resumes.
- Secure user authentication and data handling.

## Tech Stack

### Backend

- **Framework**: Spring Boot 3
- **Authentication**: JWT-based stateless session management
- **Database**: Heroku hosted PostgreSQL database
- **Storage**: AWS S3 Bucket
- **Language**: Java 17
- **API**: RESTful endpoints for processing user data and integrating AI services
- **Testing**: JUnit 5 and Mockito 5

### Frontend

- **Framework**: React
- **UI Library**: DaisyUI
- **Testing**: Vitest & JSDOM

## Current Status
- A web app is up and running! Go to [Tailor.ai here](https://tailor-ai.netlify.app/).

## Future Ideas

- A job description scraping microservice to further tailor resumes to job-specific descriptions, utilizing Flask and Beautiful Soup.
  - use [Fresh LinkedIn Profile Data](https://rapidapi.com/freshdata-freshdata-default/api/fresh-linkedin-profile-data) from RapidAPI, in the meantime. 

## Note

This repository is not intended for independent setup or deployment. The application is configured to work with specific AWS and Heroku resources, including a Heroku hosted database, Heroku hosted backend server, and an S3 bucket.

## Contact

For any questions or feedback, feel free to reach out to the development team at [tailor.ai.com@gmail.com](mailto\:tailor.ai.com@gmail.com).


