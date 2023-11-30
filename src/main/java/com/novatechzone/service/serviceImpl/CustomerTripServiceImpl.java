package com.novatechzone.service.serviceImpl;

import com.novatechzone.dto.*;
import com.novatechzone.model.Customer;
import com.novatechzone.model.CustomerTrip;
import com.novatechzone.model.RiderRating;
import com.novatechzone.repository.CustomerRepository;
import com.novatechzone.repository.CustomerTripRepository;
import com.novatechzone.repository.RateRiderRepository;
import com.novatechzone.repository.VehicleTypeRepository;
import com.novatechzone.service.CustomerNotificationService;
import com.novatechzone.service.CustomerTripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerTripServiceImpl implements CustomerTripService {
    @Autowired
    VehicleTypeRepository vehicleTypeRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    CustomerTripRepository customerTripRepository;
    @Autowired
    CustomerNotificationService customerNotificationService;
    @Autowired
    RateRiderRepository rateRiderRepository;
    @Autowired
    RequestMetaDTO requestMetaDTO;

    @Override
    public ResponseEntity<?> requestNewTripByCustomer(NewTripRequestDTO newTripRequestDTO) {
        if (newTripRequestDTO.getVerification().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Verification Not Found!");
        } else if (String.valueOf(newTripRequestDTO.getVehicleTypeId()).equals("0")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle Type Id Not Found!");
        } else if (newTripRequestDTO.getStartLat().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Start Lat Not Found!");
        } else if (newTripRequestDTO.getStartLon().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Start Lon Not Found!");
        } else if (newTripRequestDTO.getEndLat().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("End Lat Not Found!");
        } else if (newTripRequestDTO.getEndLon().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("End Lon Not Found!");
        } else if (newTripRequestDTO.getCustomerNote().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer Note Not Found!");
        } else if (String.valueOf(newTripRequestDTO.getDistance()).equals("0")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Distance Not Found!");
        } else if (newTripRequestDTO.getStartPoint().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Start Point Not Found!");
        } else if (newTripRequestDTO.getEndPoint().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("End Point Not Found!");
        } else if (vehicleTypeRepository.findById(newTripRequestDTO.getVehicleTypeId()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Vehicle Type Id Invalid!");
        } else {

            Optional<Customer> optionalCustomer = customerRepository.findById(requestMetaDTO.getCustomerId());
            if (optionalCustomer.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer Id Invalid!");
            }

            Customer customer = optionalCustomer.get();
            if (!customer.getVerification().equals(newTripRequestDTO.getVerification())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Verification!");
            }

            CustomerTrip customerTrip = new CustomerTrip();
            customerTrip.setCustomerId(customer.getCustomerId());
            customerTrip.setVehicleTypeId(newTripRequestDTO.getVehicleTypeId());
            customerTrip.setStartPoint(newTripRequestDTO.getStartPoint());
            customerTrip.setEndPoint(newTripRequestDTO.getEndPoint());
            customerTrip.setStartLat(newTripRequestDTO.getStartLat());
            customerTrip.setStartLon(newTripRequestDTO.getStartLon());
            customerTrip.setEndLat(newTripRequestDTO.getEndLat());
            customerTrip.setEndLon(newTripRequestDTO.getEndLon());
            customerTrip.setCustomerNote(newTripRequestDTO.getCustomerNote());
            customerTrip.setTripDistance(newTripRequestDTO.getDistance());
            customerTrip.setTripDate(String.valueOf(LocalDate.now()));
            customerTrip.setTripTime(String.valueOf(LocalTime.now()));

            customerTripRepository.save(customerTrip);

            customerNotificationService.saveNotification(new CustomerNotificationDTO(
                    requestMetaDTO.getCustomerId(),
                    "Looking for Riders for a New Trip!",
                    "New Trip Request",
                    1));

            return ResponseEntity.status(HttpStatus.OK).body("Success!");
        }
    }

    @Override
    public ResponseEntity<?> customerRejectTrip(int tripId, CustomerRejectTripDTO customerRejectTripDTO) {
        if (String.valueOf(tripId).equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trip Id Not Found!");
        } else if (customerRejectTripDTO.getVerification().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Verification Not Found!");
        } else if (customerRejectTripDTO.getReason().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reason Not Found!");
        } else if (customerTripRepository.findById(tripId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid Trip Id!");
        } else {
            Customer customer = customerRepository.findById(requestMetaDTO.getCustomerId()).get();
            if (!customer.getVerification().equals(customerRejectTripDTO.getVerification())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Verification!");
            }

            CustomerTrip customerTrip = customerTripRepository.findById(tripId).get();
            customerTrip.setRejectReason(customerRejectTripDTO.getReason());
            customerTrip.setRejectDate(String.valueOf(LocalDate.now()));
            customerTrip.setRejectTime(String.valueOf(LocalTime.now()));
            customerTrip.setRejectUserType(1);
            customerTrip.setTripStatus(6);

            customerTripRepository.save(customerTrip);

            customerNotificationService.saveNotification(new CustomerNotificationDTO(
                    requestMetaDTO.getCustomerId(),
                    "You Cancelled Trip (Trip ID:" + customerTrip.getCustomerTripId() + ")!",
                    "Trip Cancelled by Passenger",
                    1));

            return ResponseEntity.status(HttpStatus.OK).body("Success!");
        }
    }

    @Override
    public ResponseEntity<?> checkCustomerTripAcceptedByRider(int tripId, VerificationCodeDTO verificationCodeDTO) {
        if (customerRepository.findById(requestMetaDTO.getCustomerId()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid Customer Id!");
        } else if (String.valueOf(tripId).equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trip Id Not Found!");
        } else if (customerTripRepository.findById(tripId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid Trip Id!");
        } else if (verificationCodeDTO.getVerification().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Verification Not Found!");
        } else if (!customerRepository.findById(requestMetaDTO.getCustomerId()).get().getVerification().equals(verificationCodeDTO.getVerification())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Verification!");
        } else {

            CustomerTrip customerTrip = customerTripRepository.findById(tripId).get();

            if (String.valueOf(customerTrip.getTripStatus()).equals("0")) {
                customerNotificationService.saveNotification(new CustomerNotificationDTO(
                        requestMetaDTO.getCustomerId(),
                        "Your Trip is Pending (Trip ID:" + customerTrip.getCustomerTripId() + ")!",
                        "Trip Pending",
                        1));
                return ResponseEntity.status(HttpStatus.OK).body("Pending!");
            } else if (String.valueOf(customerTrip.getTripStatus()).equals("1")) {
                customerNotificationService.saveNotification(new CustomerNotificationDTO(
                        requestMetaDTO.getCustomerId(),
                        "Looking a Riders for a New Trip (Trip ID:" + customerTrip.getCustomerTripId() + ")!",
                        "Search for Rider!",
                        1));
                return ResponseEntity.status(HttpStatus.OK).body("Search for Rider!");
            } else if (String.valueOf(customerTrip.getTripStatus()).equals("2")) {
                customerNotificationService.saveNotification(new CustomerNotificationDTO(
                        requestMetaDTO.getCustomerId(),
                        "Trip Accepted by a Rider (Trip ID:" + customerTrip.getCustomerTripId() + ")!",
                        "Search for Rider",
                        1));
                return ResponseEntity.status(HttpStatus.OK).body("Rider Assigned!");
            } else if (String.valueOf(customerTrip.getTripStatus()).equals("3")) {
                customerNotificationService.saveNotification(new CustomerNotificationDTO(
                        requestMetaDTO.getCustomerId(),
                        "Trip Started Successfully (Trip ID:" + customerTrip.getCustomerTripId() + ")!",
                        "Trip Start",
                        1));
                return ResponseEntity.status(HttpStatus.OK).body("Trip Start!");
            } else if (String.valueOf(customerTrip.getTripStatus()).equals("4")) {
                customerNotificationService.saveNotification(new CustomerNotificationDTO(
                        requestMetaDTO.getCustomerId(),
                        "Trip Finished Successfully (Trip ID:" + customerTrip.getCustomerTripId() + ")!",
                        "Trip Finished",
                        1));
                return ResponseEntity.status(HttpStatus.OK).body("Trip End!");
            } else if (String.valueOf(customerTrip.getTripStatus()).equals("5")) {
                customerNotificationService.saveNotification(new CustomerNotificationDTO(
                        requestMetaDTO.getCustomerId(),
                        "Trip Cancelled by Rider (Trip ID:" + customerTrip.getCustomerTripId() + ")!",
                        "Trip Cancelled by Rider",
                        1));
                return ResponseEntity.status(HttpStatus.OK).body("Trip Cancel By Rider!");
            } else if (String.valueOf(customerTrip.getTripStatus()).equals("6")) {
                customerNotificationService.saveNotification(new CustomerNotificationDTO(
                        requestMetaDTO.getCustomerId(),
                        "You Cancelled Trip (Trip ID:" + customerTrip.getCustomerTripId() + ")!",
                        "Trip Cancelled by Passenger",
                        1));
                return ResponseEntity.status(HttpStatus.OK).body("Trip Cancel By Customer!");
            } else if (String.valueOf(customerTrip.getTripStatus()).equals("7")) {
                customerNotificationService.saveNotification(new CustomerNotificationDTO(
                        requestMetaDTO.getCustomerId(),
                        "All Riders are Busy for the Newly Requested  (Trip ID:" + customerTrip.getCustomerTripId() + ")!",
                        "Rider Not Found for Trip!",
                        1));
                return ResponseEntity.status(HttpStatus.OK).body("Rider Not Found for Trip!");
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went Wrong!");

        }
    }

    @Override
    public ResponseEntity<?> getTripDataCustomerById(int tripId, VerificationCodeDTO verificationCodeDTO) {
        if (customerRepository.findById(requestMetaDTO.getCustomerId()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid Customer Id!");
        } else if (String.valueOf(tripId).equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trip Id Not Found!");
        } else if (customerTripRepository.findById(tripId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid Trip Id!");
        } else if (verificationCodeDTO.getVerification().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Verification Not Found!");
        } else if (!customerRepository.findById(requestMetaDTO.getCustomerId()).get().getVerification().equals(verificationCodeDTO.getVerification())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Verification!");
        } else {

            CustomerTrip customerTrip = customerTripRepository.findById(tripId).get();
            return ResponseEntity.status(HttpStatus.OK).body(customerTrip);

        }
    }

    @Override
    public ResponseEntity<?> rateRider(int tripId, RateRiderDTO rateRiderDTO) {
        if (customerRepository.findById(requestMetaDTO.getCustomerId()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid Customer Id!");
        } else if (String.valueOf(tripId).equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trip Id Not Found!");
        } else if (customerTripRepository.findById(tripId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid Trip Id!");
        } else if (rateRiderDTO.getVerification().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Verification Not Found!");
        } else if (!customerRepository.findById(requestMetaDTO.getCustomerId()).get().getVerification().equals(rateRiderDTO.getVerification())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Verification!");
        } else if (rateRiderDTO.getStarRate().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rating Not Found!");
        } else if (rateRiderDTO.getComment().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comments Not Found!");
        } else {

            RiderRating riderRating = new RiderRating();
            riderRating.setCustomerTripId(requestMetaDTO.getCustomerId());
            riderRating.setDate(String.valueOf(LocalDate.now()));
            riderRating.setTime(String.valueOf(LocalTime.now()));
            riderRating.setStarRate(rateRiderDTO.getStarRate());
            riderRating.setComment(rateRiderDTO.getComment());

            rateRiderRepository.save(riderRating);

            customerNotificationService.saveNotification(new CustomerNotificationDTO(
                    requestMetaDTO.getCustomerId(),
                    "You Rate the Rider (Trip ID:" + tripId + ")!",
                    "Rate Rider!",
                    1));

            return ResponseEntity.status(HttpStatus.OK).body("Success!");
        }
    }

    @Override
    public ResponseEntity<?> getTripHistoryByCustomer(RequestTripHistoryDTO requestTripHistoryDTO) {
        if (customerRepository.findById(requestMetaDTO.getCustomerId()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid Customer Id!");
        } else if (requestTripHistoryDTO.getVerification().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Verification Not Found!");
        } else if (!customerRepository.findById(requestMetaDTO.getCustomerId()).get().getVerification().equals(requestTripHistoryDTO.getVerification())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Verification!");
        } else {

            if (requestTripHistoryDTO.getDate().equals("")) {
                List<CustomerTrip> todayTrips = customerTripRepository.findAllByCustomerIdAndTripDate(requestMetaDTO.getCustomerId(), String.valueOf(LocalDate.now()));
                return ResponseEntity.status(HttpStatus.OK).body(todayTrips);
            } else {
                String trpDateString = requestTripHistoryDTO.getDate();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                List<CustomerTrip> customerTrips = new ArrayList<>();
                try {
                    Date date = dateFormat.parse(trpDateString);
                    List<CustomerTrip> allTrips = customerTripRepository.findAllByCustomerId(requestMetaDTO.getCustomerId());
                    allTrips.forEach(trip -> {
                        try {
                            Date tripDate = dateFormat.parse(trip.getTripDate());
                            if (tripDate.equals(date) || tripDate.after(date)) {
                                customerTrips.add(trip);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return ResponseEntity.status(HttpStatus.OK).body(customerTrips);
            }

        }
    }

//    @Override
//    public ResponseEntity<?> getCustomerRecentVisitedDestination(VerificationCodeDTO verificationCodeDTO) {
//        if (customerRepository.findById(requestMetaDTO.getCustomerId()).isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid Customer Id!");
//        } else if (verificationCodeDTO.getVerification().equals("")) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Verification Not Found!");
//        } else if (!customerRepository.findById(requestMetaDTO.getCustomerId()).get().getVerification().equals(verificationCodeDTO.getVerification())) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Verification!");
//        } else {
//            List<CustomerTrip> trips = customerTripRepository.findAllByCustomerId(requestMetaDTO.getCustomerId());
//            trips.forEach(trip -> {
//
//            });
//        }
//    }
}
