package com.kingdoms.plugin.commands;

import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.kingdoms.plugin.KingdomsPlugin;
import com.kingdoms.plugin.building.BuildingType;

import javax.annotation.Nonnull;

/**
 * Quick command to build a Farm
 * Usage: /build_farm
 */
public class BuildFarmCommand extends CommandBase {

    private final KingdomsPlugin plugin;

    public BuildFarmCommand(KingdomsPlugin plugin) {
        super("build_farm", "Build a Farm");
        this.setPermissionGroup(GameMode.Adventure);
        this.plugin = plugin;
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        // Place at offset from origin
        int x = 20;
        int y = 64;
        int z = 0;
        
        plugin.getBuildingManager().startConstruction(ctx, BuildingType.FARM, x, y, z);
    }
}
