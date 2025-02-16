package ds.microservice.device.services;

import ds.microservice.device.dtos.DeviceDto;
import ds.microservice.device.dtos.PersonDto;

import java.util.UUID;

public interface PersonService {

    UUID insertPerson(PersonDto personDto);

    UUID updatePerson(PersonDto personDto);

    void deletePersonById(UUID id);

    void deleteAllPersons();


}
