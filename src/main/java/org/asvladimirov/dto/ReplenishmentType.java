package org.asvladimirov.dto;

public enum ReplenishmentType {
    CHALLENGE("challenge"),
    BALANCE_DOWNGRADE("balance_downgrade");


    String description;

    ReplenishmentType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
