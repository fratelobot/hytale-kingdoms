package com.kingdoms.plugin.interactions;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.protocol.BlockPosition;
import com.hypixel.hytale.protocol.InteractionState;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.kingdoms.plugin.KingdomsPlugin;
import com.kingdoms.plugin.items.BlueprintItem;

import javax.annotation.Nonnull;

/**
 * Custom interaction for placing blueprint items
 * When player right-clicks with a blueprint, spawn a construction site
 */
public class PlaceBlueprintInteraction extends SimpleInstantInteraction {

    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    @Nonnull
    public static final BuilderCodec<PlaceBlueprintInteraction> CODEC = BuilderCodec.builder(
        PlaceBlueprintInteraction.class, 
        PlaceBlueprintInteraction::new, 
        SimpleInstantInteraction.CODEC
    ).build();

    @Override
    protected void firstRun(@Nonnull InteractionType interactionType, 
                           @Nonnull InteractionContext context, 
                           @Nonnull CooldownHandler cooldownHandler) {
        
        CommandBuffer<EntityStore> commandBuffer = context.getCommandBuffer();
        if (commandBuffer == null) {
            context.getState().state = InteractionState.Failed;
            LOGGER.atWarning().log("CommandBuffer is null");
            return;
        }

        Ref<EntityStore> ref = context.getEntity();
        PlayerRef playerRef = (PlayerRef) commandBuffer.getComponent(ref, PlayerRef.getComponentType());
        if (playerRef == null) {
            context.getState().state = InteractionState.Failed;
            LOGGER.atWarning().log("PlayerRef is null");
            return;
        }

        ItemStack itemStack = context.getHeldItem();
        if (itemStack == null) {
            context.getState().state = InteractionState.Failed;
            LOGGER.atWarning().log("ItemStack is null");
            return;
        }

        String itemId = itemStack.getItemId();
        BlueprintItem blueprint = KingdomsPlugin.getInstance().getItemRegistry().getBlueprint(itemId);
        
        if (blueprint == null) {
            context.getState().state = InteractionState.Failed;
            LOGGER.atWarning().log("Unknown blueprint item: %s", itemId);
            return;
        }

        // Get the target block position from raycast
        BlockPosition targetBlock = context.getTargetBlock();
        if (targetBlock == null) {
            context.getState().state = InteractionState.Failed;
            LOGGER.atWarning().log("No target block - player must be looking at a valid surface");
            playerRef.sendMessage(Message.raw("§cYou must be looking at a valid surface to place a blueprint!"));
            return;
        }

        // Place construction site one block above the target (on top of the clicked block)
        int x = targetBlock.x;
        int y = targetBlock.y + 1;
        int z = targetBlock.z;

        LOGGER.atInfo().log("Placing blueprint %s at (%d, %d, %d)", blueprint.getDisplayName(), x, y, z);

        // Start construction
        KingdomsPlugin.getInstance().getBuildingManager()
            .startConstructionSilent(blueprint.getBuildingType(), x, y, z);

        // Send feedback to player
        playerRef.sendMessage(Message.raw(
            "§a⚒ Started construction of " + blueprint.getDisplayName() + "!"));

        // Success - don't set Failed state
    }
}
