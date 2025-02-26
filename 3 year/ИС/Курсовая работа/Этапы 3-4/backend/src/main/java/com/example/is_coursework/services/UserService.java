package com.example.is_coursework.services;

import com.example.is_coursework.models.User;
import com.example.is_coursework.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUserFromAuthentication(Authentication authentication) {
        String login = authentication.getName();
        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
        String name = jwt.getClaim("name");
        Optional<User> user = userRepository.findUserByLogin(login);
        return user.orElseGet(() ->
                userRepository.saveAndFlush(
                        User.builder()
                                .login(login)
                                .username(name)
                                .build()));
    }
}