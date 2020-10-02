package se.experis.tidsbanken.server.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.experis.tidsbanken.server.models.*;
import se.experis.tidsbanken.server.repositories.StatusRepository;
import se.experis.tidsbanken.server.services.AuthorizationService;
import se.experis.tidsbanken.server.utils.ResponseUtility;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
public class StatusController {

    @Autowired StatusRepository statusRepository;
    @Autowired AuthorizationService authService;
    @Autowired ResponseUtility responseUtility;
    private Logger logger = LoggerFactory.getLogger(CommentController.class);

    @GetMapping("/status")
    public ResponseEntity<CommonResponse> getStatus(HttpServletRequest request) {
        try{
            return responseUtility.ok("All statuses", statusRepository.findAll());
        } catch(Exception e) {
            logger.error(e.getMessage());
            return responseUtility.errorMessage("fetch status");
        }
    }

    @GetMapping("/status/{status_id}")
    public ResponseEntity<CommonResponse> getStatus(@PathVariable("status_id") Integer statusId,
                                                    HttpServletRequest request) {
        try {
            final Optional<Status> statusOp = statusRepository.findById(statusId);
            if(statusOp.isPresent())
                return responseUtility.ok("Status Found", statusOp.get());
            return responseUtility.notFound("Status Not Found");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return responseUtility.errorMessage("fetch status with id " + statusId);
        }
    }
}
