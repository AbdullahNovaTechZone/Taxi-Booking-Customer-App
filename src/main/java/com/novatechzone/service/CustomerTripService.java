package com.novatechzone.service;

import com.novatechzone.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface CustomerTripService {
    ResponseEntity<?> requestNewTripByCustomer(NewTripRequestDTO newTripRequestDTO);

    ResponseEntity<?> customerRejectTrip(int tripId, CustomerRejectTripDTO customerRejectTripDTO);

    ResponseEntity<?> checkCustomerTripAcceptedByRider(int tripId, VerificationCodeDTO verificationCodeDTO);

    ResponseEntity<?> getTripDataCustomerById(int tripId, VerificationCodeDTO verificationCodeDTO);

    ResponseEntity<?> rateRider(int tripId, RateRiderDTO rateRiderDTO);

    ResponseEntity<?> getTripHistoryByCustomer(RequestTripHistoryDTO requestTripHistoryDTO);

//    ResponseEntity<?> getCustomerRecentVisitedDestination(VerificationCodeDTO verificationCodeDTO);
}
