package com.example.is_coursework.repositories;

import com.example.is_coursework.models.OpenedFacts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpenFactsRepository extends JpaRepository<OpenedFacts, Long> {
}
