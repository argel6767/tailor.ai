# Runtime
FROM eclipse-temurin:17-jdk-alpine

#Working directory
WORKDIR /app

#copy jar file
COPY target/tailor.ai-0.0.1-SNAPSHOT.jar app.jar

#Exposing port
ENTRYPOINT 8080

#Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]