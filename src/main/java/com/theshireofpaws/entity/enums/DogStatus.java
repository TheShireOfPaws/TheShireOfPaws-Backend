package com.theshireofpaws.entity.enums;

public enum DogStatus {
    AVAILABLE("Available"),
    ADOPTED("Adopted"),
    IN_PROCESS("In process");

    private final String displayName;

    DogStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}