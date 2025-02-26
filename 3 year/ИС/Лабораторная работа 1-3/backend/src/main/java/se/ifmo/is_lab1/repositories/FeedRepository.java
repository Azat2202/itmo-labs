package se.ifmo.is_lab1.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.ifmo.is_lab1.models.Feed;
import se.ifmo.is_lab1.models.User;

import java.util.List;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
    Page<Feed> findAllByUser(User user, Pageable pageable);
}
