package se.experis.tidsbanken.server.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.experis.tidsbanken.server.DTOs.ImportExportDTO;
import se.experis.tidsbanken.server.models.*;
import se.experis.tidsbanken.server.services.*;
import se.experis.tidsbanken.server.socket.NotificationObserver;
import se.experis.tidsbanken.server.utils.ResponseUtility;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ImportExportController {

    @Autowired private AuthorizationService authService;
    @Autowired private ResponseUtility responseUtility;
    @Autowired private NotificationObserver observer;
    @Autowired private ImportExportService importExportService;
    private Logger logger = LoggerFactory.getLogger(CommentController.class);

    @GetMapping("/export")
    public ResponseEntity<CommonResponse> exportVacations(HttpServletRequest request) {
        if (!authService.isAuthorizedAdmin(request)) return responseUtility.unauthorized();
        try{
            return responseUtility
                    .ok("All Export Data", importExportService.getExportData());
        } catch(Exception e) {
            logger.error(e.getMessage());
            return responseUtility.errorMessage("export vacations");
        }
    }

    @PostMapping("/import")
    public ResponseEntity<CommonResponse> importVacations(HttpServletRequest request,
                                                          @RequestBody List<ImportExportDTO> importData){
        if (!authService.isAuthorizedAdmin(request)) return responseUtility.unauthorized();
        try{
            importExportService.saveImportData(importData);
            return responseUtility.created("All Data Was Imported!", null);
        } catch(Exception e) {
            /* TODO: Add different error messages depending on what went wrong */
            logger.error(e.getMessage());
            return responseUtility.errorMessage("import vacations");
        }
    }

}
