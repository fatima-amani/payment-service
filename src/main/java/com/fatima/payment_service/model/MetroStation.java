package com.fatima.payment_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "metro_stations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MetroStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}
