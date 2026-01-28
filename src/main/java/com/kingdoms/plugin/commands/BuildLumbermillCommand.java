package com.kingdoms.plugin.commands;

import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.kingdoms.plugin.KingdomsPlugin;
import com.kingdoms.plugin.building.BuildingType;

import javax.annotation.Nonnull;

/**
 * Quick command to build a Lumbermill
 * Usage: /build_lumbermill
 */
public class BuildLumbermillCommand extends CommandBase {

    private final KingdomsPlugin plugin;

    public BuildLumbermillCommand(KingdomsPlugin plugin) {
        super("build_lumbermill", "Build a Lumbermill");
        this.setPermissionGroup(GameMode.Adventure);
        this.plugin = plugin;
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        // Place at offset from origin to avoid overlap with Town Hall
        int x = 10;
        int y = 64;
        int z = 0;
        
        plugin.getBuildingManager().startConstruction(ctx, BuildingType.LUMBERMILL, x, y, z);
    }
}
