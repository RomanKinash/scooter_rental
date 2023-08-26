package com.example.scooter_rental.mapper;

public interface ResponseDtoMapper<T, D> {
    D mapToDto(T t);
}
