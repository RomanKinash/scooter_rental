package com.example.scooter_rental.dto.response;

import com.example.scooter_rental.model.Scooter;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ScooterResponseDto {
    private Long id;
    private String model;
    private String brand;
    private Scooter.ScooterType type;
    private int inventory;
    private BigDecimal dailyFee;
}
