package ds.microservice.device.services;

import ds.microservice.device.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ds.microservice.device.dtos.DeviceDto;
import ds.microservice.device.dtos.builders.DeviceBuilder;
import ds.microservice.device.entities.Device;
import ds.microservice.device.entities.Person;
import ds.microservice.device.repositories.DeviceRepository;
import ds.microservice.device.repositories.PersonRepository;
import ds.microservice.device.services.rabbitmq.DeviceRabbitMQToMeasurements;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DeviceServiceImpl implements DeviceService{

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceServiceImpl.class);

    private DeviceRepository deviceRepository;

    private PersonRepository personRepository;

    private DeviceRabbitMQToMeasurements deviceRabbitMQToMeasurements;

    public DeviceServiceImpl() {
    }

    @Autowired
    public DeviceServiceImpl(DeviceRepository deviceRepository, PersonRepository personRepository, DeviceRabbitMQToMeasurements deviceRabbitMQToMeasurements) {
        this.deviceRepository = deviceRepository;
        this.personRepository = personRepository;
        this.deviceRabbitMQToMeasurements = deviceRabbitMQToMeasurements;
    }

    @Override
    @Transactional
    public List<DeviceDto> findAllDevices() {

        List<Device> deviceList = deviceRepository.findAll();
        return deviceList.stream()
                .map(DeviceBuilder::toDeviceDto)
                .toList();
    }

    @Override
    public DeviceDto findDeviceById(UUID id) {

        Optional<Device> deviceOptional = deviceRepository.findById(id);
        if (deviceOptional.isEmpty()) {
            LOGGER.error("Device with id {} was not found in db", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }
        return DeviceBuilder.toDeviceDto(deviceOptional.get());
    }

    @Override
    public UUID insertDevice(DeviceDto deviceDto) {
        Device device = DeviceBuilder.toEntity(deviceDto);
        device = deviceRepository.save(device);
        deviceDto.setDeviceId(device.getDeviceId());
        deviceRabbitMQToMeasurements.sendInsertDevice(deviceDto);
        LOGGER.debug("Device with id {} was inserted in db", device.getDeviceId());
        return device.getDeviceId();
    }

    @Override
    public UUID updateDevice(DeviceDto deviceDto) {
        Optional<Device> optionalDevice = deviceRepository.findById(deviceDto.getDeviceId());
        if(optionalDevice.isEmpty()) {
            LOGGER.error("Device with id {} was not found in db", deviceDto.getDeviceId());
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + deviceDto.getDeviceId());
        }
        Device device = optionalDevice.get();
        device.setAddress(deviceDto.getAddress());
        device.setDescription(deviceDto.getDescription());
        device.setMaximumHourlyEnergyConsumption(deviceDto.getMaximumHourlyEnergyConsumption());
        device.setPerson(new Person(deviceDto.getPersonId(), deviceDto.getPersonName()));

        device = deviceRepository.save(device);
        deviceRabbitMQToMeasurements.sendUpdateDevice(deviceDto);
        LOGGER.debug("Device with id {} was updated in db", device.getDeviceId());

        return device.getDeviceId();
    }

    @Override
    public void deleteDeviceById(UUID id) {
        Optional<Device> deviceOptional = deviceRepository.findById(id);
        if(deviceOptional.isEmpty()) {
            LOGGER.error("Device with id {} was not found in db", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }

        deviceRepository.deleteById(id);
        DeviceDto deviceDto = new DeviceDto();
        deviceDto.setDeviceId(id);
        deviceRabbitMQToMeasurements.sendDeleteDevice(deviceDto);

    }

    @Override
    public void deleteAllDevices() {
        deviceRepository.deleteAll();
    }

    @Override
    public List<DeviceDto> findDevicesByPersonId(UUID personId) {
        List<Device> deviceList = deviceRepository.findDevicesByPersonPersonId(personId);
        return deviceList.stream()
                .map(DeviceBuilder::toDeviceDto)
                .toList();
    }

    public DeviceRepository getDeviceRepository() {
        return deviceRepository;
    }

    public void setDeviceRepository(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public PersonRepository getPersonRepository() {
        return personRepository;
    }

    public void setPersonRepository(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
}
