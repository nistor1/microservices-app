package ds.microservice.device.services;

import ds.microservice.device.dtos.DeviceDto;

import java.util.List;
import java.util.UUID;

public interface DeviceService {
        List<DeviceDto> findAllDevices();

        List<DeviceDto> findDevicesByPersonId(UUID personId);

        DeviceDto findDeviceById(UUID id);

        UUID insertDevice(DeviceDto deviceDto);

        UUID updateDevice(DeviceDto deviceDto);

        void deleteDeviceById(UUID id);

        void deleteAllDevices();

}
