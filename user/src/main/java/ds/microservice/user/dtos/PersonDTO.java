package ds.microservice.user.dtos;

import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;
import java.util.UUID;

public class PersonDTO extends RepresentationModel<PersonDTO> {

    private UUID personId;

    private String personName;

    private String email;

    private String password;

    private int age;

    private String role;


    public PersonDTO() {
    }

    public PersonDTO(UUID personId, String personName, int age, String role, String email, String password) {
        this.personId = personId;
        this.personName = personName;
        this.age = age;
        this.role = role;
        this.email = email;
        this.password = password;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonDTO personDTO = (PersonDTO) o;
        return age == personDTO.age &&
                Objects.equals(personName, personDTO.personName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personName, age);
    }
}
