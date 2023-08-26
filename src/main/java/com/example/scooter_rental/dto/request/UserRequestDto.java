package com.example.scooter_rental.dto.request;

import com.example.scooter_rental.lib.Email;
import com.example.scooter_rental.lib.Password;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@Password(
        field = "password",
        fieldMatch = "repeatPassword",
        message = "Passwords do not match!"
)
public class UserRequestDto {
    @Email
    private String email;
    private String firstName;
    private String lastName;
    @Size(min = 8, max = 40)
    private String password;
    private String repeatPassword;
}
