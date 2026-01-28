package com.kingdoms.plugin.commands;

import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.kingdoms.plugin.KingdomsPlugin;
import com.kingdoms.plugin.building.BuildingType;

import javax.annotation.Nonnull;

/**
 * Command to build a Town Hall
 * Usage: /build_townhall
 * 
 * TODO: Get actual player position when API is available
 */
public class BuildTownHallCommand extends CommandBase {

    private final KingdomsPlugin plugin;

    public BuildTownHallCommand(KingdomsPlugin plugin) {
        super("build_townhall", "Start construction of a Town Hall");
        this.setPermissionGroup(GameMode.Adventure);
        this.plugin = plugin;
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        // TODO: Get player position from context when API is available
        // For now, use fixed position
        int x = 0;
        int y = 64;
        int z = 0;
        
        plugin.getBuildingManager().startConstruction(ctx, BuildingType.TOWN_HALL, x, y, z);
    }
}
