package com.kingdoms.plugin.commands;

import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.kingdoms.plugin.KingdomsPlugin;
import com.kingdoms.plugin.building.BuildingType;

import javax.annotation.Nonnull;

/**
 * Command to build a Town Hall at the player's position
 * Usage: /build_townhall
 */
public class BuildTownHallCommand extends CommandBase {

    private final KingdomsPlugin plugin;

    public BuildTownHallCommand(KingdomsPlugin plugin) {
        super("build_townhall", "Start construction of a Town Hall at your position");
        this.setPermissionGroup(GameMode.Adventure);
        this.plugin = plugin;
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        CommandSender sender = ctx.sender();
        
        int x, y, z;
        
        // Try to get player position if sender is a player
        if (sender instanceof Player) {
            Player player = (Player) sender;
            // Get player's block position
            var pos = player.getBlockPosition();
            x = pos.getX();
            y = pos.getY();
            z = pos.getZ();
            
            ctx.sender().sendMessage(Message.raw("§7Building at your position: (" + x + ", " + y + ", " + z + ")"));
        } else {
            // Fallback position for console
            x = 0;
            y = 64;
            z = 0;
            ctx.sender().sendMessage(Message.raw("§7Building at default position: (" + x + ", " + y + ", " + z + ")"));
        }
        
        plugin.getBuildingManager().startConstruction(ctx, BuildingType.TOWN_HALL, x, y, z);
    }
}
