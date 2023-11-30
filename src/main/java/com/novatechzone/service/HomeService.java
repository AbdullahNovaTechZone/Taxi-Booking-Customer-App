package com.novatechzone.service;

import com.novatechzone.dto.VerificationCodeDTO;
import com.novatechzone.dto.VerifyRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface HomeService {
    ResponseEntity<?> checkCustomerOnGoingTrip(VerificationCodeDTO verificationCodeDTO);
}
