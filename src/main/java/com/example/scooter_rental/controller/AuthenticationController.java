package com.example.scooter_rental.controller;

import com.example.scooter_rental.dto.request.UserLoginDto;
import com.example.scooter_rental.dto.request.UserRegistrationDto;
import com.example.scooter_rental.dto.request.UserRequestDto;
import com.example.scooter_rental.dto.response.UserResponseDto;
import com.example.scooter_rental.exception.AuthenticationException;
import com.example.scooter_rental.mapper.DtoMapper;
import com.example.scooter_rental.model.User;
import com.example.scooter_rental.security.jwt.JwtTokenProvider;
import com.example.scooter_rental.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@Tag(name = "Authentication", description = "The Authentication API. "
        + "Describes registration of new users and authentication of already registered users")
public class AuthenticationController {
    private final AuthenticationService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final DtoMapper<UserRequestDto, UserResponseDto, User> userMapper;

    @Operation(summary = "Register new user",
            description = "This method is designed to facilitate user registration. "
                    + "It accepts a UserRegistrationDto object via the request body, which "
                    + "contains essential user registration information, including email, "
                    + "first name, last name, password, and password confirmation. The method "
                    + "proceeds by invoking the authService to carry out the user registration "
                    + "process with the provided details. Upon a successful registration, it obtains "
                    + "a User object, which it then transforms into the corresponding UserResponseDto "
                    + "using the userMapper. Finally, the method returns this UserResponseDto.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = User.class),
                            mediaType = "application/json")})
    })
    @PostMapping("/register")
    public UserResponseDto register(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Here you need to specify the fields required for user "
                    + "registration in json format")
                                        @RequestBody UserRegistrationDto request) {
        User user = authService.register(request.getEmail(), request.getFirstName(),
                request.getLastName(), request.getPassword());
        return userMapper.mapToDto(user);
    }

    @Operation(summary = "Authenticate new user",
            description = "This method is responsible for managing user login functionality. "
                    + "It expects a validated UserLoginDto object via the request body, which includes "
                    + "the user's login credentials, namely email and password. The method proceeds by "
                    + "invoking the authService to authenticate these login credentials. If the login is "
                    + "successful, it generates a JSON Web Token (JWT) using the jwtTokenProvider. This JWT "
                    + "contains the user's email and role and is returned within a ResponseEntity as the "
                    + "response body. The generated token can be utilized for subsequent authentication and "
                    + "authorization operations.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = ResponseEntity.class),
                            mediaType = "application/json")})
    })
    @PostMapping("/login")
    public ResponseEntity<Object> login(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Here you need to specify the fields required for user "
                    + "authentication in json format")
                                            @RequestBody @Valid UserLoginDto userLoginDto)
            throws AuthenticationException {
        User user = authService.login(userLoginDto.getLogin(),
                userLoginDto.getPassword());
        String token = jwtTokenProvider.createToken(user.getEmail(),
                List.of(user.getRole().name()));
        return new ResponseEntity<>(Map.of("token", token), HttpStatus.OK);
    }
}
