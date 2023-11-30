package com.novatechzone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RateRiderDTO {
    private String verification;
    private String starRate;
    private String comment;
}
