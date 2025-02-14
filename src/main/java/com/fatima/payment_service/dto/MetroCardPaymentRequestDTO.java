package com.fatima.payment_service.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MetroCardPaymentRequestDTO {
    private Long userId;
    private Integer source;
    private Integer destination;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
}

