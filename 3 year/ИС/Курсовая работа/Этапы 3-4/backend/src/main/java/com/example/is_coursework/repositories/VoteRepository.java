package com.example.is_coursework.repositories;

import com.example.is_coursework.models.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
}
