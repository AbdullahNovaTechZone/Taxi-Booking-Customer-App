package com.novatechzone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerUpdateProfileDTO {
    private String verification;
    private String fullName;
    private String emergencyNumber;
    private String birthday;
    private String email;
    private String nic;
}
