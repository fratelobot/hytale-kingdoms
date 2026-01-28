package com.kingdoms.plugin;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.kingdoms.plugin.building.BuildingManager;
import com.kingdoms.plugin.commands.BuildingCommand;
import com.kingdoms.plugin.commands.BuildTownHallCommand;
import com.kingdoms.plugin.commands.ListBuildingsCommand;

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
        this.getCommandRegistry().registerCommand(new BuildTownHallCommand(this));
        this.getCommandRegistry().registerCommand(new ListBuildingsCommand(this));
        
        LOGGER.atInfo().log("Kingdoms plugin ready! Commands: /buildings, /build_townhall, /list_buildings");
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
