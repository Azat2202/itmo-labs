package se.ifmo.is_lab1.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import se.ifmo.is_lab1.messages.authentication.UserResponse;
import se.ifmo.is_lab1.models.User;

@Service
@RequiredArgsConstructor
public class UserService {
    private final ModelMapper modelMapper;

    public UserResponse me(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return modelMapper.map(user, UserResponse.class);
    }
}
