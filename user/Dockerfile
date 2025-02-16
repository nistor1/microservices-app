FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/user-0.0.1-SNAPSHOT.jar /app/user.jar
ENTRYPOINT ["java", "-jar", "/app/user.jar"]
EXPOSE 8080