package ds.microservice.measurement.controller;

import ds.microservice.measurement.dto.MeasurementDto;
import ds.microservice.measurement.publisher.RabbitMQJsonProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1")
public class MessageJsonController {

    private RabbitMQJsonProducer rabbitMQJsonProducer;

    public MessageJsonController(RabbitMQJsonProducer rabbitMQJsonProducer) {
        this.rabbitMQJsonProducer = rabbitMQJsonProducer;
    }

    @PostMapping("/publish")
    public ResponseEntity<String> sendJsonMessage(@RequestBody MeasurementDto measurement) {
        rabbitMQJsonProducer.sendJsonMessage(measurement);

        return ResponseEntity.ok("Json message sent to RabbitMQ ...");
    }
}
