package com.example.is_coursework.repositories;

import com.example.is_coursework.models.BodyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BodyTypeRepository extends JpaRepository<BodyType, Long> {
    @Query("SELECT bt FROM BodyType bt WHERE bt.level = :level")
    List<BodyType> findByLevel(@Param("level") int level);
}
