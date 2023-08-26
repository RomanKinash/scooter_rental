package com.example.scooter_rental.dto.request;

import lombok.Data;

@Data
public class UserLoginDto {
    private String login;
    private String password;
}
