package com.kingdoms.plugin.building;

import java.util.UUID;

/**
 * Represents a completed building
 */
public class Building {
    
    private final UUID id;
    private final UUID ownerId;
    private final BuildingType type;
    private final int x;
    private final int y;
    private final int z;
    private final long completedTime;

    public Building(ConstructionSite site) {
        this.id = site.getId();
        this.ownerId = site.getOwnerId();
        this.type = site.getType();
        this.x = site.getX();
        this.y = site.getY();
        this.z = site.getZ();
        this.completedTime = System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public BuildingType getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public long getCompletedTime() {
        return completedTime;
    }

    @Override
    public String toString() {
        return String.format("%s at (%d, %d, %d)", type.getDisplayName(), x, y, z);
    }
}
