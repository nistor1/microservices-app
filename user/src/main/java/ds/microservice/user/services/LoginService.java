package ds.microservice.user.services;

import ds.microservice.user.dtos.LoginDTO;

public interface LoginService {

    public LoginDTO login(String email, String password);
}
