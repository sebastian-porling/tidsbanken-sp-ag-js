package se.experis.tidsbanken.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.experis.tidsbanken.server.repositories.IneligiblePeriodRepository;
import se.experis.tidsbanken.server.models.*;

import java.util.*;
import se.experis.tidsbanken.server.models.CommonResponse;
import se.experis.tidsbanken.server.models.IneligiblePeriod;

@Controller
public class IneligibleController{

    @Autowired
    private IneligiblePeriodRepository ipRepository;

    @GetMapping("/ineligible")
    public ResponseEntity<CommonResponse> getIneligiblePeriod(){
        CommonResponse cr = new CommonResponse();
        HttpStatus resStatus;

        try {
            cr.data = ipRepository.findAll(Sort.by(Sort.Direction.DESC, "period_start"));
            cr.message = "All ineligible periods";
            cr.status = 200;
            resStatus = HttpStatus.OK;
        } catch (Exception e) {
            cr.message = "Something went wrong when trying to process the request";
            resStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(cr, resStatus);
    }


    @PostMapping("/ineligible")
    public ResponseEntity<CommonResponse> createIneligiblePeriod(@RequestBody IneligiblePeriod ip) {
        CommonResponse cr = new CommonResponse();
        HttpStatus resStatus;

        List<IneligiblePeriod> allPeriods = ipRepository.findAll();
        boolean found = false;
        boolean overwritten = false;

        Date newStart = ip.period_start;
        Date newEnd = ip.period_end;

        for (IneligiblePeriod period: allPeriods) {
            Date oldStart = period.period_start;
            Date oldEnd = period.period_end;

            // If new period is same as already existing period
            if ((newStart == oldStart) && (oldEnd == newEnd)) {
                found = true;
                break;
            }
            // The following if statements is a check to see if the new period overlaps an already existing period
            // Overlaps: Start period before existing start but end overlaps.
            else if (newStart.before(oldStart) && (newEnd.before(oldEnd) && newEnd.after(oldEnd))) {
                period.period_start = ip.period_start;
                period.modified_at = new java.sql.Timestamp(new Date().getTime());
                try {
                    ipRepository.save(period);
                    cr.data = period;
                    overwritten = true;
                } catch (Exception e) {
                    return errorMessage(e);
                }
                break;
            }
            //Overlaps: Start period overlaps but end is after existing end.
            else if ((newStart.after(oldStart) && newStart.after(oldEnd)) && newEnd.after(oldEnd)) {
                period.period_end = ip.period_end;
                period.modified_at = new java.sql.Timestamp(new Date().getTime());
                try {
                    ipRepository.save(period);
                    cr.data = period;
                    overwritten = true;
                } catch (Exception e) {
                    return errorMessage(e);
                }
                break;
            }
            // Overlaps: Start is before existing start and end is after existing end
            else if (newStart.before(oldStart) && newEnd.after(oldEnd)) {
                period.period_end = ip.period_end;
                period.period_start = ip.period_start;
                period.modified_at = new java.sql.Timestamp(new Date().getTime());
                try {
                    ipRepository.save(period);
                    cr.data = period;
                    overwritten = true;
                } catch (Exception e) {
                    return errorMessage(e);
                }
                break;
            }
        }

        if(!found) {
            try {
                ipRepository.save(ip);

                cr.message = "New period added";
                cr.data = ip;
                cr.status = 201;
                resStatus = HttpStatus.CREATED;
            } catch (Exception e) {
                return errorMessage(e);
            }
        } else {
            if (overwritten) {
                cr.message = "New period added. Overwrote already existing period";
                cr.status = 201;
                resStatus = HttpStatus.CREATED;
            } else {
                cr.message = "Period already exists";
                cr.status = 400;
                resStatus = HttpStatus.BAD_REQUEST;
            }
        }

        return new ResponseEntity<>(cr, resStatus);
    }

    @GetMapping("/ineligible/:ip_id")
    public ResponseEntity<CommonResponse> getIneligiblePeriodForId(@PathVariable("ip_id")long ip_id){
        CommonResponse cr = new CommonResponse();
        HttpStatus resStatus;

        Optional<IneligiblePeriod> fetchedPeriod = ipRepository.findById(ip_id);

        if (fetchedPeriod.isPresent()){
            IneligiblePeriod period = fetchedPeriod.get();

            HashMap<String, Object> data = new HashMap<>();
            data.put("period_start", period.period_start);
            data.put("period_end", period.period_end);
            data.put("moderator", period.moderator);
            data.put("created_at", period.created_at);
            data.put("modified_at", period.modified_at);

            cr.data = data;
            cr.message = "Ineligible period fetched successfully";
            cr.status = 200;
            resStatus = HttpStatus.OK;
        } else {
            cr.message = "Ineligible period not found";
            cr.status = 404;
            resStatus = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(cr, resStatus);
    }

    @PatchMapping("/ineligible/:ip_id")
    public ResponseEntity<CommonResponse> updateIneligiblePeriod(@PathVariable("ip_id")long ip_id,
                                                                 @RequestBody IneligiblePeriod ip) {
        CommonResponse cr = new CommonResponse();
        HttpStatus resStatus;

        Optional<IneligiblePeriod> fetchedPeriod = ipRepository.findById(ip_id);
        /* NOT IMPLEMENTED:
         *      Check if admin
         */
        if (fetchedPeriod.isPresent()) {
            IneligiblePeriod updatedPeriod = fetchedPeriod.get();
            if (ip.period_start != null) updatedPeriod.period_start = ip.period_start;
            if (ip.period_end != null) updatedPeriod.period_end = ip.period_end;
            if (ip.moderator != null) updatedPeriod.moderator = ip.moderator;
            updatedPeriod.modified_at = new java.sql.Timestamp(new Date().getTime());

            try {
                ipRepository.save(updatedPeriod);

                /* Maybe separate out this later? */
                HashMap<String, Object> data = new HashMap<>();
                data.put("period_start", updatedPeriod.period_start);
                data.put("period_end", updatedPeriod.period_end);
                data.put("moderator", updatedPeriod.moderator);
                data.put("created_at", updatedPeriod.created_at);
                data.put("modified_at", updatedPeriod.modified_at);

                cr.data = data;
                cr.message = "Ineligible period updated successfully";
                cr.status = 200;
                resStatus = HttpStatus.OK;
            } catch (Exception e) {
                return errorMessage(e);
            }

        } else {
            cr.message = "Ineligible period not found";
            cr.status = 404;
            resStatus = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<>(cr, resStatus);
    }

    @DeleteMapping("/ineligible/:ip_id")
    public ResponseEntity<CommonResponse> deleteIneligiblePeriod(@PathVariable("ip_id")long ip_id){
        CommonResponse cr = new CommonResponse();
        HttpStatus resStatus;

        Optional<IneligiblePeriod> fetchedPeriod = ipRepository.findById(ip_id);
        /* NOT IMPLEMENTED:
         *      Check if admin
         */
        if (fetchedPeriod.isPresent()) {
            try {
                ipRepository.deleteById(ip_id);

                cr.message = "Ineligible period deleted successfully";
                cr.status = 200;
                resStatus = HttpStatus.OK;
            }catch (Exception e) {
                return errorMessage(e);
            }
        } else {
            cr.message = "Ineligible period not found";
            cr.status = 404;
            resStatus = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<>(cr, resStatus);
    }

    private ResponseEntity<CommonResponse> errorMessage (Exception e) {
        CommonResponse cr = new CommonResponse();
        System.out.println(e.getMessage());
        cr.message = "Something went wrong when trying to process the request";
        HttpStatus resStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<CommonResponse>(cr, resStatus);
    }
}