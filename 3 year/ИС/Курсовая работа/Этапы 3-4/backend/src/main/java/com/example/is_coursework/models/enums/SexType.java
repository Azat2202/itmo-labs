package com.example.is_coursework.models.enums;

public enum SexType {
//    MALE, FEMALE
    MALE("Мужчина"),
    FEMALE("Женщина");

    private final String displayName;

    SexType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
