package com.kingdoms.plugin.building;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.kingdoms.plugin.KingdomsPlugin;

import java.util.*;
import java.util.concurrent.*;

/**
 * Manages all construction sites and completed buildings
 */
public class BuildingManager {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    
    private final KingdomsPlugin plugin;
    private final List<ConstructionSite> constructionSites;
    private final List<Building> completedBuildings;
    private final ScheduledExecutorService scheduler;
    
    public BuildingManager(KingdomsPlugin plugin) {
        this.plugin = plugin;
        this.constructionSites = new CopyOnWriteArrayList<>();
        this.completedBuildings = new CopyOnWriteArrayList<>();
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        
        // Check construction progress every second
        scheduler.scheduleAtFixedRate(this::checkConstructions, 1, 1, TimeUnit.SECONDS);
        
        LOGGER.atInfo().log("BuildingManager initialized");
    }

    /**
     * Start construction of a building at the given location
     */
    public ConstructionSite startConstruction(CommandContext ctx, BuildingType type, int x, int y, int z) {
        ConstructionSite site = new ConstructionSite(type, x, y, z);
        constructionSites.add(site);
        
        LOGGER.atInfo().log("Started construction: %s at (%d, %d, %d)", 
            type.getDisplayName(), x, y, z);
        
        ctx.sendMessage(Message.raw("§aStarted construction of " + type.getDisplayName() + "!"));
        ctx.sendMessage(Message.raw("§7Build time: " + type.getBuildTimeSeconds() + " seconds"));
        ctx.sendMessage(Message.raw("§7Location: (" + x + ", " + y + ", " + z + ")"));
        
        return site;
    }

    /**
     * Check all construction sites and complete finished ones
     */
    private void checkConstructions() {
        for (ConstructionSite site : constructionSites) {
            if (site.isFinished() && !site.isCompleted()) {
                completeConstruction(site);
            }
        }
    }

    /**
     * Complete a construction site, turning it into a building
     */
    private void completeConstruction(ConstructionSite site) {
        site.setCompleted(true);
        constructionSites.remove(site);
        
        Building building = new Building(site);
        completedBuildings.add(building);
        
        LOGGER.atInfo().log("§aConstruction completed: %s at (%d, %d, %d)", 
            site.getType().getDisplayName(),
            site.getX(), site.getY(), site.getZ());
    }

    /**
     * Get all active construction sites
     */
    public List<ConstructionSite> getConstructionSites() {
        return new ArrayList<>(constructionSites);
    }

    /**
     * Get all completed buildings
     */
    public List<Building> getCompletedBuildings() {
        return new ArrayList<>(completedBuildings);
    }

    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
        LOGGER.atInfo().log("BuildingManager shut down");
    }
}
