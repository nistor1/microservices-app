package ds.microservice.measurement.consumer;

import ds.microservice.measurement.dto.DeviceDto;
import ds.microservice.measurement.service.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQDeviceConsumer {

    private DeviceService deviceService;

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQDeviceConsumer.class);

    @Autowired
    public RabbitMQDeviceConsumer(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @RabbitListener(queues = {"${rabbitmq.queue.simulator.insert.name}"})
    public void consumeInsertMessage(DeviceDto deviceDto) {
        LOGGER.info(String.format("Received Insert message from simulator -> %s, %s", deviceDto.getDeviceId(), deviceDto.getMaximumHourlyEnergyConsumption()));

        deviceService.insertDevice(deviceDto);
    }

    @RabbitListener(queues = {"${rabbitmq.queue.simulator.update.name}"})
    public void consumeUpdateMessage(DeviceDto deviceDto) {
        LOGGER.info(String.format("Received Update message from simulator -> %s, %s", deviceDto.getDeviceId(), deviceDto.getMaximumHourlyEnergyConsumption()));

        deviceService.updateDevice(deviceDto);
    }

    @RabbitListener(queues = {"${rabbitmq.queue.device.delete.name}"})
    public void consumeDeleteMessage(DeviceDto deviceDto) {
        LOGGER.info(String.format("Received Delete message from simulator -> %s, %s", deviceDto.getDeviceId(), deviceDto.getMaximumHourlyEnergyConsumption()));

        deviceService.deleteDeviceById(deviceDto.getDeviceId());
    }

    public DeviceService getDeviceService() {
        return deviceService;
    }

    public void setDeviceService(DeviceService deviceService) {
        this.deviceService = deviceService;
    }
}
