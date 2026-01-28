package com.kingdoms.plugin.commands;

import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.kingdoms.plugin.KingdomsPlugin;
import com.kingdoms.plugin.building.BuildingType;

import javax.annotation.Nonnull;

/**
 * Command to show available building types
 * Usage: /buildings
 */
public class BuildingCommand extends CommandBase {

    public BuildingCommand(KingdomsPlugin plugin) {
        super("buildings", "Show available building types in Kingdoms");
        this.setPermissionGroup(GameMode.Adventure);
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        ctx.sendMessage(Message.raw("§6=== Kingdoms Building Types ==="));
        for (BuildingType type : BuildingType.values()) {
            ctx.sendMessage(Message.raw(String.format("§a%s §7- %ds build time, %dx%d size",
                type.name().toLowerCase(),
                type.getBuildTimeSeconds(),
                type.getSizeX(),
                type.getSizeZ()
            )));
        }
        ctx.sendMessage(Message.raw("§7Use /build_<type> to start construction"));
    }
}
