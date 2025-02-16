package sd.simulator;

import java.time.LocalDateTime;
import java.util.UUID;

public class Measurement {
    private LocalDateTime timestamp;
    private UUID deviceId;
    private double hourlyConsumption;

    public Measurement(LocalDateTime timestamp, UUID deviceId, double hourlyConsumption) {
        this.timestamp = timestamp;
        this.deviceId = deviceId;
        this.hourlyConsumption = hourlyConsumption;
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

    @Override
    public String toString() {
        return "Measurement{" +
                "timestamp='" + timestamp + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", measurementValue=" + hourlyConsumption +
                '}';
    }
}
