package com.example.is_coursework.repositories;

import com.example.is_coursework.models.Cataclysm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CataclysmRepository extends JpaRepository<Cataclysm, Long> {
}
