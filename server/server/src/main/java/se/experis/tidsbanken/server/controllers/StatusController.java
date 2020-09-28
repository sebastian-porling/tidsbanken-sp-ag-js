package se.experis.tidsbanken.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import se.experis.tidsbanken.server.models.CommonResponse;
import se.experis.tidsbanken.server.models.Status;
import se.experis.tidsbanken.server.repositories.StatusRepository;
import se.experis.tidsbanken.server.services.AuthorizationService;
import se.experis.tidsbanken.server.utils.ResponseUtility;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class StatusController {

    @Autowired StatusRepository statusRepository;

    @Autowired AuthorizationService authService;

    @Autowired ResponseUtility responseUtility;

    @GetMapping("/status")
    public ResponseEntity<CommonResponse> getStatus(HttpServletRequest request) {
        if (!authService.isAuthorized(request)) return responseUtility.unauthorized();
        try{
            return responseUtility.ok("All statuses", statusRepository.findAll());
        } catch(Exception e) { return responseUtility.errorMessage(); }
    }

    @GetMapping("/status/{status_id}")
    public ResponseEntity<CommonResponse> getStatus(@PathVariable("status_id") Integer statusId,
                                                    HttpServletRequest request) {
        if (!authService.isAuthorized(request)) return responseUtility.unauthorized();
        try {
            final Optional<Status> statusOp = statusRepository.findById(statusId);
            if(statusOp.isPresent())
                return responseUtility.ok("Status Found", statusOp.get());
            return responseUtility.notFound("Status Not Found");
        } catch (Exception e) { return responseUtility.errorMessage(); }
    }
}
