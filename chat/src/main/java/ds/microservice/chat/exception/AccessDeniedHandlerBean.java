package ds.microservice.chat.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

@Slf4j
public class AccessDeniedHandlerBean implements AccessDeniedHandler {

    private final Logger logger = LoggerFactory.getLogger(AccessDeniedHandlerBean.class);

    private final ObjectMapper objectMapper;

    public AccessDeniedHandlerBean(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final AccessDeniedException exception
    ) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());

        try {
            objectMapper.writeValue(response.getWriter(), exception.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
