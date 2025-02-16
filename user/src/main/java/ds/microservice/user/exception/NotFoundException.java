package ds.microservice.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
public class NotFoundException extends RuntimeException {

    private final String message;

    public NotFoundException(String message) {
        this.message = message;
    }

}