package com.kingdoms.plugin.items;

import com.hypixel.hytale.assetstore.AssetRegistry;
import com.hypixel.hytale.assetstore.AssetStore;
import com.hypixel.hytale.assetstore.map.DefaultAssetMap;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.kingdoms.plugin.KingdomsPlugin;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * Registers custom blueprint items with the Hytale asset system
 */
public class ItemRegistry {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    
    private final KingdomsPlugin plugin;
    private final Map<String, BlueprintItem> blueprintItems = new HashMap<>();

    public ItemRegistry(KingdomsPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Register all blueprint items
     */
    public void registerItems() {
        LOGGER.atInfo().log("Registering Kingdoms blueprint items...");
        
        for (BlueprintItem blueprint : BlueprintItem.ALL_BLUEPRINTS) {
            registerBlueprintItem(blueprint);
        }
        
        LOGGER.atInfo().log("Registered %d blueprint items", blueprintItems.size());
    }

    /**
     * Register a single blueprint item
     */
    private void registerBlueprintItem(BlueprintItem blueprint) {
        blueprintItems.put(blueprint.getId(), blueprint);
        LOGGER.atInfo().log("Registered blueprint: %s", blueprint.getId());
        
        // Note: The actual Item asset registration happens via JSON files
        // in Server/Item/ folder which are loaded by the asset system.
        // This registry just tracks our custom items for the placement listener.
    }

    /**
     * Check if an item ID is a blueprint
     */
    public boolean isBlueprint(@Nonnull String itemId) {
        return blueprintItems.containsKey(itemId);
    }

    /**
     * Get blueprint by item ID
     */
    public BlueprintItem getBlueprint(@Nonnull String itemId) {
        return blueprintItems.get(itemId);
    }

    /**
     * Get all registered blueprints
     */
    public Map<String, BlueprintItem> getAllBlueprints() {
        return blueprintItems;
    }
}
