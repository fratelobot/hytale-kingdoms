package com.kingdoms.plugin;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.kingdoms.plugin.building.BuildingManager;
import com.kingdoms.plugin.commands.*;
import com.kingdoms.plugin.listeners.BlueprintPlacementListener;

import javax.annotation.Nonnull;

/**
 * Kingdoms - Persistent RTS game for Hytale
 * 
 * Build bases, train armies, conquer AI kingdoms!
 */
public class KingdomsPlugin extends JavaPlugin {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private static KingdomsPlugin instance;
    
    private BuildingManager buildingManager;
    private BlueprintPlacementListener blueprintListener;

    public KingdomsPlugin(@Nonnull JavaPluginInit init) {
        super(init);
        instance = this;
        LOGGER.atInfo().log("Kingdoms plugin loading...");
    }

    @Override
    protected void setup() {
        LOGGER.atInfo().log("Setting up Kingdoms plugin");
        
        // Initialize managers
        this.buildingManager = new BuildingManager(this);
        
        // Initialize listeners
        this.blueprintListener = new BlueprintPlacementListener(this);
        
        // Register commands
        registerCommands();
        
        LOGGER.atInfo().log("Kingdoms plugin ready!");
        LOGGER.atInfo().log("Commands: /buildings, /build, /build_townhall, /build_lumbermill, /build_farm, /list_buildings");
    }
    
    @Override
    protected void start() {
        LOGGER.atInfo().log("Kingdoms plugin starting...");
        
        // Register event listeners
        blueprintListener.register();
        
        LOGGER.atInfo().log("Kingdoms plugin started!");
        LOGGER.atInfo().log("Place blueprint items to start construction!");
    }
    
    private void registerCommands() {
        // Info commands
        this.getCommandRegistry().registerCommand(new BuildingCommand(this));
        this.getCommandRegistry().registerCommand(new ListBuildingsCommand(this));
        
        // Generic build command
        this.getCommandRegistry().registerCommand(new BuildCommand(this));
        
        // Quick build commands (for testing)
        this.getCommandRegistry().registerCommand(new BuildTownHallCommand(this));
        this.getCommandRegistry().registerCommand(new BuildLumbermillCommand(this));
        this.getCommandRegistry().registerCommand(new BuildFarmCommand(this));
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
}
