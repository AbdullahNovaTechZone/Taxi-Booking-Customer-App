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
@Table(name = "customer_trip")
public class CustomerTrip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_trip_id")
    private int customerTripId;
    @Column(name = "customer_id")
    private int customerId;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_Id", updatable = false, insertable = false)
    private Customer customer;
    @Column(name = "vehicle_type_id")
    private int vehicleTypeId;
    @ManyToOne
    @JoinColumn(name = "vehicle_type_id", referencedColumnName = "vehicle_type_Id", updatable = false, insertable = false)
    private VehicleType VehicleType;
    @Column(name = "rider_id")
    private int riderId = 0;
    @Column(name = "start_point", length = 850)
    private String startPoint;
    @Column(name = "end_point", length = 850)
    private String endPoint;
    @Column(name = "start_lat", length = 45)
    private String startLat;
    @Column(name = "start_lon", length = 45)
    private String startLon;
    @Column(name = "end_lat", length = 45)
    private String endLat;
    @Column(name = "end_lon", length = 45)
    private String endLon;
    @Column(name = "customer_note", length = 450)
    private String customerNote;
    @Column(name = "rider_note", columnDefinition = "TEXT")
    private String riderNote;
    @Column(name = "arrived")
    private int arrived;
    @Column(name = "arrived_date", length = 45)
    private String arrivedDate;
    @Column(name = "arrived_time", length = 45)
    private String arrivedTime;
    @Column(name = "trip_date", length = 45)
    private String tripDate;
    @Column(name = "trip_time", length = 45)
    private String tripTime;
    @Column(name = "trip_accept_time", length = 45)
    private String tripAcceptTime;
    @Column(name = "trip_accept_date", length = 45)
    private String tripAcceptDate;
    @Column(name = "trip_start_date", length = 45)
    private String tripStartDate;
    @Column(name = "trip_start_time", length = 45)
    private String tripStartTime;
    @Column(name = "trip_end_date", length = 45)
    private String tripEndDate;
    @Column(name = "trip_end_time", length = 45)
    private String tripEndTime;
    @Column(name = "reject_user_type")
    private int rejectUserType;
    @Column(name = "reject_reason", length = 45)
    private String rejectReason;
    @Column(name = "reject_date", length = 45)
    private String rejectDate;
    @Column(name = "reject_time", length = 45)
    private String rejectTime;
    @Column(name = "trip_distance")
    private Double tripDistance;
    @Column(name = "km_rate")
    private Double kmRate;
    @Column(name = "company_commission_rate", length = 45)
    private String companyCommissionRate;
    @Column(name = "payment_done")
    private int paymentDone;
    @Column(name = "payment_type")
    private int paymentType;
    @Column(name = "trip_charge")
    private Double tripCharge;
    @Column(name = "company_income", length = 45)
    private String companyIncome;
    @Column(name = "driver_income", length = 45)
    private String driverIncome;
    @Column(name = "trip_status")
    private int tripStatus = 1;
}