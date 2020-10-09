package se.experis.tidsbanken.server.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import se.experis.tidsbanken.server.models.CommonResponse;
import se.experis.tidsbanken.server.models.User;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * Builds Http Request responses
 */
@Component
public class ResponseUtility {

    /**
     * Builds a response for unauthorized requests
     * @return ResponseEntity with 401 status
     */
    public ResponseEntity<CommonResponse> unauthorized() {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Not Authorized");
    }

    /**
     * Builds a response for forbidden requests
     * @param message message for the forbidden attempt
     * @return ResponseEntity with status 403 and a message
     */
    public ResponseEntity<CommonResponse> forbidden(String message) {
        return buildResponse(HttpStatus.FORBIDDEN, "Forbidden. " + message);
    }

    /**
     * Builds a server error response
     * @param process the server error
     * @return ResponseEntity with status of 500 and a message
     */
    public ResponseEntity<CommonResponse> errorMessage(String process) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "Something went wrong on the server when trying to " + process);
    }

    /**
     * Builds a 404 Response
     * @param message the resource not found
     * @return ResponseEntity with status of 404 and a message
     */
    public ResponseEntity<CommonResponse> notFound(String message) {
        return buildResponse(HttpStatus.NOT_FOUND, message);
    }

    /**
     * Returns a 400 response with a message
     * @param message
     * @return
     */
    public ResponseEntity<CommonResponse> badRequest(String message) {
        return buildResponse(HttpStatus.BAD_REQUEST, message);
    }

    /**
     * Returns a 400 response with validation violations
     * @param violations set of violations
     * @return ResponseEntity with all validation violations
     */
    public ResponseEntity<CommonResponse> superBadRequest(Set<ConstraintViolation<Object>> violations) {
        return buildResponse(HttpStatus.BAD_REQUEST,
                violations.stream().map(ConstraintViolation::getMessage).reduce((a, b) -> a + ", " + b + ", ").get());
    }

    /**
     * Builds a 200 response
     * @param message String of the request
     * @param data Any data to return
     * @return ResponseEntity with a message and data
     */
    public ResponseEntity<CommonResponse> ok(String message, Object data) {
        return ResponseEntity.ok().body(new CommonResponse().message(message).data(data));
    }

    /**
     * Returns a 201 response with message and data
     * @param message String
     * @param data Any data that was created
     * @return ResponseEntity with a 201 status, message and data
     */
    public ResponseEntity<CommonResponse> created(String message, Object data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponse().message(message).data(data));
    }

    /**
     * Builds 419 response
     * @return ResponseEntity with 419 status
     */
    public ResponseEntity<CommonResponse> tooManyRequests() {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(new CommonResponse().message("Too Many Failed Attempts"));
    }

    /**
     * Builder for responses
     * @param status the status of the response
     * @param message message to send with the response
     * @return ResponseEntity
     */
    private ResponseEntity<CommonResponse> buildResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new CommonResponse().message(message));
    }
}
