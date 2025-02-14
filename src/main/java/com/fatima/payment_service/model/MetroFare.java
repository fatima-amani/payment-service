package com.fatima.payment_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "metro_fare")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MetroFare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "source_id", nullable = false)
    private MetroStation sourceStation;

    @ManyToOne
    @JoinColumn(name = "destination_id", nullable = false)
    private MetroStation destinationStation;

    @Column(nullable = false)
    private int fare;
}

