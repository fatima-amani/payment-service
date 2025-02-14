package com.fatima.payment_service.service;

import com.fatima.payment_service.event.PaymentSuccessEvent;
import com.fatima.payment_service.event.PenaltyChargedEvent;
import com.fatima.payment_service.model.Payment;
import com.fatima.payment_service.model.Penalty;
import com.fatima.payment_service.model.MetroFare;
import com.fatima.payment_service.repository.PaymentRepository;
import com.fatima.payment_service.repository.PenaltyRepository;
import com.fatima.payment_service.repository.MetroFareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PenaltyRepository penaltyRepository;
    private final MetroFareRepository metroFareRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final double PEAK_HOUR_INCREASE = 0.20;
    private static final double METRO_CARD_DISCOUNT = 0.1;
    private static final int PENALTY_TIME_LIMIT = 90; // in minutes

    public Payment processQRPayment(Long userId, Integer sourceId, Integer destinationId, LocalDateTime issueTime) {
        double finalFare = isPeakHour(issueTime) ? getPeakHourFare(sourceId, destinationId) : getBaseFare(sourceId, destinationId);

        Payment payment = new Payment();
        payment.setUserId(userId);
        payment.setPaymentMethod("QR_TICKET");
        payment.setAmount(finalFare);
        payment.setSource(sourceId);
        payment.setDestination(destinationId);
        payment.setPaymentTime(LocalDateTime.now()); // Ensure the time is stored correctly

        Payment savedPayment = paymentRepository.save(payment); // Persist to DB

        kafkaTemplate.send("ticket_payment_success", new PaymentSuccessEvent(userId, finalFare, "QR_TICKET", sourceId, destinationId));
        return savedPayment; // Return the saved payment (with ID)
    }

    public Payment processMetroCardPayment(Long userId, Integer sourceId, Integer destinationId, LocalDateTime checkInTime, LocalDateTime checkOutTime) {
        double finalFare = isPeakHour(checkInTime) ? getPeakHourFare(sourceId, destinationId) : getBaseFare(sourceId, destinationId);

        if (Duration.between(checkInTime, checkOutTime).toMinutes() > PENALTY_TIME_LIMIT) {
            applyPenalty(userId, finalFare * 0.1);
        }

        finalFare -= finalFare * METRO_CARD_DISCOUNT; // Apply discount for metro card users

        Payment payment = new Payment();
        payment.setUserId(userId);
        payment.setPaymentMethod("METRO_CARD");
        payment.setAmount(finalFare);
        payment.setSource(sourceId);
        payment.setDestination(destinationId);
        payment.setPaymentTime(LocalDateTime.now()); // Store the payment time

        Payment savedPayment = paymentRepository.save(payment); // Persist to DB

        kafkaTemplate.send("ticket_payment_success", new PaymentSuccessEvent(userId, finalFare, "METRO_CARD", sourceId, destinationId));
        return savedPayment;
    }


    public void applyPenalty(Long userId, double penaltyAmount) {
        Penalty penalty = new Penalty(null, userId, penaltyAmount, LocalDateTime.now());
        penaltyRepository.save(penalty);
        kafkaTemplate.send("penalty_charged", new PenaltyChargedEvent(userId, penaltyAmount));
    }

    @Cacheable(value = "base_fare_cache", key = "#sourceId + '-' + #destinationId")
    private int getBaseFare(Integer sourceId, Integer destinationId) {
        return metroFareRepository.findBySourceStationIdAndDestinationStationId(sourceId, destinationId)
                .map(MetroFare::getFare)
                .orElseThrow(() -> new RuntimeException("Fare not found for the given route"));
    }

    @Cacheable(value = "peak_fare_cache", key = "#sourceId + '-' + #destinationId")
    private double getPeakHourFare(Integer sourceId, Integer destinationId) {
        int baseFare = getBaseFare(sourceId, destinationId);
        return baseFare + (baseFare * PEAK_HOUR_INCREASE);
    }

    private boolean isPeakHour(LocalDateTime time) {
        LocalTime localTime = time.toLocalTime();
        return (localTime.isAfter(LocalTime.of(8, 0)) && localTime.isBefore(LocalTime.of(10, 0))) ||
                (localTime.isAfter(LocalTime.of(18, 0)) && localTime.isBefore(LocalTime.of(21, 0)));
    }

    public List<Payment> getPaymentHistory(Long userId) {
        return paymentRepository.findTop5ByUserIdOrderByPaymentTimeDesc(userId);
    }
}
