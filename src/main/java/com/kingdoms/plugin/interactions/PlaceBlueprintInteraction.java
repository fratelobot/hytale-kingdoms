package com.kingdoms.plugin.interactions;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.interaction.interaction.InteractionContext;
import com.hypixel.hytale.server.core.modules.interaction.interaction.InteractionState;
import com.hypixel.hytale.server.core.modules.interaction.interaction.SimpleInstantInteraction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.cooldown.CooldownHandler;
import com.hypixel.hytale.server.core.universe.Player;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.protocol.InteractionType;
import com.kingdoms.plugin.KingdomsPlugin;
import com.kingdoms.plugin.items.BlueprintItem;

import javax.annotation.Nonnull;

/**
 * Custom interaction for placing blueprint items
 * When player right-clicks with a blueprint, spawn a construction site
 */
public class PlaceBlueprintInteraction extends SimpleInstantInteraction {

    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public static final BuilderCodec<PlaceBlueprintInteraction> CODEC = BuilderCodec.builder(
        PlaceBlueprintInteraction.class, 
        PlaceBlueprintInteraction::new, 
        SimpleInstantInteraction.CODEC
    ).build();

    @Override
    protected void firstRun(@Nonnull InteractionType interactionType, 
                           @Nonnull InteractionContext interactionContext, 
                           @Nonnull CooldownHandler cooldownHandler) {
        
        CommandBuffer<EntityStore> commandBuffer = interactionContext.getCommandBuffer();
        if (commandBuffer == null) {
            interactionContext.getState().state = InteractionState.Failed;
            LOGGER.atWarning().log("CommandBuffer is null");
            return;
        }

        Ref<EntityStore> ref = interactionContext.getEntity();
        Player player = commandBuffer.getComponent(ref, Player.getComponentType());
        if (player == null) {
            interactionContext.getState().state = InteractionState.Failed;
            LOGGER.atWarning().log("Player is null");
            return;
        }

        ItemStack itemStack = interactionContext.getHeldItem();
        if (itemStack == null) {
            interactionContext.getState().state = InteractionState.Failed;
            LOGGER.atWarning().log("ItemStack is null");
            return;
        }

        String itemId = itemStack.getItemId();
        BlueprintItem blueprint = KingdomsPlugin.getInstance().getItemRegistry().getBlueprint(itemId);
        
        if (blueprint == null) {
            interactionContext.getState().state = InteractionState.Failed;
            LOGGER.atWarning().log("Unknown blueprint item: %s", itemId);
            return;
        }

        // Get target position from interaction context
        // For now, use player position - TODO: get actual raycast hit position
        var position = interactionContext.getPosition();
        int x = (int) position.getX();
        int y = (int) position.getY();
        int z = (int) position.getZ();

        LOGGER.atInfo().log("Placing blueprint %s at (%d, %d, %d)", 
            blueprint.getDisplayName(), x, y, z);

        // Start construction
        KingdomsPlugin.getInstance().getBuildingManager()
            .startConstructionSilent(blueprint.getBuildingType(), x, y, z);

        // Send feedback to player
        player.sendMessage(com.hypixel.hytale.server.core.Message.raw(
            "§a⚒ Started construction of " + blueprint.getDisplayName() + "!"));

        interactionContext.getState().state = InteractionState.Completed;
    }
}
