package ds.microservice.measurement.publisher;

import ds.microservice.measurement.dto.MeasurementDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQJsonProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQJsonProducer.class);


    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.json.key}")
    private String routingJsonKey;

    private RabbitTemplate rabbitTemplate;

    public RabbitMQJsonProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendJsonMessage(MeasurementDto measurement) {

        LOGGER.info(String.format("Json message sent -> %s", measurement.getMeasurementId()));

        rabbitTemplate.convertAndSend(exchange, routingJsonKey, measurement, message -> {
            message.getMessageProperties().setContentType("application/json");
            return message;
        });

    }
}
