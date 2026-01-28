package com.kingdoms.plugin;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.kingdoms.plugin.building.BuildingManager;
import com.kingdoms.plugin.commands.BuildingCommand;

import javax.annotation.Nonnull;

/**
 * Kingdoms - Persistent RTS game for Hytale
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
        this.getCommandRegistry().registerCommand(new BuildingCommand(this));
        
        LOGGER.atInfo().log("Kingdoms plugin ready!");
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
