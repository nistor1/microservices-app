FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/chat-0.0.1-SNAPSHOT.jar /app/chat.jar
ENTRYPOINT ["java", "-jar", "/app/chat.jar"]
EXPOSE 8083