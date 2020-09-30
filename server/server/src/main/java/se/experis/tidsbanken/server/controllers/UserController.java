package se.experis.tidsbanken.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import se.experis.tidsbanken.server.models.*;
import se.experis.tidsbanken.server.repositories.*;
import se.experis.tidsbanken.server.services.AuthorizationService;
import se.experis.tidsbanken.server.socket.NotificationObserver;
import se.experis.tidsbanken.server.utils.ResponseUtility;

import javax.servlet.http.HttpServletRequest;
import javax.validation.*;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*", allowedHeaders = "*") 
public class UserController {

    @Autowired private UserRepository userRepository;

    @Autowired private VacationRequestRepository vacationRequestRepository;

    @Autowired private AuthorizationService authService;

    @Autowired private ResponseUtility responseUtility;

    @Autowired private NotificationObserver observer;

    @Autowired private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    @Autowired private Validator validator = factory.getValidator();

    @GetMapping("/user")
    public ResponseEntity<CommonResponse> getUser(HttpServletRequest request) {
        if(!authService.isAuthorized(request)) { return responseUtility.unauthorized(); }
        final HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/user/" + authService.currentUser(request).getId()));
        return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
    }

    @PostMapping("/user")
    public ResponseEntity<CommonResponse> createUser(@RequestBody User user,
                                                     HttpServletRequest request) {
        if (!authService.isAuthorizedAdmin(request)) { return responseUtility.unauthorized(); }
        final Optional<User> fetchedUser = userRepository.getByEmailAndIsActiveTrue(user.getEmail());
        if (fetchedUser.isEmpty()) {
            try {
                Set<ConstraintViolation<Object>> violations = validator.validate(user);
                if(violations.isEmpty()) {
                    return responseUtility.created(
                            "New user with email " + user.getEmail(),
                            userRepository.save(user));
                }
                else return responseUtility.superBadRequest(violations);
            } catch (Exception e) { return responseUtility.errorMessage(); }
        } else return responseUtility.badRequest("User already exists");
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<CommonResponse> getUser(@PathVariable("user_id") Long userId,
                                                  HttpServletRequest request){
        if (!authService.isAuthorized(request)) { return responseUtility.unauthorized(); }
        final Optional<User> fetchedUser = userRepository.findByIdAndIsActiveTrue(userId);
        if (fetchedUser.isPresent()){
            return responseUtility
                    .ok("User fetched successfully", authService.isAuthorizedAdmin(request)
                            ? getAdminResponse(fetchedUser.get())
                            : getUserResponse(fetchedUser.get()));
        } else return responseUtility.notFound("User not found");
    }

    @PatchMapping("/user/{user_id}")
    public ResponseEntity<CommonResponse> updateUser(@PathVariable("user_id") Long userId,
                                                     @RequestBody User user,
                                                     HttpServletRequest request) {
        if (!authService.isAuthorizedAdmin(request) &&
                authService.currentUser(request).getId().compareTo(userId) != 0)
        { return responseUtility.unauthorized(); }
        final Optional<User> fetchedUser = userRepository.findByIdAndIsActiveTrue(userId);
        if (fetchedUser.isPresent()) {
            final User updatedUser = fetchedUser.get();
            Set<ConstraintViolation<Object>> violations = validator.validate(user);
            if(violations.isEmpty()) {
            if (user.getPassword() != null) return responseUtility.badRequest("Not allowed to patch password");
            if (authService.isAuthorizedAdmin(request)) {
                if (user.getVacationDays() != null) updatedUser.setVacationDays(user.getVacationDays());
                if (user.getUsedVacationDays() != null) updatedUser.setUsedVacationDays(user.getUsedVacationDays());
                if (user.isAdmin() != null) updatedUser.setAdmin(user.isAdmin());
            } else {
                if (user.isAdmin() != null ) return responseUtility.forbidden();
            }
            if (user.getEmail() != null) updatedUser.setEmail(user.getEmail());
            if (user.getFullName() != null) updatedUser.setFullName(user.getFullName());
            if (user.getProfilePic() != null) updatedUser.setProfilePic(user.getProfilePic());
            updatedUser.setModifiedAt(new java.sql.Timestamp(new Date().getTime()));
            try {

                    final User patchedUser = userRepository.save(updatedUser);
                    if (authService.isAuthorizedAdmin(request))
                        observer.sendNotification("Your account have been modified!", updatedUser);
                    return responseUtility.ok("User updated successfully", patchedUser);

            } catch (Exception e) { return responseUtility.errorMessage(); }
            }
            else return responseUtility.superBadRequest(violations);
        } else return responseUtility.notFound("User not found");
    }

    @DeleteMapping("/user/{user_id}")
    public ResponseEntity<CommonResponse> deleteUser(@PathVariable("user_id") Long userId,
                                                     HttpServletRequest request){
        final Optional<User> fetchedUser = userRepository.findByIdAndIsActiveTrue(userId);
        if(!authService.isAuthorizedAdmin(request) &&
                authService.currentUser(request).getId().compareTo(userId) != 0) {
            return responseUtility.forbidden();
        }
        if (fetchedUser.isPresent()) {
            final User user = fetchedUser.get();
            user.setActive(false);
            try {
                userRepository.save(user);
                return responseUtility.ok("User deactivated successfully", null);
            } catch (Exception e) { return responseUtility.errorMessage(); }
        } else return responseUtility.notFound("User not found");
    }

    @GetMapping("/user/{user_id}/requests")
    public ResponseEntity<CommonResponse> getUserVacationRequests(@PathVariable("user_id") Long userId,
                                                                  HttpServletRequest request){
        if(!authService.isAuthorized(request)) { return responseUtility.unauthorized(); }
        final Optional<User> fetchedUser = userRepository.findByIdAndIsActiveTrue(userId);
        if (fetchedUser.isPresent()){
            Object data;
            final List<VacationRequest> allVacationRequests =
                   vacationRequestRepository.findAllByOwner(fetchedUser.get());
            final String message = "Vacation requests for user " +
                    fetchedUser.get().getFullName() + " fetched successfully";
            if (authService.isAuthorizedAdmin(request) ||
                    authService.currentUser(request).getId().compareTo(userId) == 0) {
                data = allVacationRequests;
            } else {
                data = allVacationRequests.stream()
                        .filter(VacationRequest::onlyApproved).collect(Collectors.toList());
            }
            return responseUtility.ok(message, data);
        } else return responseUtility.notFound("User not found");
    }

    @PostMapping("/user/{user_id}/update_password")
    public ResponseEntity<CommonResponse> updatePassword(@PathVariable("user_id") Long userId,
                                                         @RequestBody User user,
                                                         HttpServletRequest request) {
        final Optional<User> fetchedUser = userRepository.findByIdAndIsActiveTrue(userId);
        if(!authService.isAuthorizedAdmin(request) &&
                authService.currentUser(request).getId().compareTo(userId) != 0) {
            return responseUtility.unauthorized();
        }
        if (fetchedUser.isPresent()) {
            final User updatedUser = fetchedUser.get();
            if (user.getPassword() != null) updatedUser.setPassword(user.getPassword());
            try {
                Set<ConstraintViolation<Object>> violations = validator.validate(user);
                if(violations.isEmpty()) {
                    userRepository.save(updatedUser);
                    if(authService.isAuthorizedAdmin(request) && !authService.currentUser(request).getId().equals(user.getId()))
                        observer.sendNotification("Your password have been updated!", updatedUser);
                    return responseUtility.ok("User password updated successfully", null);
                }
                else return responseUtility.superBadRequest(violations);
            } catch (Exception e) { return responseUtility.errorMessage(); }
        } else return responseUtility.notFound("User not found");
    }

    @GetMapping("/user/all")
    public ResponseEntity<CommonResponse> getAllUsers(HttpServletRequest request) {
        if(authService.isAuthorizedAdmin(request)) {
            try{
                return responseUtility.ok("All users", userRepository.findAllByIsActiveTrue());
            } catch (Exception e) { return responseUtility.errorMessage(); }
        } else return responseUtility.unauthorized();
    }

    private HashMap<String, Object> getUserResponse(User user) {
        final HashMap<String, Object> data = new HashMap<>();
        data.put("email", user.getEmail());
        data.put("full_name", user.getFullName());
        data.put("profile_pic", user.getProfilePic());
        return data;
    }

    private HashMap<String, Object> getAdminResponse(User user) {
        final HashMap<String, Object> data = getUserResponse(user);
        data.put("user_id", user.getId());
        data.put("created_at", user.getCreatedAt());
        data.put("is_admin", user.isAdmin());
        data.put("vacation_days", user.getVacationDays());
        data.put("used_vacation_days", user.getUsedVacationDays());
        return data;
    }
}
