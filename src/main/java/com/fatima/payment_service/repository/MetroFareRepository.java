package com.fatima.payment_service.repository;

import com.fatima.payment_service.model.MetroFare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MetroFareRepository extends JpaRepository<MetroFare, Long> {

    @Query("SELECT mf.fare FROM MetroFare mf WHERE mf.sourceStation.id = :sourceId AND mf.destinationStation.id = :destinationId")
    Integer findFareByStationIds(@Param("sourceId") int sourceId, @Param("destinationId") int destinationId);

    Optional<MetroFare> findBySourceStationIdAndDestinationStationId(Integer sourceId, Integer destinationId);
}

