package com.novatechzone.service.serviceImpl;

import com.novatechzone.dto.*;
import com.novatechzone.model.Customer;
import com.novatechzone.repository.CustomerRepository;
import com.novatechzone.service.CustomerNotificationService;
import com.novatechzone.service.CustomerService;
import com.novatechzone.util.JwtUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    CustomerNotificationService customerNotificationService;
    @Autowired
    RequestMetaDTO requestMetaDTO;

    public static final String UPLOAD_DIR_NAME = "upload";

    @Override
    public ResponseEntity<?> registerAndLoginCustomer(CustomerAuthRequestDTO customerAuthRequestDTO) {
        if (customerAuthRequestDTO.getMobile().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mobile Not Found!");
        } else if (customerAuthRequestDTO.getName().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Name Not Found!");
        } else if (customerAuthRequestDTO.getNotificationKey().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notification Key Not Found!");
        } else if (customerAuthRequestDTO.getCurrentLat().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Current Lat Not Found!");
        } else if (customerAuthRequestDTO.getCurrentLon().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Current Lon Not Found!");
        } else {
            Optional<Customer> optionalCustomer = customerRepository.findByMobile1(customerAuthRequestDTO.getMobile());

            if (optionalCustomer.isEmpty()) {
                Customer customer = new Customer();
                customer.setMobile1(customerAuthRequestDTO.getMobile());
                customer.setUsername(customerAuthRequestDTO.getName());
                customer.setNotificationKey(customerAuthRequestDTO.getNotificationKey());
                customer.setCurrentLat(customerAuthRequestDTO.getCurrentLat());
                customer.setCurrentLon(customerAuthRequestDTO.getCurrentLon());

                customerRepository.save(customer);

                customerNotificationService.saveNotification(new CustomerNotificationDTO(
                        customerRepository.findByMobile1(customerAuthRequestDTO.getMobile()).get().getCustomerId(),
                        "Account Created Successfully!",
                        "New Customer Registration",
                        0));

            } else if (String.valueOf(optionalCustomer.get().getStatus()).equals("0")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account Blacklisted. Cannot Login to Account!");
            }

            customerNotificationService.saveNotification(new CustomerNotificationDTO(
                    customerRepository.findByMobile1(customerAuthRequestDTO.getMobile()).get().getCustomerId(),
                    "Logging Success!",
                    "Customer Login",
                    1));

            return ResponseEntity.status(HttpStatus.OK).body("Success");
        }
    }

    @Override
    public ResponseEntity<?> getCustomerPinNumber(CustomerMobileDTO customerMobileDTO) {
        if (customerMobileDTO.getMobile().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mobile Not Found!");
        } else {
            Optional<Customer> optionalCustomer = customerRepository.findByMobile1(customerMobileDTO.getMobile());
            if (optionalCustomer.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Mobile!");
            } else {
                String pinNumber = String.format("%06d", new Random().nextInt(999999));
                Customer customer = optionalCustomer.get();
                customer.setVerification(pinNumber);
                customer.setLastOtpDate(String.valueOf(LocalDate.now()));
                customer.setLastOtpTime(String.valueOf(LocalTime.now()));
                customerRepository.save(customer);
                return ResponseEntity.status(HttpStatus.OK).body(pinNumber);
            }
        }
    }

    @Override
    public ResponseEntity<?> verifyCustomerMobile(VerifyRequestDTO verifyRequestDTO) {
        if (verifyRequestDTO.getMobile().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mobile Not Found!");
        } else if (verifyRequestDTO.getOtp().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("OTP Not Found!");
        } else {
            Optional<Customer> optionalCustomer = customerRepository.findByMobile1(verifyRequestDTO.getMobile());
            if (optionalCustomer.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mobile Number Not Found. Register Again!");
            } else {
                Customer customer = optionalCustomer.get();
                if (!customer.getVerification().equals(verifyRequestDTO.getOtp())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Entered OTP is Wrong, Try Again!");
                }
                customer.setStatus(1);
                customerRepository.save(customer);
                String accessToken = jwtUtils.generateAccessToken(customer);
                Map<String, String> data = new HashMap<>();
                data.put("message", "Good to Go");
                data.put("accessToken", accessToken);

                return ResponseEntity.status(HttpStatus.OK).body(data);
            }
        }
    }

    @Override
    public ResponseEntity<?> updateProfile(CustomerUpdateProfileDTO customerUpdateProfileDTO) {
        if (customerUpdateProfileDTO.getVerification().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Verification Not Found!");
        } else if (!customerRepository.findById(requestMetaDTO.getCustomerId()).get().getVerification().equals(customerUpdateProfileDTO.getVerification())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid Verification!");
        } else {

            Customer customer = customerRepository.findById(requestMetaDTO.getCustomerId()).get();
            customer.setFullName(customerUpdateProfileDTO.getFullName());
            customer.setMobile2(customerUpdateProfileDTO.getEmergencyNumber());
            customer.setBirthday(customerUpdateProfileDTO.getBirthday());
            customer.setEmail(customerUpdateProfileDTO.getEmail());
            customer.setNic(customerUpdateProfileDTO.getNic());

            customerRepository.save(customer);

            customerNotificationService.saveNotification(new CustomerNotificationDTO(
                    requestMetaDTO.getCustomerId(),
                    "Your Profile Updated Successfully!",
                    "Profile Update",
                    1));

            return ResponseEntity.status(HttpStatus.OK).body("Success");
        }
    }

    @Override
    public ResponseEntity<?> updateHomeOfficeAddress(UpdateAddressDTO updateAddressDTO) {
        if (customerRepository.findById(requestMetaDTO.getCustomerId()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid Customer Id!");
        } else if (updateAddressDTO.getVerification().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Verification Not Found!");
        } else if (!customerRepository.findById(requestMetaDTO.getCustomerId()).get().getVerification().equals(updateAddressDTO.getVerification())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Verification!");
        } else if (updateAddressDTO.getAddressText().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Address Text Not Found!");
        } else if (updateAddressDTO.getAddressLat().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Address Lat Not Found!");
        } else if (updateAddressDTO.getAddressLon().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Address Lon Not Found!");
        } else if (String.valueOf(updateAddressDTO.getAddressType()).equals("0")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Address Type Not Found!");
        } else {

            Customer customer = customerRepository.findById(requestMetaDTO.getCustomerId()).get();
            if (String.valueOf(updateAddressDTO.getAddressType()).equals("1")) {
                customer.setHomeAddress(updateAddressDTO.getAddressText());
                customer.setHomeAddressLat(updateAddressDTO.getAddressLat());
                customer.setHomeAddressLon(updateAddressDTO.getAddressLon());
            } else if (String.valueOf(updateAddressDTO.getAddressType()).equals("2")) {
                customer.setOfficeAddress(updateAddressDTO.getAddressText());
                customer.setOfficeAddressLat(updateAddressDTO.getAddressLat());
                customer.setOfficeAddressLon(updateAddressDTO.getAddressLon());
            }
            customerRepository.save(customer);

            customerNotificationService.saveNotification(new CustomerNotificationDTO(
                    requestMetaDTO.getCustomerId(),
                    "Address Updated Successfully!",
                    "Address Update",
                    1));

            return ResponseEntity.status(HttpStatus.OK).body("Success!");
        }
    }

    @Override
    public ResponseEntity<?> removeHomeOfficeAddress(RemoveAddressDTO removeAddressDTO) {
        if (customerRepository.findById(requestMetaDTO.getCustomerId()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid Customer Id!");
        } else if (removeAddressDTO.getVerification().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Verification Not Found!");
        } else if (!customerRepository.findById(requestMetaDTO.getCustomerId()).get().getVerification().equals(removeAddressDTO.getVerification())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Verification!");
        } else if (String.valueOf(removeAddressDTO.getAddressType()).equals("0")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Address Type Not Found!");
        } else {
            Customer customer = customerRepository.findById(requestMetaDTO.getCustomerId()).get();
            if (String.valueOf(removeAddressDTO.getAddressType()).equals("1")) {
                customer.setHomeAddress("");
                customer.setHomeAddressLat("");
                customer.setHomeAddressLon("");
            } else if (String.valueOf(removeAddressDTO.getAddressType()).equals("2")) {
                customer.setOfficeAddress("");
                customer.setOfficeAddressLat("");
                customer.setOfficeAddressLon("");
            }
            customerRepository.save(customer);

            customerNotificationService.saveNotification(new CustomerNotificationDTO(
                    requestMetaDTO.getCustomerId(),
                    "Address Deleted Successfully!",
                    "Address Delete",
                    1));

            return ResponseEntity.status(HttpStatus.OK).body("Address Deleted Successfully!");
        }
    }

    @Override
    public ResponseEntity<?> updateProfileImage(String verification, MultipartFile file) throws IOException {
        if (customerRepository.findById(requestMetaDTO.getCustomerId()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid Customer Id!");
        } else if (verification.equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Verification Not Found!");
        } else if (!customerRepository.findById(requestMetaDTO.getCustomerId()).get().getVerification().equals(verification)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Verification!");
        } else if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please Select a Image!");
        } else {

            Path path = Paths.get(UPLOAD_DIR_NAME);

            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }

            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String fileName = System.currentTimeMillis() + "." + extension;

            Path filePath = path.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String appUrl = String.format("http://%s:%s", InetAddress.getLocalHost().getHostName(), 8080);
            String url = UPLOAD_DIR_NAME + "/" + fileName;
            String fullUrl = appUrl + "/" + url;

            Customer customer = customerRepository.findById(requestMetaDTO.getCustomerId()).get();
            customer.setProfileImage(url);

            customerRepository.save(customer);

            customerNotificationService.saveNotification(new CustomerNotificationDTO(
                    requestMetaDTO.getCustomerId(),
                    "Profile Image Updated Successfully!",
                    "Profile Image Update",
                    1));

            return ResponseEntity.status(HttpStatus.OK).body("Success!");
        }
    }
}
