package com.theshireofpaws.entity.enums;

public enum DogTrait {
    ACTIVE("Active"),
    CALM("Calm"),
    AFFECTIONATE("Affectionate"),
    GOOD_WITH_KIDS("Good with kids"),
    GOOD_WITH_DOGS("Good with other dogs"),
    HOUSE_TRAINED("House-trained");

    private final String displayName;

    DogTrait(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
