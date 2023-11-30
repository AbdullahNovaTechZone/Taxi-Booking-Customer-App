package com.novatechzone.service.serviceImpl;

import com.novatechzone.dto.CustomerNotificationDTO;
import com.novatechzone.dto.MessageDTO;
import com.novatechzone.dto.RequestMetaDTO;
import com.novatechzone.model.CustomerContact;
import com.novatechzone.repository.CustomerRepository;
import com.novatechzone.repository.MessageRepository;
import com.novatechzone.service.CustomerNotificationService;
import com.novatechzone.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    RequestMetaDTO requestMetaDTO;
    @Autowired
    CustomerNotificationService customerNotificationService;

    @Override
    public ResponseEntity<?> sendMessage(MessageDTO messageDTO) {
        if (customerRepository.findById(requestMetaDTO.getCustomerId()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid Customer Id!");
        } else if (messageDTO.getVerification().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Verification Not Found!");
        } else if (!customerRepository.findById(requestMetaDTO.getCustomerId()).get().getVerification().equals(messageDTO.getVerification())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Verification!");
        } else if (messageDTO.getEmail().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email Not Found!");
        } else if (messageDTO.getMessage().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message Not Found!");
        } else {
            CustomerContact customerContact = new CustomerContact();
            customerContact.setCustomerId(requestMetaDTO.getCustomerId());
            customerContact.setEmail(messageDTO.getEmail());
            customerContact.setMessage(messageDTO.getMessage());
            customerContact.setDate(String.valueOf(LocalDate.now()));
            customerContact.setTime(String.valueOf(LocalTime.now()));

            messageRepository.save(customerContact);

            customerNotificationService.saveNotification(new CustomerNotificationDTO(
                    requestMetaDTO.getCustomerId(),
                    "Your Message Sent Successfully!",
                    "Message Sent!",
                    1));

            return ResponseEntity.status(HttpStatus.OK).body("Message Sent!");
        }
    }
}
