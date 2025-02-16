package ds.microservice.chat.services;

import ds.microservice.chat.dtos.PersonDTO;
import ds.microservice.chat.entities.Person;

import java.util.UUID;

public interface PersonService {

    Person findPersonById(UUID id);

    UUID insertPerson(PersonDTO personDto);

    UUID updatePerson(PersonDTO personDto);

    void deletePersonById(UUID id);

    void deleteAllPersons();


}
