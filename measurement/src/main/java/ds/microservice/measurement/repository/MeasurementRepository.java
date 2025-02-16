package ds.microservice.measurement.repository;

import ds.microservice.measurement.dto.MeasurementDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MeasurementRepository extends JpaRepository<MeasurementDto, UUID> {

    @Query("SELECT m FROM MeasurementDto m WHERE m.timestamp >= :startOfDay AND m.timestamp < :endOfDay AND m.deviceId = :deviceId")
    List<MeasurementDto> findByDateAndDeviceId(LocalDateTime startOfDay, LocalDateTime endOfDay, UUID deviceId);
}