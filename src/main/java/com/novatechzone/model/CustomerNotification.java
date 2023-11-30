package com.novatechzone.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer_notification")
public class CustomerNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_notification_id")
    private int customerNotificationId;
    @Column(name = "customer_id")
    private int customerId;
    @Column(name = "notification_topic", length = 850)
    private String notificationTopic;
    @Column(name = "notification", columnDefinition = "TEXT")
    private String notification;
    @Column(name = "date", length = 45)
    private String date;
    @Column(name = "time", length = 45)
    private String time;
    @Column(name = "show_to_user")
    private int showToUser = 0;
    @Column(name = "status")
    private int status = 1;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_Id", updatable = false, insertable = false)
    private Customer customer;
}