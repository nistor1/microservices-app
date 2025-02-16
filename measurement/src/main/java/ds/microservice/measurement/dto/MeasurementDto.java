package ds.microservice.measurement.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class MeasurementDto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID measurementId;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "device_id", nullable = false)
    private UUID deviceId;

    @Column(name = "hourly_consumption", nullable = false)
    private double hourlyConsumption;

    public MeasurementDto() {
    }

    public MeasurementDto(LocalDateTime timestamp, UUID deviceId, double hourlyConsumption) {
        this.timestamp = timestamp;
        this.deviceId = deviceId;
        this.hourlyConsumption = hourlyConsumption;
    }

    public UUID getMeasurementId() {
        return measurementId;
    }

    public void setMeasurementId(UUID measurementId) {
        this.measurementId = measurementId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public double getHourlyConsumption() {
        return hourlyConsumption;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public void setHourlyConsumption(double hourlyConsumption) {
        this.hourlyConsumption = hourlyConsumption;
    }
}
