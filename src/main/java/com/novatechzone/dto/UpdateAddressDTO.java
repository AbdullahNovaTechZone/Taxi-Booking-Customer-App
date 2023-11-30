package com.novatechzone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateAddressDTO {
    private String verification;
    private String addressText;
    private String addressLat;
    private String addressLon;
    private int addressType;
}
