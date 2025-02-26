package se.ifmo.is_lab1.messages.collection;

import lombok.Data;
import se.ifmo.is_lab1.messages.authentication.UserResponse;
import se.ifmo.is_lab1.models.Coordinates;
import se.ifmo.is_lab1.models.Person;
import se.ifmo.is_lab1.models.enums.FormOfEducation;
import se.ifmo.is_lab1.models.enums.Semester;

@Data
public class StudyGroupResponse {
    private int id;

    private String name;

    private Coordinates coordinates;

    private java.time.ZonedDateTime creationDate;

    private Integer studentsCount;

    private Integer expelledStudents;

    private int transferredStudents;

    private FormOfEducation formOfEducation;

    private Integer shouldBeExpelled;

    private Semester semester;

    private Person groupAdmin;

    private UserResponse user;

    private Boolean isEditable;
}

