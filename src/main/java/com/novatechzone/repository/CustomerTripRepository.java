package com.novatechzone.repository;

import com.novatechzone.model.CustomerTrip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerTripRepository extends JpaRepository<CustomerTrip, Integer> {
    List<CustomerTrip> findAllByCustomerId(int customerId);

    List<CustomerTrip> findAllByCustomerIdAndTripDate(int customerId, String currentDate);
}
