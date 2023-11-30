package com.novatechzone.controller;

import com.novatechzone.dto.MessageDTO;
import com.novatechzone.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    MessageService messageService;

    @PostMapping("/send_message/{app_id}")
    public ResponseEntity<?> sendMessage(@PathVariable("app_id") String appId, @RequestBody MessageDTO messageDTO) {
        if (appId.equals("novatechzone_customer_app")) {
            return messageService.sendMessage(messageDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid App Id!");
        }
    }
}
