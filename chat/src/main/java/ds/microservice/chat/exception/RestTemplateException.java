package ds.microservice.chat.exception;

import lombok.Getter;

@Getter
public class RestTemplateException extends RuntimeException {

    private final String message;

    public RestTemplateException(String message) {
        this.message = message;
    }
}
