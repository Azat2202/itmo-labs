package se.ifmo.is_lab1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.ifmo.is_lab1.models.ObjectAudit;

@Repository
public interface ObjectAuditRepository extends JpaRepository<ObjectAudit, Long> {
}
