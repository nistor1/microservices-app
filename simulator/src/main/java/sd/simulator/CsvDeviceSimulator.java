package sd.simulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CsvDeviceSimulator {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvDeviceSimulator.class);

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    @Value("${measurement.device.1.id}")
    private UUID deviceId;

    @Value("${measurement.csv.path}")
    private String csvFilePath;

    private final RabbitTemplate rabbitTemplate;


    private BufferedReader bufferedReader;
    private boolean isFileFinished = false;

    public CsvDeviceSimulator(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Scheduled(fixedRate = 10000)
    public void sendMeasurement() {
        if (isFileFinished) {
            LOGGER.info("End of file reached. Stopping scheduled task.");
            return;
        }

        try {
            if (bufferedReader == null) {
                Resource resource = new ClassPathResource(csvFilePath);
                if (!resource.exists()) {
                    LOGGER.error("File not found in classpath: {}", csvFilePath);
                    isFileFinished = true;
                    return;
                }
                bufferedReader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            }

            String line = bufferedReader.readLine();
            if (line != null) {
                double measurementValue = Double.parseDouble(line);
                Measurement measurement = new Measurement(LocalDateTime.now(), deviceId, measurementValue);
                rabbitTemplate.convertAndSend(exchange, routingKey, measurement);
                LOGGER.info(String.format("Simulator -> Message sent: %s ...", measurement));
            } else {
                isFileFinished = true;
                bufferedReader.close();
                LOGGER.info("Finished reading file.");
            }
        } catch (IOException e) {
            LOGGER.error("Error reading CSV file", e);
        }
    }
}