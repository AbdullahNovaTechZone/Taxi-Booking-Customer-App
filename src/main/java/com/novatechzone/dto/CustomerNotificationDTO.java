package com.novatechzone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerNotificationDTO {
    private int customerId;
    private String notification;
    private String notificationTopic;
    private int showToUser;
}

