package se.experis.tidsbanken.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import se.experis.tidsbanken.server.models.*;
import se.experis.tidsbanken.server.repositories.UserRepository;
import se.experis.tidsbanken.server.repositories.VacationRequestRepository;
import se.experis.tidsbanken.server.services.AuthorizationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VacationRequestRepository vacationRequestRepository;

    @Autowired
    private AuthorizationService authorizationService;

    @GetMapping("/user")
    public ResponseEntity<CommonResponse> getUser(HttpServletRequest request) {
        if(!authorizationService.isAuthorized(request)) { return unauthorized(); }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/user/" + authorizationService.currentUser(request).user_id));
        return new ResponseEntity<>(headers, HttpStatus.PERMANENT_REDIRECT);
    }

    @PostMapping("/user")
    public ResponseEntity<CommonResponse> createUser(@RequestBody AppUser user,
                                                     HttpServletRequest request) {
        if (!authorizationService.isAuthorizedAdmin(request)) { return unauthorized(); }
        Optional<AppUser> fetchedUser = userRepository.getByEmail(user.email);
        if (fetchedUser.isEmpty()) {
            try {
                userRepository.save(user);
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new CommonResponse("New user with email " + user.email, getAdminResponse(user)));
            } catch (Exception e) { return errorMessage(e); }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CommonResponse("User already exists"));
        }
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<CommonResponse> getUser(@PathVariable("user_id") Long userId,
                                                  HttpServletRequest request){
        if (!authorizationService.isAuthorized(request)) { return unauthorized(); }
        Optional<AppUser> fetchedUser = userRepository.findById(userId);
        if (fetchedUser.isPresent()){
            AppUser user = fetchedUser.get();
            HashMap<String, Object> data;
            data = authorizationService.isAuthorizedAdmin(request) ? getAdminResponse(user) : getUserResponse(user);
            return ResponseEntity.ok(new CommonResponse("User fetched successfully", data));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CommonResponse("User not found"));
        }
    }

    @PatchMapping("/user/{user_id}")
    public ResponseEntity<CommonResponse> updateUser(@PathVariable("user_id") Long userId,
                                                     @RequestBody AppUser user,
                                                     HttpServletRequest request) {
        if (!authorizationService.isAuthorized(request)) { return unauthorized(); }
        Optional<AppUser> fetchedUser = userRepository.findById(userId);
        if (fetchedUser.isPresent()) {
            AppUser updatedUser = fetchedUser.get();
            if (user.email != null) updatedUser.email = user.email;
            if (user.full_name != null) updatedUser.full_name = user.full_name;
            if (user.profile_pic != null) updatedUser.profile_pic = user.profile_pic;
            if (user.vacation_days != null) updatedUser.vacation_days = user.vacation_days;
            if (user.used_vacation_days != null) updatedUser.used_vacation_days = user.used_vacation_days;
            updatedUser.modified_at = new java.sql.Timestamp(new Date().getTime());
            if (authorizationService.isAuthorizedAdmin(request)) {
                updatedUser.isAdmin = user.isAdmin;
            }
            try {
                userRepository.save(updatedUser);
                return ResponseEntity.ok(new CommonResponse("User updated successfully", getUserResponse(user)));
            } catch (Exception e) {
                return errorMessage(e);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CommonResponse("User not found"));

        }
    }

    @DeleteMapping("/user/{user_id}")
    public ResponseEntity<CommonResponse> deleteUser(@PathVariable("user_id") Long userId,
                                                     HttpServletRequest request){
        Optional<AppUser> fetchedUser = userRepository.findById(userId);
        if(!authorizationService.isAuthorizedAdmin(request) &&
                authorizationService.currentUser(request).user_id.compareTo(userId) != 0) {
            return unauthorized();
        }
        if (fetchedUser.isPresent()) {
            AppUser user = fetchedUser.get();
            user.isActive = false;
            try {
                userRepository.save(user);
                return ResponseEntity.ok(new CommonResponse("User deactivated successfully"));
            } catch (Exception e) {
                return errorMessage(e);
            }

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CommonResponse("User not found"));
        }
    }

    @GetMapping("/user/{user_id}/requests")
    public ResponseEntity<CommonResponse> getUserVacationRequests(@PathVariable("user_id") Long userId,
                                                                  HttpServletRequest request){
        if(!authorizationService.isAuthorized(request)) { return unauthorized(); }
        Optional<AppUser> fetchedUser = userRepository.findById(userId);
        if (fetchedUser.isPresent()){
            CommonResponse cr;
            List<VacationRequest> allVacationRequests = vacationRequestRepository.findAllByOwner(fetchedUser.get());
            String message = "Vacation requests for user " + fetchedUser.get().full_name + " fetched successfully";
            if (authorizationService.isAuthorizedAdmin(request) ||
                    authorizationService.currentUser(request).user_id.compareTo(userId) == 0) {
                cr = new CommonResponse(message, allVacationRequests);
            } else {
                List<VacationRequest> onlyApproved = allVacationRequests.stream()
                        .filter(VacationRequest::onlyApproved).collect(Collectors.toList());
                cr = new CommonResponse(message, onlyApproved);
            }
            return ResponseEntity.ok(cr);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CommonResponse("User not found"));
        }
    }

    @PostMapping("/user/{user_id}/update_password")
    public ResponseEntity<CommonResponse> updatePassword(@PathVariable("user_id") Long userId,
                                                         @RequestBody AppUser user,
                                                         HttpServletRequest request) {
        Optional<AppUser> fetchedUser = userRepository.findById(userId);
        if(!authorizationService.isAuthorizedAdmin(request) &&
                authorizationService.currentUser(request).user_id.compareTo(userId) != 0) {
            return unauthorized();
        }
        if (fetchedUser.isPresent()) {
            AppUser updatedUser = fetchedUser.get();
            if (user.password != null) updatedUser.password = user.password;
            try {
                userRepository.save(updatedUser);
                return ResponseEntity.ok(new CommonResponse("User password updated successfully"));
            } catch (Exception e) { return errorMessage(e); }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CommonResponse("User not found"));
        }
    }

    private ResponseEntity<CommonResponse> errorMessage (Exception e) {
        System.out.println(e.getMessage());
        return new ResponseEntity<>(
                new CommonResponse("Something went wrong when trying to process the request"),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private HashMap<String, Object> getUserResponse(AppUser user) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("full_name", user.full_name);
        data.put("profile_pic", user.profile_pic);
        return data;
    }

    private HashMap<String, Object> getAdminResponse(AppUser user) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("email", user.email);
        data.put("full_name", user.full_name);
        data.put("profile_pic", user.profile_pic);
        data.put("vacation_days", user.vacation_days);
        data.put("used_vacation_days", user.used_vacation_days);
        data.put("created_at", user.created_at);
        data.put("is_admin", user.isAdmin);
        return data;
    }

    private ResponseEntity<CommonResponse> unauthorized() {
        return new ResponseEntity<>(
                new CommonResponse( "Not Authorized"),
                HttpStatus.UNAUTHORIZED);
    }
}
