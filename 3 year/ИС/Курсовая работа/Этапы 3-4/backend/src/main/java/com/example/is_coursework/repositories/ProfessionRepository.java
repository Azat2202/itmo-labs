package com.example.is_coursework.repositories;

import com.example.is_coursework.models.BodyType;
import com.example.is_coursework.models.Profession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfessionRepository extends JpaRepository<Profession, Long> {
    @Query("SELECT bt FROM Profession bt WHERE bt.level = :level")
    List<Profession> findByLevel(@Param("level") int level);
}
