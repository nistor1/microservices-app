FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/device-0.0.1-SNAPSHOT.jar /app/device.jar
ENTRYPOINT ["java", "-jar", "/app/device.jar"]
EXPOSE 8081