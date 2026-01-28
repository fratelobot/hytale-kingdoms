package com.kingdoms.plugin;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.kingdoms.plugin.building.BuildingManager;
import com.kingdoms.plugin.items.ItemRegistry;
import com.kingdoms.plugin.listeners.BlueprintPlacementListener;

import javax.annotation.Nonnull;

/**
 * Kingdoms - Persistent RTS game for Hytale
 * 
 * Build bases, train armies, conquer AI kingdoms!
 * 
 * Usage:
 * 1. Get blueprint item: /give <player> Town_Hall_Blueprint
 * 2. Place blueprint on ground
 * 3. Construction site spawns
 * 4. Wait for build timer
 * 5. Building complete!
 */
public class KingdomsPlugin extends JavaPlugin {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private static KingdomsPlugin instance;
    
    private BuildingManager buildingManager;
    private ItemRegistry itemRegistry;
    private BlueprintPlacementListener blueprintListener;

    public KingdomsPlugin(@Nonnull JavaPluginInit init) {
        super(init);
        instance = this;
        LOGGER.atInfo().log("Kingdoms plugin loading...");
    }

    @Override
    protected void setup() {
        LOGGER.atInfo().log("Setting up Kingdoms plugin");
        
        // Initialize registries
        this.itemRegistry = new ItemRegistry(this);
        this.itemRegistry.registerItems();
        
        // Initialize managers
        this.buildingManager = new BuildingManager(this);
        
        // Initialize listeners
        this.blueprintListener = new BlueprintPlacementListener(this);
        
        LOGGER.atInfo().log("Kingdoms plugin ready!");
    }
    
    @Override
    protected void start() {
        LOGGER.atInfo().log("Kingdoms plugin starting...");
        
        // Register event listeners
        blueprintListener.register();
        
        LOGGER.atInfo().log("Kingdoms plugin started!");
        LOGGER.atInfo().log("Available blueprints:");
        LOGGER.atInfo().log("  /give <player> Town_Hall_Blueprint");
        LOGGER.atInfo().log("  /give <player> Lumbermill_Blueprint");
        LOGGER.atInfo().log("  /give <player> Farm_Blueprint");
    }

    @Override
    public void shutdown() {
        LOGGER.atInfo().log("Kingdoms plugin shutting down...");
        
        // Unregister listeners
        if (blueprintListener != null) {
            blueprintListener.unregister();
        }
        
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
