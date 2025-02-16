package ds.microservice.device.services.rabbitmq;

import ds.microservice.device.dtos.DeviceDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DeviceRabbitMQToMeasurements {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceRabbitMQToMeasurements.class);

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.device.delete.key}")
    private String deviceDeleteKey;

    @Value("${rabbitmq.routing.device.update.key}")
    private String deviceUpdateKey;

    @Value("${rabbitmq.routing.device.insert.key}")
    private String deviceInsertKey;

    private final RabbitTemplate rabbitTemplate;

    public DeviceRabbitMQToMeasurements(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendDeleteDevice(DeviceDto deviceDto) {
        LOGGER.info(String.format("Delete device sent -> %s", deviceDto.getDeviceId()));

        rabbitTemplate.convertAndSend(exchange, deviceDeleteKey, deviceDto, message -> {
            message.getMessageProperties().setContentType("application/json");
            return message;
        });
    }

    public void sendUpdateDevice(DeviceDto deviceDto) {
        LOGGER.info(String.format("Update device sent -> %s", deviceDto.getDeviceId()));

        rabbitTemplate.convertAndSend(exchange, deviceUpdateKey, deviceDto, message -> {
            message.getMessageProperties().setContentType("application/json");
            return message;
        });
    }

    public void sendInsertDevice(DeviceDto deviceDto) {
        LOGGER.info(String.format("Insert device sent -> %s", deviceDto.getDeviceId()));

        rabbitTemplate.convertAndSend(exchange, deviceInsertKey, deviceDto, message -> {
            message.getMessageProperties().setContentType("application/json");
            return message;
        });
    }

}
