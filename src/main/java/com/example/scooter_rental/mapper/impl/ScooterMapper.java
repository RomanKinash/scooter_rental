
package com.example.scooter_rental.mapper.impl;

import com.example.scooter_rental.dto.request.ScooterRequestDto;
import com.example.scooter_rental.dto.response.ScooterResponseDto;
import com.example.scooter_rental.mapper.DtoMapper;
import com.example.scooter_rental.model.Scooter;
import org.springframework.stereotype.Component;

@Component
public class ScooterMapper implements DtoMapper<ScooterRequestDto, ScooterResponseDto, Scooter> {
    @Override
    public Scooter mapToModel(ScooterRequestDto dto) {
        Scooter scooter = new Scooter();
        scooter.setModel(dto.getModel());
        scooter.setType(Scooter.ScooterType.valueOf((dto.getType().name())));
        scooter.setInventory(dto.getInventory());
        scooter.setBrand(dto.getBrand());
        scooter.setDailyFee(dto.getDailyFee());
        return scooter;
    }

    @Override
    public ScooterResponseDto mapToDto(Scooter scooter) {
        ScooterResponseDto dto = new ScooterResponseDto();
        dto.setId(scooter.getId());
        dto.setInventory(scooter.getInventory());
        dto.setBrand(scooter.getBrand());
        dto.setModel(scooter.getModel());
        dto.setType(scooter.getType());
        dto.setDailyFee(scooter.getDailyFee());
        return dto;
    }
}
