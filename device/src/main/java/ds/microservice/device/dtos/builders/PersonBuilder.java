package ds.microservice.device.dtos.builders;


import ds.microservice.device.dtos.PersonDto;
import ds.microservice.device.entities.Person;

public class PersonBuilder {

    private PersonBuilder() {
    }

    public static PersonDto toPersonDTO(Person person) {
        return new PersonDto(person.getPersonId(), person.getPersonName());
    }

    public static Person toEntity(PersonDto personDto) {
        return new Person(
                personDto.getPersonId(),
                personDto.getPersonName()
                );
    }
}

