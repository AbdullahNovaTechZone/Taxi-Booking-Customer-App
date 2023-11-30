package com.novatechzone.model;

//import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private int customerId;
    @Column(name = "full_name", length = 850)
    private String fullName;
    @Column(name = "gender")
    private int gender;
    @Column(name = "nic", length = 45)
    private String nic;
    @Column(name = "country", length = 100)
    private String country;
    @Column(name = "address_type")
    private int addressType;
    @Column(name = "address", length = 850)
    private String address;
    @Column(name = "city", length = 90)
    private String city;
    @Column(name = "postcode", length = 45)
    private String postcode;
    @Column(name = "mobile_1", length = 45)
    private String mobile1;
    @Column(name = "mobile_2", length = 45)
    private String mobile2;
    @Column(name = "birthday", length = 45)
    private String birthday;
    @Column(name = "email", columnDefinition = "TEXT")
    private String email;
    @Column(name = "username", length = 45)
    private String username;
    @Column(name = "password", columnDefinition = "TEXT")
    private String password;
    @Column(name = "nationality", length = 45)
    private String nationality;
    @Column(name = "profile_image", length = 850)
    private String profileImage;
    @Column(name = "verification", length = 50)
    private String verification;
    @Column(name = "credit_customer")
    private int creditCustomer;
    @Column(name = "balance", length = 50)
    private String balance;
    @Column(name = "due", length = 45)
    private String due;
    @Column(name = "note", columnDefinition = "TEXT")
    private String note;
    @Column(name = "home_address_lat", length = 100)
    private String homeAddressLat;
    @Column(name = "home_address_lon", length = 50)
    private String homeAddressLon;
    @Column(name = "office_address_lat", length = 100)
    private String officeAddressLat;
    @Column(name = "office_address_lon", length = 50)
    private String officeAddressLon;
    @Column(name = "home_address", length = 200)
    private String homeAddress;
    @Column(name = "office_address", length = 200)
    private String officeAddress;
    @Column(name = "register_date", length = 45)
    private String registerDate;
    @Column(name = "register_time", length = 45)
    private String registerTime;
    @Column(name = "notification_key", length = 200)
    private String notificationKey;
    @Column(name = "current_lat", length = 50)
    private String currentLat;
    @Column(name = "current_lon", length = 50)
    private String currentLon;
    @Column(name = "last_otp_date", length = 50)
    private String lastOtpDate;
    @Column(name = "last_otp_time", length = 50)
    private String lastOtpTime;
    @Column(name = "status")
    private int status = 0;
}