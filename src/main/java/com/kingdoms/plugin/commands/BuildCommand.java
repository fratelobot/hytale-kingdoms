package com.kingdoms.plugin.commands;

import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.kingdoms.plugin.KingdomsPlugin;
import com.kingdoms.plugin.building.BuildingType;

import javax.annotation.Nonnull;

/**
 * Generic build command that accepts building type and coordinates
 * Usage: /build <type> <x> <y> <z>
 * 
 * Example: /build town_hall 100 64 200
 */
public class BuildCommand extends CommandBase {

    private final KingdomsPlugin plugin;

    public BuildCommand(KingdomsPlugin plugin) {
        super("build", "Build a structure: /build <type> <x> <y> <z>");
        this.setPermissionGroup(GameMode.Adventure);
        this.plugin = plugin;
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        // Parse command arguments from the raw input
        // Format: /build <type> <x> <y> <z>
        
        ctx.sendMessage(Message.raw("§6=== Kingdoms Build Command ==="));
        ctx.sendMessage(Message.raw("§eUsage: /build <type> <x> <y> <z>"));
        ctx.sendMessage(Message.raw("§7"));
        ctx.sendMessage(Message.raw("§7Available types:"));
        
        for (BuildingType type : BuildingType.values()) {
            ctx.sendMessage(Message.raw(String.format("§a  %s §7(%ds, %dx%d)",
                type.name().toLowerCase(),
                type.getBuildTimeSeconds(),
                type.getSizeX(),
                type.getSizeZ()
            )));
        }
        
        ctx.sendMessage(Message.raw("§7"));
        ctx.sendMessage(Message.raw("§7Quick commands:"));
        ctx.sendMessage(Message.raw("§e  /build_townhall §7- Build at (0, 64, 0)"));
        ctx.sendMessage(Message.raw("§e  /build_lumbermill §7- Build at (10, 64, 0)"));
        ctx.sendMessage(Message.raw("§e  /build_farm §7- Build at (20, 64, 0)"));
    }
}
