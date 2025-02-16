package ds.microservice.device.services;

import ds.microservice.device.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ds.microservice.device.dtos.PersonDto;
import ds.microservice.device.dtos.builders.PersonBuilder;
import ds.microservice.device.entities.Person;
import ds.microservice.device.repositories.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PersonServiceImpl implements PersonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonServiceImpl.class);

    private PersonRepository personRepository;

    public PersonServiceImpl() {
    }

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UUID insertPerson(PersonDto personDto) {

        Person person = PersonBuilder.toEntity(personDto);
        person = personRepository.save(person);
        LOGGER.debug("Person with id {} was inserted in db", person.getPersonId());
        return person.getPersonId();
    }

    @Override
    public UUID updatePerson(PersonDto personDto) {

        Optional<Person> optionalPerson = personRepository.findById(personDto.getPersonId());
        if(optionalPerson.isEmpty()) {
            LOGGER.error("Person with id {} was not found in db", personDto.getPersonName());
            throw new ResourceNotFoundException(Person.class.getSimpleName() + " with id: " + personDto.getPersonId());
        }
        Person person = optionalPerson.get();
        person.setPersonName(personDto.getPersonName());

        person = personRepository.save(person);
        LOGGER.debug("Person with id {} was updated in db", person.getPersonId());
        return person.getPersonId();
    }

    @Override
    public void deletePersonById(UUID id) {

        Optional<Person> optionalPerson = personRepository.findById(id);
        if(optionalPerson.isEmpty()) {
            LOGGER.error("Person with id {} was not found in db", id);
            throw new ResourceNotFoundException(Person.class.getSimpleName() + " with id: " + id);
        }

        personRepository.deleteById(id);
    }

    @Override
    public void deleteAllPersons() {
        personRepository.deleteAll();
    }

    public PersonRepository getPersonRepository() {
        return personRepository;
    }

    public void setPersonRepository(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
}
