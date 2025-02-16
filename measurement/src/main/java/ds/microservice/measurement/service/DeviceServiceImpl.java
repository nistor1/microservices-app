package ds.microservice.measurement.service;

import ds.microservice.measurement.consumer.RabbitMQDeviceConsumer;
import ds.microservice.measurement.dto.DeviceDto;
import ds.microservice.measurement.repository.DeviceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class DeviceServiceImpl implements DeviceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceServiceImpl.class);

    private DeviceRepository deviceRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public DeviceDto findDeviceById(UUID id) {

        Optional<DeviceDto> deviceOptional = deviceRepository.findById(id);
        if (deviceOptional.isEmpty()) {
            LOGGER.error("Device with id {} was not found in db", id);
            throw new EntityNotFoundException(DeviceDto.class.getSimpleName() + " with id: " + id);
        }
        return deviceOptional.get();
    }

    @Override
    public UUID insertDevice(DeviceDto deviceDto) {

        deviceRepository.save(deviceDto);
        LOGGER.debug("Device with id {} was inserted in db", deviceDto.getDeviceId());
        return deviceDto.getDeviceId();
    }

    @Override
    public UUID updateDevice(DeviceDto deviceDto) {

        Optional<DeviceDto> optionalDevice = deviceRepository.findById(deviceDto.getDeviceId());
        if(optionalDevice.isEmpty()) {
            LOGGER.error("Device with id {} was not found in db", deviceDto.getDeviceId());
            throw new EntityNotFoundException(DeviceDto.class.getSimpleName() + " with id: " + deviceDto.getDeviceId());
        }
        DeviceDto device = optionalDevice.get();
        device.setMaximumHourlyEnergyConsumption(deviceDto.getMaximumHourlyEnergyConsumption());
        device = deviceRepository.save(device);
        LOGGER.debug("Device with id {} was updated in db", device.getDeviceId());

        return device.getDeviceId();
    }

    @Override
    public void deleteDeviceById(UUID id) {

        Optional<DeviceDto> deviceOptional = deviceRepository.findById(id);
        if(deviceOptional.isEmpty()) {
            LOGGER.error("Device with id {} was not found in db", id);
            throw new EntityNotFoundException(DeviceDto.class.getSimpleName() + " with id: " + id);
        }

        deviceRepository.deleteById(id);
    }

    public DeviceRepository getDeviceRepository() {
        return deviceRepository;
    }

    public void setDeviceRepository(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

}
