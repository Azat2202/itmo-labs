package se.ifmo.is_lab1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import se.ifmo.is_lab1.models.AdminProposal;

public interface AdminProposalRepository extends JpaRepository<AdminProposal, Long> {
    public Boolean existsByUserId(Long userId);
    public void deleteAllByUserId(Long userId);
}
