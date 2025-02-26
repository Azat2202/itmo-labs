package com.example.is_coursework.repositories;

import com.example.is_coursework.models.BodyType;
import com.example.is_coursework.models.Health;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HealthRepository extends JpaRepository<Health, Long> {
    @Query("SELECT bt FROM Health bt WHERE bt.level = :level")
    List<Health> findByLevel(@Param("level") int level);
}

