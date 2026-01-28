package com.kingdoms.plugin.listeners;

import com.hypixel.hytale.event.EventRegistration;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.event.events.ecs.PlaceBlockEvent;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.kingdoms.plugin.KingdomsPlugin;
import com.kingdoms.plugin.items.BlueprintItem;

import javax.annotation.Nonnull;

/**
 * Listens for block placement events and intercepts blueprint items
 * to spawn construction sites instead of regular blocks.
 */
public class BlueprintPlacementListener {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    
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
        BlueprintItem blueprint = plugin.getItemRegistry().getBlueprint(itemId);
        
        if (blueprint == null) {
            return;
        }
        
        // Cancel the default block placement
        event.setCancelled(true);
        
        // Get placement position
        Vector3i targetBlock = event.getTargetBlock();
        int x = targetBlock.getX();
        int y = targetBlock.getY();
        int z = targetBlock.getZ();
        
        LOGGER.atInfo().log("Blueprint placed: %s at (%d, %d, %d)", 
            blueprint.getDisplayName(), x, y, z);
        
        // Start construction at the placement location
        plugin.getBuildingManager().startConstructionSilent(blueprint.getBuildingType(), x, y, z);
    }
}
