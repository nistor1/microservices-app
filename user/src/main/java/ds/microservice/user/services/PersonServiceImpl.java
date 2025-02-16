package ds.microservice.user.services;

import ds.microservice.user.constants.Constants;
import ds.microservice.user.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ds.microservice.user.dtos.PersonDTO;
import ds.microservice.user.dtos.PersonDetailsDTO;
import ds.microservice.user.dtos.builders.PersonBuilder;
import ds.microservice.user.entities.Person;
import ds.microservice.user.repositories.PersonRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PersonServiceImpl implements PersonService{

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonServiceImpl.class);

    private final PersonRepository personRepository;

    private final RestTemplate restTemplate;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository, PasswordEncoder passwordEncoder, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;

    }

    @Override
    @Transactional
    public List<PersonDTO> findAllPersons() {
        List<Person> personList = personRepository.findAll();
        return personList.stream()
                .map(PersonBuilder::toPersonDTO)
                .toList();
    }

    @Override
    @Transactional
    public PersonDetailsDTO findPersonById(UUID id) {
        Optional<Person> personOptional = personRepository.findById(id);
        if (personOptional.isEmpty()) {
            LOGGER.error("Person with id {} was not found in db", id);
            throw new ResourceNotFoundException(Person.class.getSimpleName() + " with id: " + id);
        }
        return PersonBuilder.toPersonDetailsDTO(personOptional.get());
    }

    @Override
    @Transactional
    public UUID insertPerson(PersonDetailsDTO personDTO) {
        Person person = PersonBuilder.toEntity(personDTO);

        person.setPassword(passwordEncoder.encode(personDTO.getPassword()));

        // Adding authentication token to the header before calling the other service
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = (authentication != null && authentication.getCredentials() != null)
                ? authentication.getCredentials().toString() : null;

        if (token != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<Person> entity = new HttpEntity<>(person, headers);

            person = personRepository.save(person);

            // Sending post request with headers
            try {
                ResponseEntity<String> response = restTemplate.exchange(
                        Constants.URL_DEVICE_MICROSERVICE, HttpMethod.POST, entity, String.class);

                // Log the response status and body
                LOGGER.debug("Response from external service: {}", response.getStatusCode());
                LOGGER.debug("Response body: {}", response.getBody());

                if (response.getStatusCode().is2xxSuccessful()) {
                    LOGGER.debug("Person with id {} was successfully inserted in the device microservice", person.getPersonId());
                } else {
                    LOGGER.error("External service returned an error: {}", response.getStatusCode());
                    personRepository.deleteById(person.getPersonId());
                    return null;
                }

            } catch (HttpClientErrorException | HttpServerErrorException e) {
                personRepository.deleteById(person.getPersonId());
                LOGGER.error("Error calling external service: {}", e.getMessage());
                return null;
            } catch (Exception e) {
                personRepository.deleteById(person.getPersonId());
                LOGGER.error("Unexpected error occurred: {}", e.getMessage());
                return null;
            }
            LOGGER.debug("Person with id {} was inserted in db", person.getPersonId());
        }

        return person.getPersonId();
    }

    @Override
    @Transactional
    public UUID updatePerson(PersonDetailsDTO personDetailsDTO) {
        Optional<Person> optionalPerson = personRepository.findById(personDetailsDTO.getPersonId());
        if(optionalPerson.isEmpty()) {
            LOGGER.error("Person with id {} was not found in db", personDetailsDTO.getPersonId());
            throw new ResourceNotFoundException(Person.class.getSimpleName() + " with id: " + personDetailsDTO.getPersonId());
        }
        Person person = optionalPerson.get();
        person.setAddress(personDetailsDTO.getAddress());
        person.setAge(personDetailsDTO.getAge());
        person.setPersonName(personDetailsDTO.getPersonName());
        person.setRole(personDetailsDTO.getRole());
        person.setEmail(personDetailsDTO.getEmail());

        person.setPassword(passwordEncoder.encode(personDetailsDTO.getPassword()));

        person = personRepository.save(person);

        // Adding authentication token to the header before calling the other service
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = (authentication != null && authentication.getCredentials() != null)
                ? authentication.getCredentials().toString() : null;

        if (token != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<Person> entity = new HttpEntity<>(person, headers);

            // Sending post request with headers
            try {
                restTemplate.exchange(Constants.URL_DEVICE_MICROSERVICE, HttpMethod.PUT, entity, String.class);
            } catch (HttpClientErrorException e) {
                personRepository.deleteById(person.getPersonId());
                LOGGER.error("Error calling external service: {}", e.getMessage());
                return null;
            }
        }


        LOGGER.debug("Person with id {} was updated in db", person.getPersonId());
        return person.getPersonId();
    }
    @Override
    @Transactional
    public void deletePersonById(UUID id) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        if(optionalPerson.isEmpty()) {
            LOGGER.error("Person with id {} was not found in db", id);
            throw new ResourceNotFoundException(Person.class.getSimpleName() + " with id: " + id);
        }

        personRepository.deleteById(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = (authentication != null && authentication.getCredentials() != null)
                ? authentication.getCredentials().toString() : null;

        if (token != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<UUID> entity = new HttpEntity<>(id, headers);

            // Sending post request with headers
            try {
                restTemplate.exchange(Constants.URL_DEVICE_MICROSERVICE + "/" + id, HttpMethod.DELETE, entity, String.class);
            } catch (HttpClientErrorException e) {
                LOGGER.error("Error calling external service: {}", e.getMessage());
            }
        }
        LOGGER.debug("Person with id {} was deleted from db", id);
    }
    @Override
    @Transactional
    public void deleteAllPersons() {
        personRepository.deleteAll();
        restTemplate.delete(Constants.URL_DEVICE_MICROSERVICE);
    }

    public PersonRepository getPersonRepository() {
        return personRepository;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    @Override
    public UUID getPersonIdByEmail(String email) {
        Optional<Person> personOptional = personRepository.findByEmail(email);
        if (personOptional.isEmpty()) {
            LOGGER.error("Person with email {} was not found in db", email);
            throw new ResourceNotFoundException(Person.class.getSimpleName() + " with email: " + email);
        }
        return personOptional.get().getPersonId();
    }
}
