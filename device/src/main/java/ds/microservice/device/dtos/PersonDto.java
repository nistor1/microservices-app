package ds.microservice.device.dtos;

import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

public class PersonDto extends RepresentationModel<PersonDto> {

    private UUID personId;

    private String personName;

    public PersonDto() {
    }

    public PersonDto(UUID id, String name) {
        this.personId = id;
        this.personName = name;
    }

    public UUID getPersonId() {
        return personId;
    }

    public void setPersonId(UUID personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }
}
