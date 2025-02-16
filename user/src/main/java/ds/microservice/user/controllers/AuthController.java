package ds.microservice.user.controllers;

import ds.microservice.user.dtos.PersonDetailsDTO;
import ds.microservice.user.services.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final PersonService personService;

    @PostMapping("/login")
    public ResponseEntity<Void> login() {
        logger.info("Login request detected...");

        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody PersonDetailsDTO registerRequest) {
        log.info("Register request detected for email: {}", registerRequest.getEmail());

        personService.insertPerson(registerRequest);

        return ResponseEntity.ok("User registered successfully.");
    }

}