package com.fatima.payment_service.controller;

import com.fatima.payment_service.dto.MetroCardPaymentRequestDTO;
import com.fatima.payment_service.dto.QRPaymentRequestDTO;
import com.fatima.payment_service.model.Payment;
import com.fatima.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/pay/qr")
    public Payment makeQRPayment(@RequestBody QRPaymentRequestDTO request) {
        return paymentService.processQRPayment(request.getUserId(), request.getSource(), request.getDestination(), request.getIssueTime());
    }

    @PostMapping("/pay/metro-card")
    public Payment makeMetroCardPayment(@RequestBody MetroCardPaymentRequestDTO request) {
        return paymentService.processMetroCardPayment(request.getUserId(), request.getSource(), request.getDestination(), request.getCheckInTime(), request.getCheckOutTime());
    }

    @GetMapping("/payment-history/{userId}")
    public List<Payment> getPaymentHistory(@PathVariable Long userId) {
        return paymentService.getPaymentHistory(userId);
    }
}
