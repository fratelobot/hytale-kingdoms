package com.kingdoms.plugin.items;

import com.kingdoms.plugin.building.BuildingType;

/**
 * Represents a blueprint item configuration
 */
public class BlueprintItem {
    
    private final String id;
    private final String displayName;
    private final String description;
    private final BuildingType buildingType;
    
    public BlueprintItem(String id, String displayName, String description, BuildingType buildingType) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.buildingType = buildingType;
    }
    
    public String getId() {
        return id;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public BuildingType getBuildingType() {
        return buildingType;
    }
    
    // Predefined blueprints
    public static final BlueprintItem TOWN_HALL = new BlueprintItem(
        "Town_Hall_Blueprint",
        "Town Hall Blueprint",
        "Place to start construction of a Town Hall",
        BuildingType.TOWN_HALL
    );
    
    public static final BlueprintItem LUMBERMILL = new BlueprintItem(
        "Lumbermill_Blueprint",
        "Lumbermill Blueprint", 
        "Place to start construction of a Lumbermill",
        BuildingType.LUMBERMILL
    );
    
    public static final BlueprintItem FARM = new BlueprintItem(
        "Farm_Blueprint",
        "Farm Blueprint",
        "Place to start construction of a Farm",
        BuildingType.FARM
    );
    
    public static final BlueprintItem[] ALL_BLUEPRINTS = {
        TOWN_HALL, LUMBERMILL, FARM
    };
}
