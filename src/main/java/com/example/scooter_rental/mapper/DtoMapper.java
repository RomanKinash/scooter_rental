package com.example.scooter_rental.mapper;

public interface DtoMapper<D, R, T> extends RequestDtoMapper<D, T>, ResponseDtoMapper<T, R> {
}
