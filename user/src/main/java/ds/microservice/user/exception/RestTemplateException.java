package ds.microservice.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class RestTemplateException extends RuntimeException {

    private final String message;

    public RestTemplateException(String message) {
        this.message = message;
    }
}
