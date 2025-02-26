package com.example.is_coursework.repositories;

import com.example.is_coursework.models.Character;
import com.example.is_coursework.models.Room;
import com.example.is_coursework.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {
    Optional<Character> findByRoomIdAndUser(Long roomId, User user);
}
