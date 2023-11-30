package com.novatechzone.service;

import com.novatechzone.dto.CustomerNotificationDTO;
import com.novatechzone.dto.VerificationCodeDTO;
import com.novatechzone.model.CustomerNotification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface CustomerNotificationService {
    public void saveNotification(CustomerNotificationDTO customerNotificationDTO);

    ResponseEntity<?> getCustomerNotification(VerificationCodeDTO verificationCodeDTO);
}
