package ds.microservice.user.services;

import ds.microservice.user.dtos.LoginDTO;
import ds.microservice.user.entities.Person;
import ds.microservice.user.repositories.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService{

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);

    private PersonRepository personRepository;

    @Autowired
    public LoginServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public LoginDTO login(String email, String password) {

        LoginDTO logged = new LoginDTO();
        Optional<Person> personOptional = getPersonRepository().findByEmail(email);

        if(personOptional.isEmpty()) {

            logged.setLogged(false);
            return logged;
        }

        Person personToLogIn = personOptional.get();

        if(personToLogIn.getPassword().equals(password)) {
            logged.setLogged(true);
            logged.setEmail(personToLogIn.getEmail());
            logged.setRole(personToLogIn.getRole());
            logged.setPersonId(personToLogIn.getPersonId());
            return logged;
        }

        logged.setLogged(false);
        return logged;
    }

    public PersonRepository getPersonRepository() {
        return personRepository;
    }

    public void setPersonRepository(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
}
