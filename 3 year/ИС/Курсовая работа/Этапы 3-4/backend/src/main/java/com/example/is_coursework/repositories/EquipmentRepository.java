package com.example.is_coursework.repositories;

import com.example.is_coursework.models.BodyType;
import com.example.is_coursework.models.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    @Query("SELECT bt FROM Equipment bt WHERE bt.level = :level")
    List<Equipment> findByLevel(@Param("level") int level);
}
