package com.kingdoms.plugin;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.kingdoms.plugin.building.BuildingManager;
import com.kingdoms.plugin.commands.*;

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
        
        // Register commands
        registerCommands();
        
        LOGGER.atInfo().log("Kingdoms plugin ready!");
        LOGGER.atInfo().log("Commands: /buildings, /build, /build_townhall, /build_lumbermill, /build_farm, /list_buildings");
    }
    
    private void registerCommands() {
        // Info commands
        this.getCommandRegistry().registerCommand(new BuildingCommand(this));
        this.getCommandRegistry().registerCommand(new ListBuildingsCommand(this));
        
        // Generic build command
        this.getCommandRegistry().registerCommand(new BuildCommand(this));
        
        // Quick build commands
        this.getCommandRegistry().registerCommand(new BuildTownHallCommand(this));
        this.getCommandRegistry().registerCommand(new BuildLumbermillCommand(this));
        this.getCommandRegistry().registerCommand(new BuildFarmCommand(this));
    }

    @Override
    protected void start() {
        LOGGER.atInfo().log("Kingdoms plugin started!");
    }

    @Override
    public void shutdown() {
        LOGGER.atInfo().log("Kingdoms plugin shutting down...");
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
