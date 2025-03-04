package se.ifmo.is_lab1.dto.batch;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import se.ifmo.is_lab1.models.enums.FormOfEducation;
import se.ifmo.is_lab1.models.enums.Semester;

@Data
public class StudyGroupBatchRequest {
    @NotNull
    @NotEmpty
    @NotBlank
    private String name; //Поле не может быть null, Строка не может быть пустой

    @NotNull
    private CoordinatesBatchRequest coordinates; //Поле не может быть null

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

    private PersonBatchRequest groupAdmin; //Поле может быть null

    private Boolean isEditable = Boolean.TRUE;

    public Boolean validate() {
        return name != null &&
                !name.isEmpty() &&
                !name.isBlank() &&
                coordinates.validate() &&
                studentsCount != null &&
                studentsCount > 0 &&
                expelledStudents != null &&
                expelledStudents > 0 &&
                transferredStudents > 0 &&
                formOfEducation != null &&
                shouldBeExpelled != null &&
                shouldBeExpelled > 0 &&
                semester != null &&
                groupAdmin.validate();
    }
}
