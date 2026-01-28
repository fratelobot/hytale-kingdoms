package com.kingdoms.plugin.commands;

import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.player.Player;
import com.kingdoms.plugin.KingdomsPlugin;
import com.kingdoms.plugin.building.BuildingType;
import com.kingdoms.plugin.building.ConstructionSite;
import com.kingdoms.plugin.building.Building;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Command to manage buildings
 * 
 * Usage:
 *   /building build <type>  - Start construction at your position
 *   /building list          - List your constructions and buildings
 *   /building types         - Show available building types
 */
public class BuildingCommand extends CommandBase {

    private final KingdomsPlugin plugin;

    public BuildingCommand(KingdomsPlugin plugin) {
        super("building", "Manage your buildings");
        this.setPermissionGroup(GameMode.Adventure);
        this.plugin = plugin;
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        Player player = ctx.getPlayer();
        if (player == null) {
            ctx.sendMessage(Message.raw("§cThis command can only be used by players"));
            return;
        }

        String[] args = ctx.getArgs();
        
        if (args.length == 0) {
            showHelp(ctx);
            return;
        }

        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "build":
                handleBuild(ctx, player, args);
                break;
            case "list":
                handleList(ctx, player);
                break;
            case "types":
                handleTypes(ctx);
                break;
            default:
                showHelp(ctx);
        }
    }

    private void handleBuild(CommandContext ctx, Player player, String[] args) {
        if (args.length < 2) {
            ctx.sendMessage(Message.raw("§cUsage: /building build <type>"));
            ctx.sendMessage(Message.raw("§7Use /building types to see available types"));
            return;
        }

        String typeName = args[1].toUpperCase();
        BuildingType type;
        
        try {
            type = BuildingType.valueOf(typeName);
        } catch (IllegalArgumentException e) {
            ctx.sendMessage(Message.raw("§cUnknown building type: " + args[1]));
            ctx.sendMessage(Message.raw("§7Use /building types to see available types"));
            return;
        }

        // Get player position
        int x = (int) player.getPosition().getX();
        int y = (int) player.getPosition().getY();
        int z = (int) player.getPosition().getZ();

        // Start construction
        plugin.getBuildingManager().startConstruction(player, type, x, y, z);
    }

    private void handleList(CommandContext ctx, Player player) {
        List<ConstructionSite> constructions = plugin.getBuildingManager()
            .getPlayerConstructions(player.getUuid());
        List<Building> buildings = plugin.getBuildingManager()
            .getPlayerBuildings(player.getUuid());

        ctx.sendMessage(Message.raw("§6=== Your Buildings ==="));
        
        if (constructions.isEmpty() && buildings.isEmpty()) {
            ctx.sendMessage(Message.raw("§7You have no buildings yet."));
            ctx.sendMessage(Message.raw("§7Use /building build <type> to start construction!"));
            return;
        }

        if (!constructions.isEmpty()) {
            ctx.sendMessage(Message.raw("§e§lUnder Construction:"));
            for (ConstructionSite site : constructions) {
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

    private void handleTypes(CommandContext ctx) {
        ctx.sendMessage(Message.raw("§6=== Building Types ==="));
        for (BuildingType type : BuildingType.values()) {
            ctx.sendMessage(Message.raw(String.format("§a%s §7- %ds build time, %dx%d size",
                type.name().toLowerCase(),
                type.getBuildTimeSeconds(),
                type.getSizeX(),
                type.getSizeZ()
            )));
        }
    }

    private void showHelp(CommandContext ctx) {
        ctx.sendMessage(Message.raw("§6=== Kingdoms Building Commands ==="));
        ctx.sendMessage(Message.raw("§e/building build <type> §7- Start construction"));
        ctx.sendMessage(Message.raw("§e/building list §7- List your buildings"));
        ctx.sendMessage(Message.raw("§e/building types §7- Show building types"));
    }
}
