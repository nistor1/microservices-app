package ds.microservice.measurement.service;

import ds.microservice.measurement.dto.DeviceDto;

import java.util.List;
import java.util.UUID;

public interface DeviceService {

    DeviceDto findDeviceById(UUID id);

    UUID insertDevice(DeviceDto deviceDto);

    UUID updateDevice(DeviceDto deviceDto);

    void deleteDeviceById(UUID id);

}
