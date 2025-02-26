package com.example.is_coursework.repositories;

import com.example.is_coursework.models.BodyType;
import com.example.is_coursework.models.Phobia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhobiaRepository extends JpaRepository<Phobia, Long> {
    @Query("SELECT bt FROM Phobia bt WHERE bt.level = :level")
    List<Phobia> findByLevel(@Param("level") int level);
}
