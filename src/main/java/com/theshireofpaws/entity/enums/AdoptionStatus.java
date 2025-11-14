package com.theshireofpaws.entity.enums;

public enum AdoptionStatus {
    IN_PROCESS("In process"),
    APPROVED("Approved"),
    DENIED("Denied");

    private final String displayName;

    AdoptionStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
