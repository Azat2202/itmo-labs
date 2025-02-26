package com.example.is_coursework.services;

import com.example.is_coursework.messages.RoomMessage;
import com.example.is_coursework.models.Character;
import com.example.is_coursework.models.Room;
import com.example.is_coursework.models.User;
import com.example.is_coursework.repositories.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class GameService {
    @Value("${game.settings.min_user_to_start}")
    private int minUserToStart;

    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;

    public RoomMessage startGame(User user, Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new NotFoundException("Room not found"));
        if (!room.getAdmin().equals(user)) {
            throw new AccessDeniedException("You are not admin of this room");
        }
        if (room.getIsStarted())
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "The game has already started!");
        if (room.getCharacters().size() < minUserToStart) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Not enough users to start game");
        }
        if (room.getCharacters().stream().anyMatch(Predicate.not(Character::validateChoosable))) {
            throw new HttpClientErrorException(HttpStatus.PRECONDITION_FAILED, "Some characters didnt choose all fields");
        }
        room.setIsStarted(true);
        roomRepository.save(room);

        return modelMapper.map(room, RoomMessage.class);
    }

    public List<RoomMessage> getGameHistory(User user){
        List<Room> rooms = roomRepository.findAll()
                .stream()
                .filter(room -> room.getCharacters().stream().anyMatch(character -> character.getUser().equals(user)))
                .toList();
        return rooms.stream().map(room -> modelMapper.map(room, RoomMessage.class)).toList();
    }
}
