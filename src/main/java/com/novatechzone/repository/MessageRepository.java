package com.novatechzone.repository;

import com.novatechzone.model.CustomerContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<CustomerContact, Integer> {
}
