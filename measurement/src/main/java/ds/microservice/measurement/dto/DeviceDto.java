package ds.microservice.measurement.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class DeviceDto {

    @Id
    @Column(name = "device_id", nullable = false)
    private UUID deviceId;

    @Column(name = "maximum_hourly_energy_consumption", nullable = false)
    private Double maximumHourlyEnergyConsumption;

    public DeviceDto(UUID deviceId, Double maximumHourlyEnergyConsumption) {
        this.deviceId = deviceId;
        this.maximumHourlyEnergyConsumption = maximumHourlyEnergyConsumption;
    }

    public DeviceDto() {
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public Double getMaximumHourlyEnergyConsumption() {
        return maximumHourlyEnergyConsumption;
    }

    public void setMaximumHourlyEnergyConsumption(Double maximumHourlyEnergyConsumption) {
        this.maximumHourlyEnergyConsumption = maximumHourlyEnergyConsumption;
    }
}

