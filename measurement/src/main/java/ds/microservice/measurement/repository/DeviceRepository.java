package ds.microservice.measurement.repository;

import ds.microservice.measurement.dto.DeviceDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeviceRepository  extends JpaRepository<DeviceDto, UUID> {
}