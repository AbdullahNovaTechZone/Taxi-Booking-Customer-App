package com.novatechzone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewTripRequestDTO {
    private String verification;
    private int vehicleTypeId;
    private String startLat;
    private String startLon;
    private String endLat;
    private String endLon;
    private String customerNote;
    private Double distance;
    private String startPoint;
    private String endPoint;
}
