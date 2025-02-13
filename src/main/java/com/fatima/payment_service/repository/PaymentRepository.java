package com.fatima.payment_service.repository;

import com.fatima.payment_service.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findTop5ByUserIdOrderByPaymentTimeDesc(Long userId);
}
