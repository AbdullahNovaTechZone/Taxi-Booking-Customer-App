package com.novatechzone.repository;

import com.novatechzone.model.RiderRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RateRiderRepository extends JpaRepository<RiderRating, Integer> {
}
