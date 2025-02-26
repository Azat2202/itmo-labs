package com.example.is_coursework.services;

import com.example.is_coursework.messages.RoomMessage;
import com.example.is_coursework.repositories.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;

    public List<RoomMessage> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(room -> modelMapper.map(room, RoomMessage.class))
                .toList();
    }
}
