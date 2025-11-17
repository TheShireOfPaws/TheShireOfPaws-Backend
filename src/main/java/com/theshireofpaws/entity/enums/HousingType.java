package com.theshireofpaws.entity.enums;

public enum HousingType {
    HOUSE("House"),
    APARTMENT("Apartment"),
    OTHER("Other");

    private final String displayName;

    HousingType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}