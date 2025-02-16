package ds.microservice.chat.dtos.builders;


import ds.microservice.chat.dtos.PersonDTO;
import ds.microservice.chat.entities.Person;

public class PersonBuilder {

    private PersonBuilder() {
    }

    public static PersonDTO toPersonDTO(Person person) {
        return new PersonDTO(person.getPersonId(), person.getPersonName());
    }

    public static Person toEntity(PersonDTO personDto) {
        return new Person(
                personDto.getPersonId(),
                personDto.getPersonName()
                );
    }
}

