package com.kingdoms.plugin.building;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.player.Player;
import com.kingdoms.plugin.KingdomsPlugin;

import java.util.*;
import java.util.concurrent.*;

/**
 * Manages all construction sites and completed buildings
 */
public class BuildingManager {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    
    private final KingdomsPlugin plugin;
    private final Map<UUID, ConstructionSite> constructionSites;
    private final Map<UUID, Building> completedBuildings;
    private final ScheduledExecutorService scheduler;
    
    public BuildingManager(KingdomsPlugin plugin) {
        this.plugin = plugin;
        this.constructionSites = new ConcurrentHashMap<>();
        this.completedBuildings = new ConcurrentHashMap<>();
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        
        // Check construction progress every second
        scheduler.scheduleAtFixedRate(this::checkConstructions, 1, 1, TimeUnit.SECONDS);
        
        LOGGER.atInfo().log("BuildingManager initialized");
    }

    /**
     * Start construction of a building at the given location
     */
    public ConstructionSite startConstruction(Player player, BuildingType type, int x, int y, int z) {
        ConstructionSite site = new ConstructionSite(player.getUuid(), type, x, y, z);
        constructionSites.put(site.getId(), site);
        
        LOGGER.atInfo().log("Started construction: %s at (%d, %d, %d) by %s", 
            type.getDisplayName(), x, y, z, player.getName());
        
        player.sendMessage(Message.raw("§aStarted construction of " + type.getDisplayName() + "!"));
        player.sendMessage(Message.raw("§7Build time: " + type.getBuildTimeSeconds() + " seconds"));
        
        // TODO: Place visual construction site blocks in the world
        
        return site;
    }

    /**
     * Check all construction sites and complete finished ones
     */
    private void checkConstructions() {
        List<ConstructionSite> toComplete = new ArrayList<>();
        
        for (ConstructionSite site : constructionSites.values()) {
            if (site.isFinished() && !site.isCompleted()) {
                toComplete.add(site);
            }
        }
        
        for (ConstructionSite site : toComplete) {
            completeConstruction(site);
        }
    }

    /**
     * Complete a construction site, turning it into a building
     */
    private void completeConstruction(ConstructionSite site) {
        site.setCompleted(true);
        constructionSites.remove(site.getId());
        
        Building building = new Building(site);
        completedBuildings.put(building.getId(), building);
        
        LOGGER.atInfo().log("Construction completed: %s", site);
        
        // TODO: Replace construction blocks with actual building
        // TODO: Notify the player (need to get player from UUID)
        
        // For now, log it
        LOGGER.atInfo().log("§a%s construction complete!", site.getType().getDisplayName());
    }

    /**
     * Get all construction sites for a player
     */
    public List<ConstructionSite> getPlayerConstructions(UUID playerId) {
        List<ConstructionSite> result = new ArrayList<>();
        for (ConstructionSite site : constructionSites.values()) {
            if (site.getOwnerId().equals(playerId)) {
                result.add(site);
            }
        }
        return result;
    }

    /**
     * Get all completed buildings for a player
     */
    public List<Building> getPlayerBuildings(UUID playerId) {
        List<Building> result = new ArrayList<>();
        for (Building building : completedBuildings.values()) {
            if (building.getOwnerId().equals(playerId)) {
                result.add(building);
            }
        }
        return result;
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
