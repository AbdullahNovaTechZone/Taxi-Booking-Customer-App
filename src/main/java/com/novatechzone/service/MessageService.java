package com.novatechzone.service;

import com.novatechzone.dto.MessageDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface MessageService {
    ResponseEntity<?> sendMessage(MessageDTO messageDTO);
}
