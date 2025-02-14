package com.fatima.payment_service.repository;


import com.fatima.payment_service.model.MetroStation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<MetroStation, Long> {
}
