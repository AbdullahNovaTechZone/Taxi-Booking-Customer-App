package com.novatechzone.controller;

import com.novatechzone.dto.*;
import com.novatechzone.service.CustomerTripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trip")
public class CustomerTripController {
    @Autowired
    CustomerTripService customerTripService;

    @PostMapping("/new_trip/{app_id}")
    public ResponseEntity<?> requestNewTripByCustomer(@PathVariable("app_id") String appId, @RequestBody NewTripRequestDTO newTripRequestDTO) {
        if (appId.equals("novatechzone_customer_app")) {
            return customerTripService.requestNewTripByCustomer(newTripRequestDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid App Id!");
        }
    }

    @PostMapping("/reject_trip/{trip_id}/{app_id}")
    public ResponseEntity<?> customerRejectTrip(@PathVariable("app_id") String appId, @PathVariable("trip_id") int tripId, @RequestBody CustomerRejectTripDTO customerRejectTripDTO) {
        if (appId.equals("novatechzone_customer_app")) {
            return customerTripService.customerRejectTrip(tripId, customerRejectTripDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid App Id!");
        }
    }

    @PostMapping("/trip_status/{trip_id}/{app_id}")
    public ResponseEntity<?> checkCustomerTripAcceptedByRider(@PathVariable("app_id") String appId, @PathVariable("trip_id") int tripId, @RequestBody VerificationCodeDTO verificationCodeDTO) {
        if (appId.equals("novatechzone_customer_app")) {
            return customerTripService.checkCustomerTripAcceptedByRider(tripId, verificationCodeDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid App Id!");
        }
    }

    @PostMapping("/trip_auto_cancel/{trip_id}/{app_id}")
    public ResponseEntity<?> autoCancelTripByCustomer(@PathVariable("app_id") String appId, @PathVariable("trip_id") int tripId, @RequestBody VerificationCodeDTO verificationCodeDTO) {
        if (appId.equals("novatechzone_customer_app")) {
//            return customerTripService.autoCancelTripByCustomer(tripId, verificationCodeDTO);
            return null;
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid App Id!");
        }
    }

    @GetMapping("/trip_data/{trip_id}/{app_id}")
    public ResponseEntity<?> getTripDataCustomerById(@PathVariable("app_id") String appId, @PathVariable("trip_id") int tripId, @RequestBody VerificationCodeDTO verificationCodeDTO) {
        if (appId.equals("novatechzone_customer_app")) {
            return customerTripService.getTripDataCustomerById(tripId, verificationCodeDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid App Id!");
        }
    }

    @PostMapping("/rate_rider/{trip_id}/{app_id}")
    public ResponseEntity<?> rateRider(@PathVariable("app_id") String appId, @PathVariable("trip_id") int tripId, @RequestBody RateRiderDTO rateRiderDTO) {
        if (appId.equals("novatechzone_customer_app")) {
            return customerTripService.rateRider(tripId, rateRiderDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid App Id!");
        }
    }

    @GetMapping("/history/{app_id}")
    public ResponseEntity<?> getTripHistoryByCustomer(@PathVariable("app_id") String appId, @RequestBody RequestTripHistoryDTO requestTripHistoryDTO) {
        if (appId.equals("novatechzone_customer_app")) {
            return customerTripService.getTripHistoryByCustomer(requestTripHistoryDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid App Id!");
        }
    }

//    @GetMapping("/recent_trip/{app_id}")
//    public ResponseEntity<?> getCustomerRecentVisitedDestination(@PathVariable("app_id") String appId, @RequestBody VerificationCodeDTO verificationCodeDTO) {
//        if (appId.equals("novatechzone_customer_app")) {
//            return customerTripService.getCustomerRecentVisitedDestination(verificationCodeDTO);
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid App Id!");
//        }
//    }

}
