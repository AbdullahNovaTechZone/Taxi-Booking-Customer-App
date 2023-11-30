package com.novatechzone.service.serviceImpl;

import com.novatechzone.dto.CustomerNotificationDTO;
import com.novatechzone.dto.RequestMetaDTO;
import com.novatechzone.dto.VerificationCodeDTO;
import com.novatechzone.model.CustomerNotification;
import com.novatechzone.repository.CustomerNotificationRepository;
import com.novatechzone.repository.CustomerRepository;
import com.novatechzone.service.CustomerNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class CustomerNotificationServiceImpl implements CustomerNotificationService {
    @Autowired
    CustomerNotificationRepository customerNotificationRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    RequestMetaDTO requestMetaDTO;

    @Override
    public void saveNotification(CustomerNotificationDTO customerNotificationDTO) {
        CustomerNotification customerNotification = new CustomerNotification();
        customerNotification.setCustomerId(customerNotificationDTO.getCustomerId());
        customerNotification.setNotificationTopic(customerNotificationDTO.getNotificationTopic());
        customerNotification.setNotification(customerNotificationDTO.getNotification());
        customerNotification.setDate(String.valueOf(LocalDate.now()));
        customerNotification.setTime(String.valueOf(LocalTime.now()));
        customerNotification.setShowToUser(customerNotificationDTO.getShowToUser());

        customerNotificationRepository.save(customerNotification);
    }

    public ResponseEntity<?> getCustomerNotification(VerificationCodeDTO verificationCodeDTO) {
        if (customerRepository.findById(requestMetaDTO.getCustomerId()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid Customer Id!");
        } else if (verificationCodeDTO.getVerification().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Verification Not Found!");
        } else if (!customerRepository.findById(requestMetaDTO.getCustomerId()).get().getVerification().equals(verificationCodeDTO.getVerification())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Verification!");
        } else {
            List<CustomerNotification> customerNotifications = customerNotificationRepository.findAllByCustomerIdAndShowToUser(requestMetaDTO.getCustomerId(), 1);
            return ResponseEntity.status(HttpStatus.OK).body(customerNotifications);
        }
    }
}
