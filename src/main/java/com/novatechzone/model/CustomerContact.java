package com.novatechzone.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer_contact")
public class CustomerContact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_contact_id")
    private int customerContactId;
    @Column(name = "customer_id")
    private int customerId;
    @Column(name = "email", length = 850)
    private String email;
    @Column(name = "message", columnDefinition = "TEXT")
    private String message;
    @Column(name = "date", length = 45)
    private String date;
    @Column(name = "time", length = 45)
    private String time;
    @Column(name = "status")
    private int status = 1;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id", updatable = false, insertable = false)
    private Customer customer;
}