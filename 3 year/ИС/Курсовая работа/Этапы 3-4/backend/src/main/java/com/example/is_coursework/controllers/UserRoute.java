package com.example.is_coursework.controllers;

import com.example.is_coursework.interfaces.CurrentUser;
import com.example.is_coursework.models.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "Контроллер для получения данных о пользователе")
public class UserRoute {
    @GetMapping("/me")
    public User get(@CurrentUser User currentUser) {
        return currentUser;
    }
}
