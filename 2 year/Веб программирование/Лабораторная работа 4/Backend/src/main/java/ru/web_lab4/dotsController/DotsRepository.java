package ru.web_lab4.dotsController;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DotsRepository extends JpaRepository<Dot, Long> {
    List<Dot> getDotByOwnerLogin(String owner);

    void deleteDotsByOwnerLogin(String owner);
}
