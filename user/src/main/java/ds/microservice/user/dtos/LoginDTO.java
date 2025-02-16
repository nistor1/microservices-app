package ds.microservice.user.dtos;

import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

public class LoginDTO extends RepresentationModel<LoginDTO> {

    private boolean logged;

    private UUID personId;

    private String email;

    private String password;

    private String role;

    public LoginDTO() {}

    public LoginDTO(UUID personId,Boolean logged, String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.logged = logged;
        this.personId = personId;
    }

    public UUID getPersonId() {
        return personId;
    }

    public void setPersonId(UUID personId) {
        this.personId = personId;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }
}
