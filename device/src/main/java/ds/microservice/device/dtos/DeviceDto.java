package ds.microservice.device.dtos;

import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

public class DeviceDto extends RepresentationModel<DeviceDto> {

    private UUID deviceId;

    @NotNull
    private String personName;

    @NotNull
    private UUID personId;

    @NotNull
    private String description;

    @NotNull
    private String address;

    @NotNull
    private Double maximumHourlyEnergyConsumption;

    public DeviceDto(UUID deviceId, UUID personId, String personName, String description, String address, Double maximumHourlyEnergyConsumption) {
        this.deviceId = deviceId;
        this.personName = personName;
        this.personId = personId;
        this.description = description;
        this.address = address;
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

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public UUID getPersonId() {
        return personId;
    }

    public void setPersonId(UUID personId) {
        this.personId = personId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getMaximumHourlyEnergyConsumption() {
        return maximumHourlyEnergyConsumption;
    }

    public void setMaximumHourlyEnergyConsumption(Double maximumHourlyEnergyConsumption) {
        this.maximumHourlyEnergyConsumption = maximumHourlyEnergyConsumption;
    }
}
