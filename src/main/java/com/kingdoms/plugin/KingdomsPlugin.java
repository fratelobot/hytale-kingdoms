package com.kingdoms.plugin;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.modules.interaction.interaction.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.kingdoms.plugin.building.BuildingManager;
import com.kingdoms.plugin.interactions.PlaceBlueprintInteraction;
import com.kingdoms.plugin.items.ItemRegistry;

import javax.annotation.Nonnull;

/**
 * Kingdoms - Persistent RTS game for Hytale
 * 
 * Build bases, train armies, conquer AI kingdoms!
 * 
 * Usage:
 * 1. Get blueprint item: /give <player> Town_Hall_Blueprint
 * 2. Right-click with blueprint to place
 * 3. Construction site spawns
 * 4. Wait for build timer
 * 5. Building complete!
 */
public class KingdomsPlugin extends JavaPlugin {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private static KingdomsPlugin instance;
    
    private BuildingManager buildingManager;
    private ItemRegistry itemRegistry;

    public KingdomsPlugin(@Nonnull JavaPluginInit init) {
        super(init);
        instance = this;
        LOGGER.atInfo().log("Kingdoms plugin loading...");
    }

    @Override
    protected void setup() {
        LOGGER.atInfo().log("Setting up Kingdoms plugin");
        
        // Register custom interaction for placing blueprints
        this.getCodecRegistry(Interaction.CODEC).register(
            "kingdoms_place_blueprint", 
            PlaceBlueprintInteraction.class, 
            PlaceBlueprintInteraction.CODEC
        );
        LOGGER.atInfo().log("Registered kingdoms_place_blueprint interaction");
        
        // Initialize registries
        this.itemRegistry = new ItemRegistry(this);
        this.itemRegistry.registerItems();
        
        // Initialize managers
        this.buildingManager = new BuildingManager(this);
        
        LOGGER.atInfo().log("Kingdoms plugin ready!");
    }
    
    @Override
    protected void start() {
        LOGGER.atInfo().log("Kingdoms plugin started!");
        LOGGER.atInfo().log("Available blueprints:");
        LOGGER.atInfo().log("  /give <player> Town_Hall_Blueprint");
        LOGGER.atInfo().log("  /give <player> Lumbermill_Blueprint");
        LOGGER.atInfo().log("  /give <player> Farm_Blueprint");
        LOGGER.atInfo().log("Right-click with blueprint to place construction site!");
    }

    @Override
    public void shutdown() {
        LOGGER.atInfo().log("Kingdoms plugin shutting down...");
        
        // Shutdown managers
        if (buildingManager != null) {
            buildingManager.shutdown();
        }
    }

    public static KingdomsPlugin getInstance() {
        return instance;
    }

    public BuildingManager getBuildingManager() {
        return buildingManager;
    }

    public ItemRegistry getItemRegistry() {
        return itemRegistry;
    }
}
