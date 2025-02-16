FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/measurement-0.0.1-SNAPSHOT.jar /app/measurement.jar
ENTRYPOINT ["java", "-jar", "/app/measurement.jar"]
EXPOSE 8082