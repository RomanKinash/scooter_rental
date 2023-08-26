package com.example.scooter_rental.controller;

import com.example.scooter_rental.dto.request.UserRequestDto;
import com.example.scooter_rental.dto.response.UserResponseDto;
import com.example.scooter_rental.mapper.DtoMapper;
import com.example.scooter_rental.model.User;
import com.example.scooter_rental.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
@Tag(name = "User", description = "The User API. "
        + "Describes the complete set of operations that can be performed on user.")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final DtoMapper<UserRequestDto, UserResponseDto, User> userMapper;

    @PutMapping("/{id}/role")
    @Operation(summary = "Update user role by user id",
            description = "The method updates the user role by its unique identifier. "
                    + "It checks the user's current role and changes it to the opposite "
                    + "(if the user is \"USER\", then the role becomes \"ADMIN\", "
                    + "and vice versa). The result of the method is a UserResponseDto "
                    + "object with updated information about the user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = User.class),
                            mediaType = "application/json")})
    })
    public UserResponseDto update(@Parameter(
            description = "Press the button \"Try it out\" and put user id to update")
                                      @PathVariable Long id) {
        User user = userService.get(id);
        return user.getRole() == User.Role.USER
                ? userMapper.mapToDto(userService.update(id, User.Role.ADMIN))
                : userMapper.mapToDto(userService.update(id, User.Role.USER));
    }

    @GetMapping("/me")
    @Operation(summary = "Get user profile info",
            description = "The method retrieves the profile information of the "
                    + "currently authenticated user. It uses the email from the "
                    + "authentication object to fetch the corresponding user "
                    + "from the database and returns a UserResponseDto containing "
                    + "the user's profile details.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = User.class),
                            mediaType = "application/json")})
    })
    public UserResponseDto get(Authentication authentication) {
        return userMapper.mapToDto(userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("We couldn't find a user with the email address: " + authentication.getName())));
    }

    @PutMapping("/me")
    @Operation(summary = "Update user profile info",
            description = "The method allows the currently authenticated user to update "
                    + "their profile information. It takes a UserRequestDto containing "
                    + "the updated profile data and updates the relevant fields in the user "
                    + "entity. The updated user profile is then returned as a UserResponseDto.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = User.class),
                            mediaType = "application/json")})
    })
    public UserResponseDto updateProfile(Authentication authentication,
                                         @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                 description = "Here you need to specify "
                                                         + "the fields that need to be "
                                                         + "updated in json format")
                                         @RequestBody UserRequestDto userRequestDto) {
        User userDB = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("We couldn't find a user with the email address: " + authentication.getName()));
        User userUpdate = userMapper.mapToModel(userRequestDto);
        userUpdate.setId(userDB.getId());
        userUpdate.setEmail(userUpdate.getEmail() == null
                ? userDB.getEmail() : userUpdate.getEmail());
        userUpdate.setFirstName(userUpdate.getFirstName() == null
                ? userDB.getFirstName() : userUpdate.getFirstName());
        userUpdate.setLastName(userUpdate.getLastName() == null
                ? userDB.getLastName() : userUpdate.getLastName());
        userUpdate.setPassword(userUpdate.getPassword() == null
                ? userDB.getPassword() : passwordEncoder.encode(userUpdate.getPassword()));
        userUpdate.setRole(userUpdate.getRole() == null
                ? userDB.getRole() : userUpdate.getRole());
        return userMapper.mapToDto(userService.update(userUpdate));
    }
}
