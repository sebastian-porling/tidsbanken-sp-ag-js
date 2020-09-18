package se.experis.tidsbanken.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import se.experis.tidsbanken.server.models.*;
import se.experis.tidsbanken.server.repositories.*;
import se.experis.tidsbanken.server.services.AuthorizationService;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class UserController {

    @Autowired private UserRepository userRepository;

    @Autowired private VacationRequestRepository vacationRequestRepository;

    @Autowired private AuthorizationService authorizationService;

    @GetMapping("/user")
    public ResponseEntity<CommonResponse> getUser(HttpServletRequest request) {
        if(!authorizationService.isAuthorized(request)) { return unauthorized(); }
        final HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/user/" + authorizationService.currentUser(request).getId()));
        return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
    }

    @PostMapping("/user")
    public ResponseEntity<CommonResponse> createUser(@RequestBody User user,
                                                     HttpServletRequest request) {
        if (!authorizationService.isAuthorizedAdmin(request)) { return unauthorized(); }
        final Optional<User> fetchedUser = userRepository.getByEmail(user.getEmail());
        if (fetchedUser.isEmpty()) {
            try {
                userRepository.save(user);
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(new CommonResponse(
                                "New user with email " + user.getEmail(),
                                getAdminResponse(user)));
            } catch (Exception e) { return errorMessage(e); }
        } else {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new CommonResponse("User already exists"));
        }
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<CommonResponse> getUser(@PathVariable("user_id") Long userId,
                                                  HttpServletRequest request){
        if (!authorizationService.isAuthorized(request)) { return unauthorized(); }
        final Optional<User> fetchedUser = userRepository.findById(userId);
        if (fetchedUser.isPresent()){
            final User user = fetchedUser.get();
            final HashMap<String, Object> data =
                    authorizationService.isAuthorizedAdmin(request)
                    ? getAdminResponse(user)
                    : getUserResponse(user);
            return ResponseEntity.ok(new CommonResponse(
                    "User fetched successfully", data));
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse("User not found"));
        }
    }

    @PatchMapping("/user/{user_id}")
    public ResponseEntity<CommonResponse> updateUser(@PathVariable("user_id") Long userId,
                                                     @RequestBody User user,
                                                     HttpServletRequest request) {
        if (!authorizationService.isAuthorized(request)) { return unauthorized(); }

        final Optional<User> fetchedUser = userRepository.findById(userId);
        if (fetchedUser.isPresent()) {
            User updatedUser = fetchedUser.get();
            if (user.getPassword() != null) return ResponseEntity
                    .badRequest()
                    .body(new CommonResponse("Not allowed to patch password"));
            if (user.getEmail() != null) updatedUser.setEmail(user.getEmail());
            if (user.getFullName() != null) updatedUser.setFullName(user.getFullName());
            if (user.getProfilePic() != null) updatedUser.setProfilePic(user.getProfilePic());
            if (user.getVacationDays() != null) updatedUser.setVacationDays(user.getVacationDays());
            if (user.getUsedVacationDays() != null) updatedUser.setUsedVacationDays(user.getUsedVacationDays());
            if (authorizationService.isAuthorizedAdmin(request)) {
                updatedUser.setAdmin(user.isAdmin());
            } else  if (user.isAdmin() != null) return forbidden();
            updatedUser.setModifiedAt(new java.sql.Timestamp(new Date().getTime()));
            try {
                userRepository.save(updatedUser);
                return ResponseEntity.ok(new CommonResponse(
                        "User updated successfully", getUserResponse(user)));
            } catch (Exception e) {
                return errorMessage(e);
            }
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse("User not found"));

        }
    }

    @DeleteMapping("/user/{user_id}")
    public ResponseEntity<CommonResponse> deleteUser(@PathVariable("user_id") Long userId,
                                                     HttpServletRequest request){
        Optional<User> fetchedUser = userRepository.findById(userId);
        if(!authorizationService.isAuthorizedAdmin(request) &&
                authorizationService.currentUser(request).getId().compareTo(userId) != 0) {
            return unauthorized();
        }
        if (fetchedUser.isPresent()) {
            final User user = fetchedUser.get();
            user.setActive(false);
            try {
                userRepository.save(user);
                return ResponseEntity.ok(new CommonResponse(
                        "User deactivated successfully"));
            } catch (Exception e) {
                return errorMessage(e);
            }

        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse("User not found"));
        }
    }

    @GetMapping("/user/{user_id}/requests")
    public ResponseEntity<CommonResponse> getUserVacationRequests(@PathVariable("user_id") Long userId,
                                                                  HttpServletRequest request){
        if(!authorizationService.isAuthorized(request)) { return unauthorized(); }
        final Optional<User> fetchedUser = userRepository.findById(userId);
        if (fetchedUser.isPresent()){
            CommonResponse cr;
            final  List<VacationRequest> allVacationRequests =
                   vacationRequestRepository.findAllByOwner(fetchedUser.get());
            final String message = "Vacation requests for user " +
                    fetchedUser.get().getFullName() + " fetched successfully";
            if (authorizationService.isAuthorizedAdmin(request) ||
                    authorizationService.currentUser(request).getId().compareTo(userId) == 0) {
                cr = new CommonResponse(message, allVacationRequests);
            } else {
                final List<VacationRequest> onlyApproved = allVacationRequests.stream()
                        .filter(VacationRequest::onlyApproved).collect(Collectors.toList());
                cr = new CommonResponse(message, onlyApproved);
            }
            return ResponseEntity.ok(cr);
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse("User not found"));
        }
    }

    @PostMapping("/user/{user_id}/update_password")
    public ResponseEntity<CommonResponse> updatePassword(@PathVariable("user_id") Long userId,
                                                         @RequestBody User user,
                                                         HttpServletRequest request) {
        Optional<User> fetchedUser = userRepository.findById(userId);
        if(!authorizationService.isAuthorizedAdmin(request) &&
                authorizationService.currentUser(request).getId().compareTo(userId) != 0) {
            return unauthorized();
        }
        if (fetchedUser.isPresent()) {
            final User updatedUser = fetchedUser.get();
            if (user.getPassword() != null) updatedUser.setPassword(user.getPassword());
            try {
                userRepository.save(updatedUser);
                return ResponseEntity.ok(new CommonResponse(
                        "User password updated successfully"));
            } catch (Exception e) { return errorMessage(e); }
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse("User not found"));
        }
    }

    @GetMapping("/user/all")
    public ResponseEntity<CommonResponse> getAllUsers(HttpServletRequest request) {
        if(authorizationService.isAuthorizedAdmin(request)) {
            try{
                return ResponseEntity.ok(
                        new CommonResponse("All users", userRepository.findAll()));
            } catch (Exception e) { return errorMessage(e); }
        } else return unauthorized();
    }

    private ResponseEntity<CommonResponse> errorMessage (Exception e) {
        System.out.println(e.getMessage());
        return new ResponseEntity<>(
                new CommonResponse(
                        "Something went wrong when trying to process the request"),
                        HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private HashMap<String, Object> getUserResponse(User user) {
        final HashMap<String, Object> data = new HashMap<>();
        data.put("email", user.getEmail());
        data.put("full_name", user.getFullName());
        data.put("profile_pic", user.getProfilePic());
        data.put("vacation_days", user.getVacationDays());
        data.put("used_vacation_days", user.getUsedVacationDays());
        return data;
    }

    private HashMap<String, Object> getAdminResponse(User user) {
        final HashMap<String, Object> data = getUserResponse(user);
        data.put("created_at", user.getCreatedAt());
        data.put("is_admin", user.isAdmin());
        return data;
    }

    private ResponseEntity<CommonResponse> unauthorized() {
        return new ResponseEntity<>(
                new CommonResponse( "Not Authorized"),
                HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<CommonResponse> forbidden() {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new CommonResponse("Forbidden"));
    }
}
