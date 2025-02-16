package ds.microservice.user.dtos;

import ds.microservice.user.dtos.validators.annotation.AgeLimit;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Objects;
import java.util.UUID;

public class PersonDetailsDTO {

    private UUID personId;

    @NotNull
    private String personName;

    @NotNull
    private String address;

    @AgeLimit(limit = 18)
    private int age;

    @NotNull
    private String role;

    @NotNull
    private String email;

    @NotNull
    private String password;

    public PersonDetailsDTO() {
    }

    public PersonDetailsDTO(String personName, String address, int age) {
        this.personName = personName;
        this.address = address;
        this.age = age;
    }

    public PersonDetailsDTO(UUID personId, String personName, String address, int age, String email, String password) {
        this.personId = personId;
        this.personName = personName;
        this.address = address;
        this.age = age;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public @NotNull String getRole() {
        return role;
    }

    public void setRole(@NotNull String role) {
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
        PersonDetailsDTO that = (PersonDetailsDTO) o;
        return age == that.age &&
                Objects.equals(personName, that.personName) &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personName, address, age);
    }
}
