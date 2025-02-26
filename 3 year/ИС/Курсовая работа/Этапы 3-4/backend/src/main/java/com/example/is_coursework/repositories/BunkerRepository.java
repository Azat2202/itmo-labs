package com.example.is_coursework.repositories;

import com.example.is_coursework.models.Bunker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BunkerRepository extends JpaRepository<Bunker, Long> {
}
