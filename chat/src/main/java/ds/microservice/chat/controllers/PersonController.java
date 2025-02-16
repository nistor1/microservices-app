package ds.microservice.chat.controllers;

import ds.microservice.chat.dtos.PersonDTO;
import ds.microservice.chat.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping(value = "/person")
public class PersonController {

    private final PersonService personService;


    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping()
    public ResponseEntity<UUID> insertPerson(@Valid @RequestBody PersonDTO personDTO) {
        UUID personID = personService.insertPerson(personDTO);
        return new ResponseEntity<>(personID, HttpStatus.CREATED);
    }

    @GetMapping("/mock")
    public ResponseEntity<PersonDTO> getMockPerson() {
        PersonDTO mockPerson = new PersonDTO();
        mockPerson.setPersonId(UUID.randomUUID());
        mockPerson.setPersonName("John Doe");
        return new ResponseEntity<>(mockPerson, HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<UUID> updatePerson (@Valid @RequestBody PersonDTO personDTO) {
        UUID personID = personService.updatePerson(personDTO);
        return new ResponseEntity<>(personID, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UUID> deletePersonById (@PathVariable("id") UUID personId) {
        personService.deletePersonById(personId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<UUID> deleteAllPersons () {
        personService.deleteAllPersons();
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
