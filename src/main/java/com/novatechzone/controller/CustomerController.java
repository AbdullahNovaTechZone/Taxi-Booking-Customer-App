package com.novatechzone.controller;

import com.novatechzone.dto.*;
import com.novatechzone.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @PostMapping("/auth/{app_id}")
    public ResponseEntity<?> registerAndLoginCustomer(@PathVariable("app_id") String appId, @RequestBody CustomerAuthRequestDTO customerAuthRequestDTO) {
        if (appId.equals("novatechzone_customer_app")) {
            return customerService.registerAndLoginCustomer(customerAuthRequestDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid App Id!");
        }
    }

    @PostMapping("/auth/otp/{app_id}")
    public ResponseEntity<?> getCustomerPinNumber(@PathVariable("app_id") String appId, @RequestBody CustomerMobileDTO customerMobileDTO) {
        if (appId.equals("novatechzone_customer_app")) {
            return customerService.getCustomerPinNumber(customerMobileDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid App Id!");
        }
    }

    @PostMapping("/auth/verify/{app_id}")
    public ResponseEntity<?> verifyCustomerMobile(@PathVariable("app_id") String appId, @RequestBody VerifyRequestDTO verifyRequestDTO) {
        if (appId.equals("novatechzone_customer_app")) {
            return customerService.verifyCustomerMobile(verifyRequestDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid App Id!");
        }
    }

    @PutMapping("/profile/{app_id}")
    public ResponseEntity<?> updateProfile(@PathVariable("app_id") String appId, @RequestBody CustomerUpdateProfileDTO customerUpdateProfileDTO) {
        if (appId.equals("novatechzone_customer_app")) {
            return customerService.updateProfile(customerUpdateProfileDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid App Id!");
        }
    }

    @PutMapping("/update_address/{app_id}")
    public ResponseEntity<?> updateHomeOfficeAddress(@PathVariable("app_id") String appId, @RequestBody UpdateAddressDTO updateAddressDTO) {
        if (appId.equals("novatechzone_customer_app")) {
            return customerService.updateHomeOfficeAddress(updateAddressDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid App Id!");
        }
    }

    @DeleteMapping("/remove_address/{app_id}")
    public ResponseEntity<?> removeHomeOfficeAddress(@PathVariable("app_id") String appId, @RequestBody RemoveAddressDTO removeAddressDTO) {
        if (appId.equals("novatechzone_customer_app")) {
            return customerService.removeHomeOfficeAddress(removeAddressDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid App Id!");
        }
    }

    @PostMapping("/update_profile_image/{app_id}")
    public ResponseEntity<?> updateProfileImage(@PathVariable("app_id") String appId, @RequestParam("verification") String verification, @RequestParam("imagePath") MultipartFile file) throws IOException {
        if (appId.equals("novatechzone_customer_app")) {
            return customerService.updateProfileImage(verification, file);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid App Id!");
        }
    }
}
