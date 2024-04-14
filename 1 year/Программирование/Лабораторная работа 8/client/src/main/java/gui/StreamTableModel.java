package gui;

import models.Coordinates;
import models.StudyGroup;

import javax.swing.table.AbstractTableModel;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamTableModel extends AbstractTableModel {
    private DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, GuiManager.getLocale());
    private String[] columnNames;
    private ArrayList<StudyGroup> allData;
    private ArrayList<StudyGroup> filteredData;
    private Integer sortingColumn = 0;
    private boolean reversed = false;
    private FilterWorker filterWorker;

    public StreamTableModel(String[] columnNames, int rowCount, FilterWorker filterWorker) {
        this.columnNames = columnNames;
        this.filterWorker = filterWorker;
    }

    public void setDataVector(ArrayList<StudyGroup> data, String[] columnNames){
        this.allData = data;
        this.columnNames = columnNames;
        this.filteredData = actFiltration(data);
    }

    public void performSorting(int column){
        this.reversed = (sortingColumn == column)
                ? !reversed
                : false;
        this.sortingColumn = column;
        this.filteredData = actFiltration(this.allData);
    }

    public void performFiltration(){
        this.filteredData = actFiltration(this.allData);
    }

    @Override
    public int getRowCount() {
        return this.filteredData.size() - 1;
    }

    @Override
    public int getColumnCount() {
        return this.columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return this.columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return this.getValueAtRow(this.filteredData.get(rowIndex), columnIndex);
    }

    private ArrayList<StudyGroup> actFiltration(ArrayList<StudyGroup> allData){
        if(Objects.isNull(this.sortingColumn)) return allData;
        ArrayList<StudyGroup> sorted = new ArrayList<>(allData.stream()
                .sorted(Comparator.comparing(o -> this.sortingColumn < 0
                        ? (float)o.getId()
                        : this.getSortedFiledFloat(o, this.sortingColumn)))
                .filter(filterWorker.getPredicate())
                .toList());
        if(reversed) Collections.reverse(sorted);
        return sorted;
    }

    public Object getValueAtRow(StudyGroup o, int row){
        return switch (row){
            case 0 -> o.getId();
            case 1 -> o.getName();
            case 2 -> o.getCoordinates();
            case 3 -> dateFormat.format(o.getCreationDate());
            case 4 -> o.getStudentsCount();
            case 5 -> o.getExpelledStudents();
            case 6 -> o.getAverageMark();
            case 7 -> o.getFormOfEducation();
            case 8 -> o.getGroupAdmin().getName();
            case 9 -> o.getGroupAdmin().getWeight();
            case 10 -> o.getGroupAdmin().getEyeColor();
            case 11 -> o.getGroupAdmin().getHairColor();
            case 12 -> o.getGroupAdmin().getNationality();
            case 13 -> o.getGroupAdmin().getLocation().getCoordinates();
            case 14 -> o.getGroupAdmin().getLocation().getName();
            case 15 -> o.getUserLogin();
            default -> throw new IllegalStateException("Unexpected value: " + row);
        };
    }

    public float getSortedFiledFloat(StudyGroup o, int column){
        return switch (column){
            case 0 -> o.getId();
            case 1 -> o.getName().length();
            case 2 -> o.getCoordinates().getRadius();
            case 3 -> o.getCreationDate().getTime();
            case 4 -> o.getStudentsCount();
            case 5 -> o.getExpelledStudents();
            case 6 -> o.getAverageMark();
            case 7 -> o.getFormOfEducation().ordinal();
            case 8 -> o.getGroupAdmin().getName().length();
            case 9 -> o.getGroupAdmin().getWeight();
            case 10 -> o.getGroupAdmin().getEyeColor().ordinal();
            case 11 -> o.getGroupAdmin().getHairColor().ordinal();
            case 12 -> o.getGroupAdmin().getNationality().ordinal();
            case 13 -> o.getGroupAdmin().getLocation().getRadius();
            case 14 -> o.getGroupAdmin().getLocation().getName().length();
            case 15 -> o.getUserLogin().length();
            default -> throw new IllegalStateException("Unexpected value: " + column);
        };
    }

    public StudyGroup getRow(int row) {
        try {
            return this.filteredData.get(row);
        } catch (IndexOutOfBoundsException e) {
            return this.filteredData.get(0);
        }
    }

    public ArrayList<StudyGroup> getAllData() {
        return allData;
    }
}
