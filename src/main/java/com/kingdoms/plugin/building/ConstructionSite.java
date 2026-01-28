package com.kingdoms.plugin.building;

import java.util.UUID;

/**
 * Represents a building under construction
 */
public class ConstructionSite {
    
    private final UUID id;
    private final UUID ownerId;
    private final BuildingType type;
    private final int x;
    private final int y;
    private final int z;
    private final long startTime;
    
    private boolean completed;

    public ConstructionSite(UUID ownerId, BuildingType type, int x, int y, int z) {
        this.id = UUID.randomUUID();
        this.ownerId = ownerId;
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
        this.startTime = System.currentTimeMillis();
        this.completed = false;
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

    /**
     * Get construction progress as percentage (0-100)
     */
    public int getProgress() {
        if (completed) return 100;
        
        long elapsed = System.currentTimeMillis() - startTime;
        long totalMs = type.getBuildTimeSeconds() * 1000L;
        int progress = (int) ((elapsed * 100) / totalMs);
        return Math.min(progress, 100);
    }

    /**
     * Check if construction is finished based on elapsed time
     */
    public boolean isFinished() {
        return getProgress() >= 100;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return String.format("%s at (%d, %d, %d) - %d%%", 
            type.getDisplayName(), x, y, z, getProgress());
    }
}
