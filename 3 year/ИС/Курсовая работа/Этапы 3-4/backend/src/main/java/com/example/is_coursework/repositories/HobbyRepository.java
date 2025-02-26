package com.example.is_coursework.repositories;


import com.example.is_coursework.models.BodyType;
import com.example.is_coursework.models.Hobby;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HobbyRepository extends JpaRepository<Hobby, Long> {
    @Query("SELECT bt FROM Hobby bt WHERE bt.level = :level")
    List<Hobby> findByLevel(@Param("level") int level);
}

