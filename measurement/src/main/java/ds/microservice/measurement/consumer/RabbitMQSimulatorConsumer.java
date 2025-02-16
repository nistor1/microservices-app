package ds.microservice.measurement.consumer;

import ds.microservice.measurement.dto.MeasurementDto;
import ds.microservice.measurement.repository.MeasurementRepository;
import ds.microservice.measurement.service.DeviceService;
import ds.microservice.measurement.service.WebSocketMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class RabbitMQSimulatorConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQSimulatorConsumer.class);

    private MeasurementRepository measurementRepository;

    private final ConcurrentMap<UUID, List<Double>> measurementsBuffer = new ConcurrentHashMap<>();

    private WebSocketMessageSender webSocketMessageSender;

    private DeviceService deviceService;

    @Autowired
    public RabbitMQSimulatorConsumer(MeasurementRepository measurementRepository,
                                     WebSocketMessageSender webSocketMessageSender,
                                     DeviceService deviceService) {
        this.measurementRepository = measurementRepository;
        this.webSocketMessageSender = webSocketMessageSender;
        this.deviceService = deviceService;
    }


    @RabbitListener(queues = {"${rabbitmq.queue.simulator.name}"})
    public void consumeJsonMessage(MeasurementDto measurement) {
        LOGGER.info(String.format("Received json message from simulator -> %s, %s, %s, %s", measurement.getMeasurementId(), measurement.getTimestamp(), measurement.getHourlyConsumption(), measurement.getDeviceId()));

        UUID deviceId = measurement.getDeviceId();
        double currentConsumption = measurement.getHourlyConsumption();

        measurementsBuffer.putIfAbsent(deviceId, new ArrayList<>());
        measurementsBuffer.get(deviceId).add(currentConsumption);


        if (measurementsBuffer.get(deviceId).size() == 6) {
            processAndSaveHourlyAverage(deviceId, measurement.getTimestamp());
            measurementsBuffer.get(deviceId).clear();
        }

    }

    private void processAndSaveHourlyAverage(UUID deviceId, LocalDateTime timestamp) {
        List<Double> hourlyConsumptions = measurementsBuffer.get(deviceId);
        if (hourlyConsumptions.isEmpty()) {
            LOGGER.warn(String.format("No measurements found for device %s", deviceId));
            return;
        }

        double firstConsumption = hourlyConsumptions.get(0);

        List<Double> adjustedConsumptions = new ArrayList<>();
        for (Double consumption : hourlyConsumptions) {
            adjustedConsumptions.add(consumption - firstConsumption);
        }

        double averageConsumption = adjustedConsumptions.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        double maxHourlyConsumption = deviceService.findDeviceById(deviceId).getMaximumHourlyEnergyConsumption();
        if (maxHourlyConsumption < averageConsumption) {
            try {
                String message = String.format("Warning: Device %s has max consumption (%.2f) smaller than average (%.2f)",
                        deviceId, maxHourlyConsumption, averageConsumption);
                webSocketMessageSender.sendMessageToClients(deviceId.toString(), message);
                LOGGER.info("WebSocket message sent: {}", message);
            } catch (IOException e) {
                LOGGER.error("Error sending WebSocket message", e);
            }
        }

        MeasurementDto avgMeasurement = new MeasurementDto();
        avgMeasurement.setDeviceId(deviceId);
        avgMeasurement.setTimestamp(timestamp);
        avgMeasurement.setHourlyConsumption(averageConsumption);

        LOGGER.info(String.format("Saving average measurement -> Device: %s, Avg Consumption: %.2f",
                deviceId, averageConsumption));

        measurementRepository.save(avgMeasurement);
    }

    public MeasurementRepository getMeasurementRepository() {
        return measurementRepository;
    }

    public void setMeasurementRepository(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }
}
