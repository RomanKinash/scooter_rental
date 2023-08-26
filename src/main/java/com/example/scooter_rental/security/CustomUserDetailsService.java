package com.example.scooter_rental.security;

import com.example.scooter_rental.model.User;
import com.example.scooter_rental.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static org.springframework.security.core.userdetails.User.withUsername;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("We couldn't find a user with the email address: " + username));
        UserBuilder builder = withUsername(username);
        builder.password(user.getPassword());
        builder.roles(user.getRole().name());
        return builder.build();
    }
}
