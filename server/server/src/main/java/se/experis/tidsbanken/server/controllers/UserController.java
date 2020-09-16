package se.experis.tidsbanken.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import se.experis.tidsbanken.server.models.*;
import se.experis.tidsbanken.server.repositories.UserRepository;
import se.experis.tidsbanken.server.repositories.VacationRequestRepository;

import java.util.*;


@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VacationRequestRepository vacationRequestRepository;

    @GetMapping("/user")
    public ResponseEntity<CommonResponse> getUser(){
        CommonResponse cr = new CommonResponse();
        cr.message = "Not Implemented";
        HttpStatus resStatus = HttpStatus.NOT_IMPLEMENTED;

        return new ResponseEntity<>(cr, resStatus);
    }

    @PostMapping("/user")
    public ResponseEntity<CommonResponse> createUser(@RequestBody AppUser user) {
        CommonResponse cr = new CommonResponse();
        HttpStatus resStatus;

        Optional<AppUser> fetchedUser = userRepository.findById(user.user_id);
        /* NOT IMPLEMENTED:
         *      Check if admin
        */
        if (fetchedUser.isPresent()) {
            cr.message = "User already exists";
            cr.status = 401;
            resStatus = HttpStatus.UNAUTHORIZED;
        } else {
            try {
                userRepository.save(user);

                HashMap<String, Object> data = new HashMap<>();
                data.put("email", user.email);
                data.put("full_name", user.full_name);
                data.put("vacation_days", user.vacation_days);
                data.put("used_vacation_days", user.used_vacation_days);
                data.put("created_at", user.created_at);

                cr.data = data;
                cr.message = "New user with email " + user.email;
                cr.status = 201;
                resStatus = HttpStatus.CREATED;
            } catch (Exception e) {
                cr.message = e.getMessage();
                resStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }

        return new ResponseEntity<>(cr, resStatus);
    }

    @GetMapping("/user/:user_id")
    public ResponseEntity<CommonResponse> getUser(@PathVariable("user_id") long user_id){
        CommonResponse cr = new CommonResponse();
        HttpStatus resStatus;

        Optional<AppUser> fetchedUser = userRepository.findById(user_id);

        if (fetchedUser.isPresent()){
            /* NOT IMPLEMENTED:
             *      Check token
             *      Return appropriate data
             */
            AppUser user = fetchedUser.get();

            // Admin
            HashMap<String, Object> data = new HashMap<>();
            data.put("email", user.email);
            data.put("full_name", user.full_name);
            data.put("profile_pic", user.profile_pic);
            data.put("vacation_days", user.vacation_days);
            data.put("used_vacation_days", user.used_vacation_days);
            data.put("created_at", user.created_at);

            // Other User
            /*HashMap<String, Object> data = new HashMap<>();
            data.put("full_name", user.full_name);
            data.put("profile_pic", user.profile_pic);*/

            cr.data = data;
            cr.message = "User fetched successfully";
            cr.status = 200;
            resStatus = HttpStatus.OK;
        } else {
            cr.message = "User not found";
            cr.status = 404;
            resStatus = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(cr, resStatus);
    }

    @PatchMapping("/user/:user_id")
    public ResponseEntity<CommonResponse> updateUser(@PathVariable("user_id") long user_id, @RequestBody AppUser user) {
        CommonResponse cr = new CommonResponse();
        HttpStatus resStatus;

        Optional<AppUser> fetchedUser = userRepository.findById(user_id);
        /* NOT IMPLEMENTED:
         *      Check if self or admin
         */
        if (fetchedUser.isPresent()) {
            AppUser updatedUser = fetchedUser.get();
            if (user.email != null) updatedUser.email = user.email;
            if (user.full_name != null) updatedUser.full_name = user.full_name;
            if (user.profile_pic != null) updatedUser.profile_pic = user.profile_pic;
            if (user.vacation_days != null) updatedUser.vacation_days = user.vacation_days;
            if (user.used_vacation_days != null) updatedUser.used_vacation_days = user.used_vacation_days;
            updatedUser.modified_at = new java.sql.Timestamp(new Date().getTime());
            /* if admin - Update isAdmin */

            try {
                userRepository.save(updatedUser);

                /* Maybe separate out this later? */
                HashMap<String, Object> data = new HashMap<>();
                data.put("full_name", user.full_name);
                data.put("profile_pic", user.profile_pic);

                cr.data = data;
                cr.message = "User updated successfully";
                cr.status = 200;
                resStatus = HttpStatus.OK;
            } catch (Exception e) {
                cr.message = e.getMessage();
                resStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            }

        } else {
            cr.message = "User not found";
            cr.status = 404;
            resStatus = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<>(cr, resStatus);
    }

    @DeleteMapping("/user/:user_id")
    public ResponseEntity<CommonResponse> deleteUser(@PathVariable("user_id")long user_id){
        CommonResponse cr = new CommonResponse();
        HttpStatus resStatus;

        Optional<AppUser> fetchedUser = userRepository.findById(user_id);
        /* NOT IMPLEMENTED:
         *      Check if self or admin
         */
        if (fetchedUser.isPresent()) {
            AppUser user = fetchedUser.get();
            user.isActive = false;
            try {
                userRepository.save(user);
                cr.message = "User deactivated successfully";
                cr.status = 200;
                resStatus = HttpStatus.OK;
            } catch (Exception e) {
                cr.message = e.getMessage();
                resStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            }

        } else {
            cr.message = "User not found";
            cr.status = 404;
            resStatus = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<>(cr, resStatus);
    }

    @GetMapping("/user/:user_id/requests")
    public ResponseEntity<CommonResponse> getUserVacationRequests(@PathVariable("user_id")long user_id){
        CommonResponse cr = new CommonResponse();
        HttpStatus resStatus;

        Optional<AppUser> fetchedUser = userRepository.findById(user_id);

        if (fetchedUser.isPresent()){
            List<VacationRequest> allVacationRequests = vacationRequestRepository.findAll();
            List<VacationRequest> vacationRequestList = new ArrayList<>();

            for (VacationRequest request: allVacationRequests) {
                if (request.owner.user_id == user_id) {
                    vacationRequestList.add(request);
                }

            }

            cr.data = vacationRequestList; /* Returns everything atm, needs to be changed later */
            cr.message = "Vacation requests for user " + fetchedUser.get().full_name + " fetched successfully";
            cr.status = 200;
            resStatus = HttpStatus.OK;
        } else {
            cr.message = "User not found";
            cr.status = 404;
            resStatus = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(cr, resStatus);
    }

    @PostMapping("/user/:user_id/update_password")
    public ResponseEntity<CommonResponse> updatePassword(@RequestParam long user_id, @RequestBody AppUser user) {
        CommonResponse cr = new CommonResponse();
        HttpStatus resStatus;

        Optional<AppUser> fetchedUser = userRepository.findById(user_id);
        /* NOT IMPLEMENTED:
         *      Check if self or admin
         */
        if (fetchedUser.isPresent()) {
            AppUser updatedUser = fetchedUser.get();
            if (user.password != null) updatedUser.password = user.password;

            try {
                userRepository.save(updatedUser);

                cr.message = "User password updated successfully";
                cr.status = 200;
                resStatus = HttpStatus.OK;
            } catch (Exception e) {
                cr.message = e.getMessage();
                resStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            }

        } else {
            cr.message = "User not found";
            cr.status = 404;
            resStatus = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<>(cr, resStatus);
    }

}
