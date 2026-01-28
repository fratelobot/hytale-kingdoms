package com.kingdoms.plugin.commands;

import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.kingdoms.plugin.KingdomsPlugin;
import com.kingdoms.plugin.building.Building;
import com.kingdoms.plugin.building.ConstructionSite;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Command to list all buildings and construction sites
 * Usage: /list_buildings
 */
public class ListBuildingsCommand extends CommandBase {

    private final KingdomsPlugin plugin;

    public ListBuildingsCommand(KingdomsPlugin plugin) {
        super("list_buildings", "List all buildings and constructions");
        this.setPermissionGroup(GameMode.Adventure);
        this.plugin = plugin;
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        List<ConstructionSite> sites = plugin.getBuildingManager().getConstructionSites();
        List<Building> buildings = plugin.getBuildingManager().getCompletedBuildings();
        
        ctx.sendMessage(Message.raw("§6=== Kingdoms Buildings ==="));
        
        if (sites.isEmpty() && buildings.isEmpty()) {
            ctx.sendMessage(Message.raw("§7No buildings yet. Use /build_townhall to start!"));
            return;
        }
        
        if (!sites.isEmpty()) {
            ctx.sendMessage(Message.raw("§e§lUnder Construction:"));
            for (ConstructionSite site : sites) {
                ctx.sendMessage(Message.raw("§7 - " + site.toString()));
            }
        }
        
        if (!buildings.isEmpty()) {
            ctx.sendMessage(Message.raw("§a§lCompleted:"));
            for (Building building : buildings) {
                ctx.sendMessage(Message.raw("§7 - " + building.toString()));
            }
        }
    }
}
