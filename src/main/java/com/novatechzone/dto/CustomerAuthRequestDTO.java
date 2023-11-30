package com.novatechzone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerAuthRequestDTO {
    private String mobile;
    private String name;
    private String notificationKey;
    private String currentLat;
    private String currentLon;
}
