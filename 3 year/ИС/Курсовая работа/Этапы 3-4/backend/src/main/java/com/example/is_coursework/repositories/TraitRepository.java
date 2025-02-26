package com.example.is_coursework.repositories;

import com.example.is_coursework.models.BodyType;
import com.example.is_coursework.models.Trait;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TraitRepository extends JpaRepository<Trait, Long> {
    @Query("SELECT bt FROM Trait bt WHERE bt.level = :level")
    List<Trait> findByLevel(@Param("level") int level);
}
