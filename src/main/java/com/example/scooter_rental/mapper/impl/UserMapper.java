package com.example.scooter_rental.mapper.impl;

import com.example.scooter_rental.dto.request.UserRequestDto;
import com.example.scooter_rental.dto.response.UserResponseDto;
import com.example.scooter_rental.mapper.DtoMapper;
import com.example.scooter_rental.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper implements DtoMapper<UserRequestDto, UserResponseDto, User> {

    @Override
    public User mapToModel(UserRequestDto dto) {
        User user = new User();
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        return user;
    }

    @Override
    public UserResponseDto mapToDto(User model) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(model.getId());
        dto.setEmail(model.getEmail());
        dto.setFirstName(model.getFirstName());
        dto.setLastName(model.getLastName());
        dto.setRole(String.valueOf(model.getRole()));
        return dto;
    }
}
