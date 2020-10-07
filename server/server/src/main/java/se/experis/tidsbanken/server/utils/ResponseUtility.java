package se.experis.tidsbanken.server.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import se.experis.tidsbanken.server.models.CommonResponse;
import se.experis.tidsbanken.server.models.User;

import javax.validation.ConstraintViolation;
import java.util.Set;

@Component
public class ResponseUtility {

    public ResponseEntity<CommonResponse> unauthorized() {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Not Authorized");
    }

    public ResponseEntity<CommonResponse> forbidden(String message) {
        return buildResponse(HttpStatus.FORBIDDEN, "Forbidden. " + message);
    }

    public ResponseEntity<CommonResponse> errorMessage(String process) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "Something went wrong on the server when trying to " + process);
    }

    public ResponseEntity<CommonResponse> notFound(String message) {
        return buildResponse(HttpStatus.NOT_FOUND, message);
    }

    public ResponseEntity<CommonResponse> badRequest(String message) {
        return buildResponse(HttpStatus.BAD_REQUEST, message);
    }

    public ResponseEntity<CommonResponse> superBadRequest(Set<ConstraintViolation<Object>> violations) {
        return buildResponse(HttpStatus.BAD_REQUEST,
                violations.stream().map(ConstraintViolation::getMessage).reduce((a, b) -> a + ", " + b + ", ").get());
    }

    public ResponseEntity<CommonResponse> ok(String message, Object data) {
        return ResponseEntity.ok().body(new CommonResponse().message(message).data(data));
    }

    public ResponseEntity<CommonResponse> created(String message, Object data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponse().message(message).data(data));
    }

    public ResponseEntity<CommonResponse> tooManyRequests() {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(new CommonResponse().message("Too Many Failed Attempts"));
    }

    private ResponseEntity<CommonResponse> buildResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new CommonResponse().message(message));
    }
}
