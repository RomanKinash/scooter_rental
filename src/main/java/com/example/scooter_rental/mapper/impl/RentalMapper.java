package com.example.scooter_rental.mapper.impl;

import com.example.scooter_rental.dto.request.RentalRequestDto;
import com.example.scooter_rental.dto.response.RentalResponseDto;
import com.example.scooter_rental.mapper.DtoMapper;
import com.example.scooter_rental.model.Scooter;
import com.example.scooter_rental.model.Rental;
import com.example.scooter_rental.model.User;
import org.springframework.stereotype.Component;

@Component
public class RentalMapper implements DtoMapper<RentalRequestDto, RentalResponseDto, Rental> {

    @Override
    public Rental mapToModel(RentalRequestDto dto) {
        Scooter scooter = new Scooter();
        scooter.setId(dto.getScooterId());
        Rental rental = new Rental();
        User user = new User();
        user.setId(dto.getUserId());

        rental.setScooter(scooter);
        rental.setReturnDate(dto.getReturnDate());
        rental.setRentalDate(dto.getRentalDate());
        rental.setUser(user);
        return rental;
    }

    @Override
    public RentalResponseDto mapToDto(Rental rental) {
        RentalResponseDto dto = new RentalResponseDto();
        dto.setId(rental.getId());
        dto.setRentalDate(rental.getRentalDate());
        dto.setActualReturnDate(rental.getActualReturnDate());
        dto.setReturnDate(rental.getReturnDate());
        dto.setScooterId(rental.getScooter().getId());
        dto.setUserId(rental.getUser().getId());
        return dto;
    }
}
