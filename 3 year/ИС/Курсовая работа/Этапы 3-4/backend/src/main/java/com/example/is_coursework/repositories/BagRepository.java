package com.example.is_coursework.repositories;

import com.example.is_coursework.models.Bag;
import com.example.is_coursework.models.BodyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BagRepository extends JpaRepository<Bag, Long> {
    @Query("SELECT bt FROM Bag bt WHERE bt.level = :level")
    List<Bag> findByLevel(@Param("level") int level);
}


