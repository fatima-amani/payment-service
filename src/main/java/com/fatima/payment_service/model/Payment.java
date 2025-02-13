package com.fatima.payment_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String paymentMethod; // "METRO_CARD" or "QR_TICKET"
    private double amount;
    private String source;
    private String destination;
    private LocalDateTime paymentTime;
}

