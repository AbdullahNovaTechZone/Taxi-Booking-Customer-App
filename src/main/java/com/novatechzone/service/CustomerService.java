package com.novatechzone.service;

import com.novatechzone.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface CustomerService {
    ResponseEntity<?> registerAndLoginCustomer(CustomerAuthRequestDTO customerAuthRequestDTO);

    ResponseEntity<?> getCustomerPinNumber(CustomerMobileDTO customerMobileDTO);

    ResponseEntity<?> verifyCustomerMobile(VerifyRequestDTO verifyRequestDTO);

    ResponseEntity<?> updateProfile(CustomerUpdateProfileDTO customerUpdateProfileDTO);

    ResponseEntity<?> updateHomeOfficeAddress(UpdateAddressDTO updateAddressDTO);

    ResponseEntity<?> removeHomeOfficeAddress(RemoveAddressDTO removeAddressDTO);

    ResponseEntity<?> updateProfileImage(String verification, MultipartFile file) throws IOException;
}
