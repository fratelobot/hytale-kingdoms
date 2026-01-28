package com.kingdoms.plugin.world;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.kingdoms.plugin.building.BuildingVisuals;
import com.kingdoms.plugin.building.BuildingVisuals.BlockPlacement;

/**
 * Service for world manipulation (placing/removing blocks)
 * Uses Hytale's WorldChunk API for block placement
 */
public class WorldService {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    /**
     * Place blocks in the world at the specified location
     * 
     * @param chunk The world chunk to modify
     * @param worldX Base X coordinate
     * @param worldY Base Y coordinate  
     * @param worldZ Base Z coordinate
     * @param placements Array of block placements (relative to base)
     * @param blockTypeResolver Function to resolve block type from string ID
     */
    public void placeBlocks(WorldChunk chunk, int worldX, int worldY, int worldZ, 
            BlockPlacement[] placements, BlockTypeResolver blockTypeResolver) {
        
        LOGGER.atInfo().log("Placing %d blocks at base (%d, %d, %d)", 
            placements.length, worldX, worldY, worldZ);

        int placed = 0;
        for (BlockPlacement placement : placements) {
            int x = worldX + placement.offsetX;
            int y = worldY + placement.offsetY;
            int z = worldZ + placement.offsetZ;
            
            BlockType blockType = blockTypeResolver.resolve(placement.blockType);
            if (blockType != null) {
                boolean success = chunk.setBlock(x, y, z, blockType);
                if (success) {
                    placed++;
                }
            } else {
                LOGGER.atWarning().log("Unknown block type: %s", placement.blockType);
            }
        }
        
        LOGGER.atInfo().log("Successfully placed %d/%d blocks", placed, placements.length);
    }

    /**
     * Place blocks using string block IDs (logs only if chunk not available)
     * Fallback method when we don't have direct chunk access
     */
    public void placeBlocksLogged(int worldX, int worldY, int worldZ, BlockPlacement[] placements) {
        LOGGER.atInfo().log("Placing %d blocks at base (%d, %d, %d)", 
            placements.length, worldX, worldY, worldZ);

        for (BlockPlacement placement : placements) {
            int x = worldX + placement.offsetX;
            int y = worldY + placement.offsetY;
            int z = worldZ + placement.offsetZ;
            
            LOGGER.atInfo().log("  -> Block at (%d, %d, %d): %s", x, y, z, placement.blockType);
        }
    }

    /**
     * Replace scaffold blocks with building blocks
     * Used when construction completes
     */
    public void replaceScaffoldWithBuilding(WorldChunk chunk, int worldX, int worldY, int worldZ, 
            BlockPlacement[] scaffoldBlocks, BlockPlacement[] buildingBlocks,
            BlockTypeResolver blockTypeResolver) {
        
        LOGGER.atInfo().log("Replacing scaffold with completed building at (%d, %d, %d)", 
            worldX, worldY, worldZ);
        
        // Clear scaffold area first (set to air)
        // Then place building blocks
        placeBlocks(chunk, worldX, worldY, worldZ, buildingBlocks, blockTypeResolver);
    }

    /**
     * Fallback method for replacing when chunk not available
     */
    public void replaceScaffoldWithBuildingLogged(int worldX, int worldY, int worldZ, 
            BlockPlacement[] scaffoldBlocks, BlockPlacement[] buildingBlocks) {
        
        LOGGER.atInfo().log("Replacing scaffold with completed building at (%d, %d, %d)", 
            worldX, worldY, worldZ);
        
        placeBlocksLogged(worldX, worldY, worldZ, buildingBlocks);
    }

    /**
     * Functional interface for resolving block type strings to BlockType objects
     */
    @FunctionalInterface
    public interface BlockTypeResolver {
        BlockType resolve(String blockTypeId);
    }
}
