package com.example.scooter_rental.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "scooters")
public class Scooter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String model;
    @Column(nullable = false)
    private String brand;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScooterType type;
    private int inventory;
    @Column(nullable = false)
    private BigDecimal dailyFee;

    public enum ScooterType {
        ELECTRIC,
        GAS_POWERED,
        FOLDABLE,
        OFF_ROAD
    }
}
