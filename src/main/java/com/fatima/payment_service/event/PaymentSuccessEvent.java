package com.fatima.payment_service.event;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSuccessEvent {
    private Long userId;
    private double amount;
    private String paymentMethod;
    private int src;
    private int dest;
}
