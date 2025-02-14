package com.fatima.payment_service.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class QRPaymentRequestDTO {
    private Long userId;
    private Integer source;
    private Integer destination;
    private LocalDateTime issueTime;
}


