package com.novatechzone.controller;

import com.novatechzone.dto.VerificationCodeDTO;
import com.novatechzone.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/home")
public class HomeController {
    @Autowired
    HomeService homeService;
    @GetMapping("/ongoing_trip/{app_id}")
    public ResponseEntity<?> checkCustomerOnGoingTrip(@PathVariable("app_id") String appId, @RequestBody VerificationCodeDTO verifyRequestDTO) {
        if (appId.equals("novatechzone_customer_app")) {
            return homeService.checkCustomerOnGoingTrip(verifyRequestDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid App Id!");
        }
    }
}
