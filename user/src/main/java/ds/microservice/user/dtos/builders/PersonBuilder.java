package ds.microservice.user.dtos.builders;

import ds.microservice.user.dtos.PersonDTO;
import ds.microservice.user.dtos.PersonDetailsDTO;
import ds.microservice.user.entities.Person;

public class PersonBuilder {

    private PersonBuilder() {
    }

    public static PersonDTO toPersonDTO(Person person) {
        return new PersonDTO(person.getPersonId(), person.getPersonName(), person.getAge(), person.getRole(), person.getEmail(), person.getPassword());
    }

    public static PersonDetailsDTO toPersonDetailsDTO(Person person) {
        return new PersonDetailsDTO(person.getPersonId(), person.getPersonName(), person.getAddress(), person.getAge(), person.getEmail(), person.getPassword());
    }

    public static Person toEntity(PersonDetailsDTO personDetailsDTO) {
        return new Person(personDetailsDTO.getPersonName(),
                personDetailsDTO.getAddress(),
                personDetailsDTO.getAge(),
                personDetailsDTO.getRole(),
                personDetailsDTO.getEmail(),
                personDetailsDTO.getPassword());

    }
}
