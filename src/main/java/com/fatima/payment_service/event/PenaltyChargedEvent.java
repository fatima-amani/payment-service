package com.fatima.payment_service.event;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PenaltyChargedEvent {
    private Long userId;
    private double penaltyAmount;
}

