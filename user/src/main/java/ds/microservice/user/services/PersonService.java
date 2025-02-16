package ds.microservice.user.services;

import ds.microservice.user.dtos.PersonDTO;
import ds.microservice.user.dtos.PersonDetailsDTO;

import java.util.List;
import java.util.UUID;

public interface PersonService {

     List<PersonDTO> findAllPersons();

     PersonDetailsDTO findPersonById(UUID id);

     UUID insertPerson(PersonDetailsDTO personDTO);

     UUID updatePerson(PersonDetailsDTO personDetailsDTO);

     UUID getPersonIdByEmail(String email);

     void deletePersonById(UUID id);

     void deleteAllPersons();
}
