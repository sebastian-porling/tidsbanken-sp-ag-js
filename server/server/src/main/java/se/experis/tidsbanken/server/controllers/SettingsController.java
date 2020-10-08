package se.experis.tidsbanken.server.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import se.experis.tidsbanken.server.models.CommonResponse;
import se.experis.tidsbanken.server.models.Setting;
import se.experis.tidsbanken.server.models.User;
import se.experis.tidsbanken.server.repositories.*;
import se.experis.tidsbanken.server.services.AuthorizationService;
import se.experis.tidsbanken.server.socket.NotificationObserver;
import se.experis.tidsbanken.server.utils.ResponseUtility;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Optional;
import java.util.Set;

/**
 * Handles all functionality for settings.
 * Are admin only
 */
@RestController
@RequestMapping("/api")
public class SettingsController {

    @Autowired private ResponseUtility responseUtility;
    @Autowired private SettingRepository settingRepository;
    @Autowired private AuthorizationService authService;
    @Autowired private UserRepository userRepository;
    @Autowired private NotificationObserver observer;
    @Autowired private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    @Autowired private Validator validator = factory.getValidator();
    private Logger logger = LoggerFactory.getLogger(CommentController.class);

    /**
     * Return all settings
     * @param request HttpServletRequest
     * @return 200 with all settings, 401 if not admin, 500 on server error
     */
    @GetMapping("/setting")
    public ResponseEntity<CommonResponse> getSettings(HttpServletRequest request){
        if(!authService.isAuthorizedAdmin(request)) return responseUtility.unauthorized();
        try{
            return responseUtility.ok("All settings", settingRepository.findAll());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return responseUtility.errorMessage("fetch settings");
        }
    }

    /**
     * Creates a new setting with provided setting data
     * @param setting Setting data
     * @param request HttpServlerRequest
     * @return 201 if created, 400 if it exists or not valid, 401 if not admin, 500 on server error
     */
    @PostMapping("/setting")
    public ResponseEntity<CommonResponse> createSetting(@RequestBody Setting setting, HttpServletRequest request){
        if(!authService.isAuthorizedAdmin(request)) return responseUtility.unauthorized();
        try {
            if (!settingRepository.existsByKey(setting.getKey())){
                Set<ConstraintViolation<Object>> violations = validator.validate(setting);
                if(violations.isEmpty()) {
                    final Setting saved = settingRepository.save(setting);
                    notify(saved, authService.currentUser(request), " created setting ");
                    return responseUtility.created("Setting added", saved);
                }
                else return responseUtility.superBadRequest(violations);
            }
            return responseUtility.badRequest("Setting already exists");
        }catch (Exception e) {
            logger.error(e.getMessage());
            return responseUtility.errorMessage("create new setting");
        }
    }

    /**
     * Updates a setting by provided setting id and data
     * @param settingId Setting Id
     * @param setting New Setting Data
     * @param request HttpServlerRequest
     * @return 200 if updated, 401 if not admin, 400 if not valid data, 404 if setting not found, 500 on server error.
     */
    @PatchMapping("/setting/{setting_id}")
    public ResponseEntity<CommonResponse> updateSetting(@PathVariable("setting_id") String settingId,
                                                        @RequestBody Setting setting,
                                                        HttpServletRequest request){
        if(!authService.isAuthorizedAdmin(request)) return responseUtility.unauthorized();
        try {
            final Optional<Setting> settingOp = settingRepository.findById(settingId);
            if (settingOp.isPresent()){
                Set<ConstraintViolation<Object>> violations = validator.validate(setting);
                if(violations.isEmpty()) {
                    final Setting settingPayload = settingOp.get();
                    if (setting.getKey() != null) settingPayload.setKey(setting.getKey());
                    if (setting.getValue() != null) settingPayload.setValue(setting.getValue());
                    final Setting saved = settingRepository.save(settingPayload);
                    notify(saved, authService.currentUser(request), " modified setting ");
                    return responseUtility.ok("Setting updated", saved);
                }
                else return responseUtility.superBadRequest(violations);
            } else return responseUtility.notFound("Setting not found");
        } catch(Exception e) {
            logger.error(e.getMessage());
            return responseUtility.errorMessage("update of setting with id " + settingId);
        }
    }

    /**
     * Returns setting by id
     * @param settingId Setting Id
     * @param request HttpServletReqeust
     * @return 200 with setting, 401 if not admin, 404 if setting not found, 500 on server error
     */
    @GetMapping("/setting/{setting_id}")
    public ResponseEntity<CommonResponse> getSetting(@PathVariable("setting_id") String settingId,
                                                     HttpServletRequest request){
        if(!authService.isAuthorizedAdmin(request)) return responseUtility.unauthorized();
        try {
            final Optional<Setting> settingOp = settingRepository.findById(settingId);
            if (settingOp.isPresent())
                return responseUtility.ok("Setting found", settingOp.get());
            return responseUtility.notFound("Setting with id: " + settingId + " Not found!");
        } catch(Exception e) {
            logger.error(e.getMessage());
            return responseUtility.errorMessage("fetch of setting with id " + settingId);
        }
    }

    /**
     * Deletes the setting with the provided setting id
     * @param settingId Setting Id
     * @param request HttpServletRequest
     * @return 200 if deleted, 401 if not admin, 404 if not found, 500 on server error
     */
    @DeleteMapping("/setting/{setting_id}")
    public ResponseEntity<CommonResponse> deleteSetting(@PathVariable("setting_id") String settingId,
                                                        HttpServletRequest request){
        if(!authService.isAuthorizedAdmin(request)) return responseUtility.unauthorized();
        try {
            final Optional<Setting> settingOp = settingRepository.findById(settingId);
            if (settingOp.isPresent()) {
                settingRepository.delete(settingOp.get());
                notify(settingOp.get(), authService.currentUser(request), " deleted setting ");
                return responseUtility.ok("Setting deleted", null);
            }
            return responseUtility.notFound("Setting with id: " + settingId + " Not found!");
        } catch(Exception e) {
            logger.error(e.getMessage());
            return responseUtility.errorMessage("delete of setting with id " + settingId);
        }
    }

    /**
     * Notifies all admin on setting changes
     * @param setting Setting that have been changed
     * @param performer User who performed the action
     * @param message Message about the action
     */
    private void notify(Setting setting, User performer, String message) {
        userRepository.findAllByIsAdminTrueAndIsActiveTrue().stream()
                .filter(u -> !u.getId().equals(performer.getId()))
                .forEach(u -> observer.sendNotification(performer.getFullName() + message + setting.getKey(), u));
    }
}
