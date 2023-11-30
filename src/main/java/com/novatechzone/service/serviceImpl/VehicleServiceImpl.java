package com.novatechzone.service.serviceImpl;

import com.novatechzone.model.VehicleType;
import com.novatechzone.repository.VehicleRepository;
import com.novatechzone.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {
    @Autowired
    VehicleRepository vehicleRepository;

    @Override
    public ResponseEntity<?> getVehicleTypes() {
        List<VehicleType> vehicleTypes = vehicleRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(vehicleTypes);
    }
}
