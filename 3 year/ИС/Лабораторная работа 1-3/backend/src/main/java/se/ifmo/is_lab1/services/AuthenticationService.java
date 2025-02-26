package se.ifmo.is_lab1.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.ifmo.is_lab1.dto.authentication.JwtDto;
import se.ifmo.is_lab1.dto.authentication.RegisterUserDto;
import se.ifmo.is_lab1.messages.authentication.UserResponse;
import se.ifmo.is_lab1.models.User;
import se.ifmo.is_lab1.models.enums.Role;
import se.ifmo.is_lab1.repositories.UserRepository;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final ModelMapper modelMapper;

    public UserResponse signup(final RegisterUserDto input) {
        if (userRepository.findUserByUsername(input.getUsername()).isPresent()) {
            throw new IllegalStateException("User with this username already exists.");
        }
        User user = modelMapper.map(input, User.class);
        user.setRole(Role.DEFAULT);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return modelMapper.map(userRepository.save(user), UserResponse.class);
    }

    public User authenticate(final JwtDto input) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(input.getUsername(),
                        input.getPassword()
                ));
        return userRepository.findUserByUsername(input.getUsername()).orElseThrow();
    }

}


