package com.example.scooter_rental.mapper;

public interface RequestDtoMapper<D, T> {
    T mapToModel(D dto);
}
