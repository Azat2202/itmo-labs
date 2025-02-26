package se.ifmo.is_lab1.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import se.ifmo.is_lab1.models.StudyGroup;
import se.ifmo.is_lab1.models.enums.FormOfEducation;
import se.ifmo.is_lab1.models.enums.Semester;

import java.util.List;

@Repository
public interface StudyGroupRepository extends JpaRepository<StudyGroup, Integer> {

    @Query("select s from StudyGroup s " +
            "left join s.groupAdmin where " +
            "(:groupName is null or s.name ilike %:groupName%) " +
            "and (:adminName is null or s.groupAdmin.name ilike %:adminName%) " +
            "and (:semester is null or s.semester = :semester) " +
            "and (:formOfEducation is null or s.formOfEducation = :formOfEducation)"
    )
    Page<StudyGroup> findByFilter(
            @Param("groupName") String groupName,
            @Param("adminName") String adminName,
            @Param("semester") Semester semester,
            @Param("formOfEducation") FormOfEducation formOfEducation,
            Pageable pageable
    );

    List<StudyGroup> findAllByGroupAdminIsNotNull();

    Boolean existsByName(String name);
    Integer countByName(String name);
}
