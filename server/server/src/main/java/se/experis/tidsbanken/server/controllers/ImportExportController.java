package se.experis.tidsbanken.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.experis.tidsbanken.server.models.*;
import se.experis.tidsbanken.server.services.AuthorizationService;
import se.experis.tidsbanken.server.services.ImportExportService;
import se.experis.tidsbanken.server.socket.NotificationObserver;
import se.experis.tidsbanken.server.utils.ResponseUtility;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ImportExportController {

    @Autowired private AuthorizationService authService;
    @Autowired private ResponseUtility responseUtility;
    @Autowired private NotificationObserver observer;
    @Autowired private ImportExportService importExportService;

    @GetMapping("/export")
    public ResponseEntity<CommonResponse> exportVacations(HttpServletRequest request) {
        if (!authService.isAuthorizedAdmin(request)) return responseUtility.unauthorized();
        try{
            return responseUtility
                    .ok("All Export Data", importExportService.getExportData());
        } catch(Exception e) { return responseUtility.errorMessage(); }
    }

    @PostMapping("/import")
    public ResponseEntity<CommonResponse> importVacations(HttpServletRequest request,
                                                          List<ImportExportDTO> importData){
        if (!authService.isAuthorizedAdmin(request)) return responseUtility.unauthorized();
        try{
            importExportService.saveImportData(importData);
            return responseUtility.created("All Data Was Imported!", null);
        } catch(Exception e) { return responseUtility.errorMessage(); }
    }

}