package com.kingdoms.plugin.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.kingdoms.plugin.KingdomsPlugin;
import com.kingdoms.plugin.building.BuildingType;

import javax.annotation.Nonnull;

/**
 * Command to build a Town Hall at the player's position
 * Usage: /build_townhall
 */
public class BuildTownHallCommand extends AbstractPlayerCommand {

    private final KingdomsPlugin plugin;

    public BuildTownHallCommand(KingdomsPlugin plugin) {
        super("build_townhall", "server.commands.kingdoms.build_townhall.desc");
        this.setPermissionGroup(GameMode.Adventure);
        this.plugin = plugin;
    }

    @Override
    protected void execute(@Nonnull CommandContext context, 
                          @Nonnull Store<EntityStore> store, 
                          @Nonnull Ref<EntityStore> ref, 
                          @Nonnull PlayerRef playerRef, 
                          @Nonnull World world) {
        
        // Get player position from TransformComponent
        TransformComponent transform = (TransformComponent) store.getComponent(ref, TransformComponent.getComponentType());
        assert transform != null;
        
        Vector3d position = transform.getPosition();
        int x = (int) position.getX();
        int y = (int) position.getY();
        int z = (int) position.getZ();
        
        plugin.getBuildingManager().startConstruction(context, BuildingType.TOWN_HALL, x, y, z);
    }
}
