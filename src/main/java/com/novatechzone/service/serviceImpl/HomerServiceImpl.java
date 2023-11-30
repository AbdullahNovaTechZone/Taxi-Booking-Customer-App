package com.novatechzone.service.serviceImpl;

import com.novatechzone.dto.CustomerNotificationDTO;
import com.novatechzone.dto.RequestMetaDTO;
import com.novatechzone.dto.VerificationCodeDTO;
import com.novatechzone.dto.VerifyRequestDTO;
import com.novatechzone.model.CustomerTrip;
import com.novatechzone.repository.CustomerRepository;
import com.novatechzone.repository.CustomerTripRepository;
import com.novatechzone.service.CustomerNotificationService;
import com.novatechzone.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HomerServiceImpl implements HomeService {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    CustomerTripRepository customerTripRepository;
    @Autowired
    RequestMetaDTO requestMetaDTO;
    @Autowired
    CustomerNotificationService customerNotificationService;

    @Override
    public ResponseEntity<?> checkCustomerOnGoingTrip(VerificationCodeDTO verificationCodeDTO) {
        if (customerRepository.findById(requestMetaDTO.getCustomerId()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid Customer Id!");
        } else if (verificationCodeDTO.getVerification().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Verification Not Found!");
        } else if (!customerRepository.findById(requestMetaDTO.getCustomerId()).get().getVerification().equals(verificationCodeDTO.getVerification())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Verification!");
        } else {
            List<CustomerTrip> customerTrips = customerTripRepository.findAllByCustomerId(requestMetaDTO.getCustomerId());
            List<CustomerTrip> onGoingTrips = new ArrayList<>();
            customerTrips.forEach(trips -> {
                if (String.valueOf(trips.getTripStatus()).equals("0") ||
                        String.valueOf(trips.getTripStatus()).equals("1") ||
                        String.valueOf(trips.getTripStatus()).equals("2") ||
                        String.valueOf(trips.getTripStatus()).equals("3")) {
                    onGoingTrips.add(trips);

                    customerNotificationService.saveNotification(new CustomerNotificationDTO(
                            requestMetaDTO.getCustomerId(),
                            "Your Ongoing Trips (Trip ID:" + trips.getCustomerTripId() + ")!",
                            "Ongoing Trips!",
                            1));
                }
            });

            return ResponseEntity.status(HttpStatus.OK).body(onGoingTrips);
        }
    }
}
