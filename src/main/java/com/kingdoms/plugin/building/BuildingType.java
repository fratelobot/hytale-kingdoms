package com.kingdoms.plugin.building;

/**
 * Types of buildings available in Kingdoms
 */
public enum BuildingType {
    TOWN_HALL("Town Hall", 10, 5, 5),
    LUMBERMILL("Lumbermill", 8, 3, 3),
    STONE_MINE("Stone Mine", 8, 3, 3),
    IRON_MINE("Iron Mine", 12, 3, 3),
    FARM("Farm", 6, 4, 4),
    BARRACKS("Barracks", 15, 4, 4),
    HOUSE("House", 5, 2, 2);

    private final String displayName;
    private final int buildTimeSeconds;
    private final int sizeX;
    private final int sizeZ;

    BuildingType(String displayName, int buildTimeSeconds, int sizeX, int sizeZ) {
        this.displayName = displayName;
        this.buildTimeSeconds = buildTimeSeconds;
        this.sizeX = sizeX;
        this.sizeZ = sizeZ;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getBuildTimeSeconds() {
        return buildTimeSeconds;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeZ() {
        return sizeZ;
    }
}
