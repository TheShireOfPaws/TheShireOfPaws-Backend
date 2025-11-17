package com.theshireofpaws.entity.enums;

public enum DogGender {
    MALE("Male"),
    FEMALE("Female"),
    UNKNOWN("Unknown");

    private final String displayName;

    DogGender(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}