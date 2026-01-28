package com.kingdoms.plugin.listeners;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.event.EventRegistration;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.event.events.ecs.PlaceBlockEvent;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.kingdoms.plugin.KingdomsPlugin;
import com.kingdoms.plugin.building.BuildingManager;
import com.kingdoms.plugin.building.BuildingType;

import javax.annotation.Nonnull;

/**
 * Listens for block placement events and intercepts blueprint items
 * to spawn construction sites instead of regular blocks.
 */
public class BlueprintPlacementListener {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    
    private static final String BLUEPRINT_PREFIX = "kingdoms:";
    private static final String TOWN_HALL_BLUEPRINT = "kingdoms:town_hall_blueprint";
    private static final String LUMBERMILL_BLUEPRINT = "kingdoms:lumbermill_blueprint";
    private static final String FARM_BLUEPRINT = "kingdoms:farm_blueprint";
    
    private final KingdomsPlugin plugin;
    private EventRegistration registration;

    public BlueprintPlacementListener(KingdomsPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Register the event listener with the server's event bus
     */
    public void register() {
        registration = HytaleServer.get().getEventBus().register(
            PlaceBlockEvent.class,
            this::onPlaceBlock
        );
        LOGGER.atInfo().log("BlueprintPlacementListener registered");
    }

    /**
     * Unregister the event listener
     */
    public void unregister() {
        if (registration != null) {
            registration.unregister();
            LOGGER.atInfo().log("BlueprintPlacementListener unregistered");
        }
    }

    /**
     * Handle block placement events
     */
    private void onPlaceBlock(@Nonnull PlaceBlockEvent event) {
        ItemStack itemInHand = event.getItemInHand();
        
        if (itemInHand == null) {
            return;
        }
        
        String itemId = itemInHand.getItem().getId();
        
        // Check if this is a blueprint item
        if (!itemId.startsWith(BLUEPRINT_PREFIX)) {
            return;
        }
        
        // Cancel the default block placement
        event.cancel();
        
        // Get placement position
        Vector3i targetBlock = event.getTargetBlock();
        int x = targetBlock.getX();
        int y = targetBlock.getY();
        int z = targetBlock.getZ();
        
        // Determine building type from item
        BuildingType buildingType = getBuildingTypeFromItem(itemId);
        
        if (buildingType == null) {
            LOGGER.atWarning().log("Unknown blueprint item: %s", itemId);
            return;
        }
        
        LOGGER.atInfo().log("Blueprint placed: %s at (%d, %d, %d)", 
            buildingType.getDisplayName(), x, y, z);
        
        // Start construction at the placement location
        // Note: We need to get the player context to send messages
        // For now, just start construction without player feedback
        plugin.getBuildingManager().startConstructionSilent(buildingType, x, y, z);
    }

    /**
     * Map item ID to BuildingType
     */
    private BuildingType getBuildingTypeFromItem(String itemId) {
        switch (itemId) {
            case TOWN_HALL_BLUEPRINT:
                return BuildingType.TOWN_HALL;
            case LUMBERMILL_BLUEPRINT:
                return BuildingType.LUMBERMILL;
            case FARM_BLUEPRINT:
                return BuildingType.FARM;
            default:
                // Try to parse from item ID
                String typeName = itemId.replace(BLUEPRINT_PREFIX, "")
                                        .replace("_blueprint", "")
                                        .toUpperCase();
                try {
                    return BuildingType.valueOf(typeName);
                } catch (IllegalArgumentException e) {
                    return null;
                }
        }
    }
}
