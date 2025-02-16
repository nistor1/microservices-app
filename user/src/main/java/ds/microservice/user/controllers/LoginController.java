package ds.microservice.user.controllers;

import ds.microservice.user.dtos.LoginDTO;
import ds.microservice.user.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping(value = "/login")
public class LoginController {

    private LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping()
    public ResponseEntity<LoginDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        LoginDTO logged = loginService.login(loginDTO.getEmail(), loginDTO.getPassword());

        if(logged.isLogged()) {
            return new ResponseEntity<>(logged, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(logged, HttpStatus.BAD_REQUEST);
        }
    }



}
