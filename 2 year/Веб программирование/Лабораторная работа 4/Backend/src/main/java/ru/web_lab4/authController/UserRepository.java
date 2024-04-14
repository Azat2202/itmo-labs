package ru.web_lab4.authController;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<ApplicationUser, Long> {
    ApplicationUser findApplicationUserByLogin(@NonNull String login);
}
