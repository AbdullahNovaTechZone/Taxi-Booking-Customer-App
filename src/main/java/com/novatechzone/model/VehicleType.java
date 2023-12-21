package com.novatechzone.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vehicle_type")
public class VehicleType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_type_id")
    private int vehicleTypeId;
    @Column(name = "vehicle_type", length = 150)
    private String vehicleType;
    @Column(name = "image", columnDefinition = "TEXT")
    private String image;
    @Column(name = "detail", columnDefinition = "TEXT")
    private String detail;
    @Column(name = "per_km_charge")
    private Double perKmCharge;
    @Column(name = "company_income_rate", length = 10)
    private String companyIncomeRate;
    @Column(name = "delay_charge")
    private Double delayCharge;
    @Column(name = "passenger_count", length = 10)
    private String passengerCount;
    @Column(name = "order_level", length = 10)
    private String orderLevel;
    @Column(name = "initial_distance", length = 45)
    private String initialDistance;
    @Column(name = "initial_distance_charge", length = 45)
    private String initialDistanceCharge = "50";
    @Column(name = "status")
    private int status = 1;
}