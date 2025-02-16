package ds.microservice.device.dtos.builders;

import ds.microservice.device.dtos.DeviceDto;
import ds.microservice.device.entities.Device;
import ds.microservice.device.entities.Person;

public class DeviceBuilder {

    private DeviceBuilder() {
    }

    public static DeviceDto toDeviceDto(Device device) {
        return new DeviceDto(
                device.getDeviceId(),
                device.getPerson().getPersonId(),
                device.getPerson().getPersonName(),
                device.getAddress(),
                device.getDescription(),
                device.getMaximumHourlyEnergyConsumption()
        );
    }

    public static Device toEntity(DeviceDto deviceDto) {
        return new Device(
                deviceDto.getDeviceId(),
                deviceDto.getDescription(),
                deviceDto.getAddress(),
                deviceDto.getMaximumHourlyEnergyConsumption(),
                new Person(deviceDto.getPersonId(), deviceDto.getPersonName())
        );
    }

}
