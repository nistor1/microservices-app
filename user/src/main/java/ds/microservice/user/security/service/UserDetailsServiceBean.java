package ds.microservice.user.security.service;

import ds.microservice.user.entities.Person;
import ds.microservice.user.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class UserDetailsServiceBean implements UserDetailsService {

    private final PersonRepository personRepository;

    public UserDetailsServiceBean(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return personRepository
                .findByEmail(email)
                .map(this::getUserDetails)
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials!!!"));
    }

    private UserDetails getUserDetails(Person person) {
        return User.builder()
                .username(person.getEmail())
                .password(person.getPassword())
                .roles(person.getRole())
                .build();
    }

    public PersonRepository getPersonRepository() {
        return personRepository;
    }
}