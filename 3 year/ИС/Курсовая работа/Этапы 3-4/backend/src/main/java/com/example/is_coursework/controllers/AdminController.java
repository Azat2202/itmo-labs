package com.example.is_coursework.controllers;


import com.example.is_coursework.messages.RoomMessage;
import com.example.is_coursework.services.AdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Controller", description = "Контроллер для админ панели")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/rooms")
    public List<RoomMessage> getAllRooms(){
        return adminService.getAllRooms();
    }
}
