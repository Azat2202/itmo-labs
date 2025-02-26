package com.example.is_coursework.repositories;

import com.example.is_coursework.models.Poll;
import com.example.is_coursework.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PollRepository extends JpaRepository<Poll, Long> {
    Optional<Poll> findFirstByIsOpenIsTrueAndRoom(Room room);
    Optional<Poll> findFirstByRoomOrderByCreationTime(Room room);

    List<Poll> findAllByRoom(Room room);

    List<Poll> findAllByIsOpenIsTrue();
}
