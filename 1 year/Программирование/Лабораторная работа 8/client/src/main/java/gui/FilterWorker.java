package gui;

import commandLine.forms.NationalityForm;
import models.*;

import java.util.*;
import java.util.function.Predicate;

public class FilterWorker {
    private final LinkedHashMap<Integer, Predicate<StudyGroup>> predicates = new LinkedHashMap<>();

    public void addPredicate(int row, Predicate<StudyGroup> predicate){
        predicates.put(row, predicate);
    }

    public Predicate<StudyGroup> getPredicate(){
        return predicates.values().stream()
                .reduce(x -> true, Predicate::and);
    }

    public void clearPredicates(){
        this.predicates.clear();
    }

    public void parsePredicate(int row, List<?> values){
        switch (row) {
            case 0 -> this.addPredicate(0, (o) -> values.contains(o.getId()));
            case 1 -> this.addPredicate(1, (o) -> values.contains(o.getName()));
            case 2 -> this.addPredicate(2, (o) -> values.contains(o.getCoordinates()));
            case 3 -> this.addPredicate(3, (o) -> values.contains(o.getCreationDate()));
            case 4 -> this.addPredicate(4, (o) -> values.contains(o.getStudentsCount()));
            case 5 -> this.addPredicate(5, (o) -> values.contains(o.getExpelledStudents()));
            case 6 -> this.addPredicate(6, (o) -> values.contains(o.getAverageMark()));
            case 7 -> this.addPredicate(7, (o) -> values.contains(o.getFormOfEducation()));
            case 8 -> this.addPredicate(8, (o) -> values.contains(o.getGroupAdmin().getName()));
            case 9 -> this.addPredicate(9, (o) -> values.contains(o.getGroupAdmin().getWeight()));
            case 10 -> this.addPredicate(10, (o) -> values.contains(o.getGroupAdmin().getEyeColor()));
            case 11 -> this.addPredicate(11, (o) -> values.contains(o.getGroupAdmin().getHairColor()));
            case 12 -> this.addPredicate(12, (o) -> values.contains(o.getGroupAdmin().getNationality()));
            case 13 -> this.addPredicate(13, (o) -> values.contains(o.getGroupAdmin().getLocation().getCoordinates()));
            case 14 -> this.addPredicate(14, (o) -> values.contains(o.getGroupAdmin().getLocation().getName()));
            case 15 -> this.addPredicate(15, (o) -> values.contains(o.getUserLogin()));
        }
    }
}
