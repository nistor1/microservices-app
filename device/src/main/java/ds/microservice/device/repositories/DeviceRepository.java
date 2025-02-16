package ds.microservice.device.repositories;

import ds.microservice.device.entities.Device;
import ds.microservice.device.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {

    /**
     * Example: JPA generate Query by Field
     */
    List<Device> findDevicesByPersonPersonId(UUID personId);
}
