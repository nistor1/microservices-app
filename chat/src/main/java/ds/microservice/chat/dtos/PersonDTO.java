package ds.microservice.chat.dtos;

import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

public class PersonDTO extends RepresentationModel<PersonDTO> {

    private UUID personId;

    private String personName;

    public PersonDTO() {
    }

    public PersonDTO(UUID id, String name) {
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
