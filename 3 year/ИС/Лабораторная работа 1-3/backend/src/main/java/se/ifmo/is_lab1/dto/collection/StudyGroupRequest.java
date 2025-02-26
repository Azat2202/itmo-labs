package se.ifmo.is_lab1.dto.collection;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import se.ifmo.is_lab1.models.enums.FormOfEducation;
import se.ifmo.is_lab1.models.enums.Semester;

@Data
@AllArgsConstructor
@Builder
public class StudyGroupRequest {
    @NotNull
    @NotEmpty
    @NotBlank
    private String name; //Поле не может быть null, Строка не может быть пустой

    @NotNull
    private Long coordinatesId; //Поле не может быть null

    @NotNull
    @Positive
    private Integer studentsCount; //Значение поля должно быть больше 0, Поле не может быть null

    @NotNull
    @Positive
    private Integer expelledStudents; //Значение поля должно быть больше 0, Поле не может быть null

    @NotNull
    @Positive
    private int transferredStudents; //Значение поля должно быть больше 0

    @NotNull
    private FormOfEducation formOfEducation; //Поле не может быть null

    @Positive
    private Integer shouldBeExpelled; //Значение поля должно быть больше 0, Поле может быть null

    @NotNull
    private Semester semester; //Поле не может быть null

    private Long groupAdminId; //Поле может быть null

    private Boolean isEditable = Boolean.TRUE;

    public Boolean validate() {
        return name != null &&
                !name.isEmpty() &&
                !name.isBlank() &&
                coordinatesId != null &&
                studentsCount != null &&
                studentsCount > 0 &&
                expelledStudents != null &&
                expelledStudents > 0 &&
                transferredStudents > 0 &&
                formOfEducation != null &&
                shouldBeExpelled != null &&
                shouldBeExpelled > 0 &&
                semester != null;
    }
}
