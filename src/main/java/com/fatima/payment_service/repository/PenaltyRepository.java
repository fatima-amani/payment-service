package com.fatima.payment_service.repository;

import com.fatima.payment_service.model.Penalty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PenaltyRepository extends JpaRepository<Penalty, Long> {
}

