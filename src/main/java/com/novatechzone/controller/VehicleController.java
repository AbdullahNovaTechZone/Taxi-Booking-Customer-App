package com.novatechzone.controller;

import com.novatechzone.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {
    @Autowired
    VehicleService vehicleService;

    @GetMapping("/vehicle_types/{app_id}")
    public ResponseEntity<?> getVehicleTypes(@PathVariable("app_id") String appId) {
        if (appId.equals("novatechzone_customer_app")) {
            return vehicleService.getVehicleTypes();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid App Id!");
        }
    }
}
