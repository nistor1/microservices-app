package ds.microservice.chat.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.UUID;

@Entity
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private UUID personId;

    @Column(name = "personName", nullable = false)
    private String personName;

    public Person() {
    }

    public Person(UUID personId, String personName) {
        this.personId = personId;
        this.personName = personName;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String name) {
        this.personName = name;
    }

    public UUID getPersonId() {
        return personId;
    }

    public void setPersonId(UUID personId) {
        this.personId = personId;
    }
    
}
