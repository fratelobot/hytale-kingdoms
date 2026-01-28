package com.kingdoms.plugin.building;

/**
 * Defines the visual representation of buildings
 * 
 * TODO: When we have access to Hytale's block API, replace string identifiers
 * with actual Block references
 */
public class BuildingVisuals {

    /**
     * Block types used for construction sites (scaffolding)
     */
    public static final String SCAFFOLD_BLOCK = "hytale:oak_planks";
    public static final String SCAFFOLD_FENCE = "hytale:oak_fence";
    
    /**
     * Block types for completed buildings
     */
    public static final String FOUNDATION_BLOCK = "hytale:stone_bricks";
    public static final String WALL_BLOCK = "hytale:stone_bricks";
    public static final String ROOF_BLOCK = "hytale:oak_planks";
    public static final String DOOR_BLOCK = "hytale:oak_door";

    /**
     * Get scaffold pattern for a construction site
     * Returns a 3D array of block positions relative to origin
     * 
     * Pattern for 3x3 scaffold:
     * Layer 0 (ground):
     *   P P P
     *   P P P
     *   P P P
     * 
     * Layer 1 (fences on corners):
     *   F . F
     *   . . .
     *   F . F
     */
    public static BlockPlacement[] getScaffoldPattern(BuildingType type) {
        int sizeX = type.getSizeX();
        int sizeZ = type.getSizeZ();
        
        // Calculate number of blocks needed
        int groundBlocks = sizeX * sizeZ;
        int fenceBlocks = 4; // corners
        BlockPlacement[] placements = new BlockPlacement[groundBlocks + fenceBlocks];
        
        int index = 0;
        
        // Ground layer (platform)
        for (int x = 0; x < sizeX; x++) {
            for (int z = 0; z < sizeZ; z++) {
                placements[index++] = new BlockPlacement(x, 0, z, SCAFFOLD_BLOCK);
            }
        }
        
        // Corner fences (layer 1)
        placements[index++] = new BlockPlacement(0, 1, 0, SCAFFOLD_FENCE);
        placements[index++] = new BlockPlacement(sizeX - 1, 1, 0, SCAFFOLD_FENCE);
        placements[index++] = new BlockPlacement(0, 1, sizeZ - 1, SCAFFOLD_FENCE);
        placements[index++] = new BlockPlacement(sizeX - 1, 1, sizeZ - 1, SCAFFOLD_FENCE);
        
        return placements;
    }

    /**
     * Get completed building pattern
     * Simple box structure for MVP
     */
    public static BlockPlacement[] getBuildingPattern(BuildingType type) {
        int sizeX = type.getSizeX();
        int sizeZ = type.getSizeZ();
        int height = 3; // Fixed height for MVP
        
        java.util.List<BlockPlacement> placements = new java.util.ArrayList<>();
        
        // Foundation (ground layer)
        for (int x = 0; x < sizeX; x++) {
            for (int z = 0; z < sizeZ; z++) {
                placements.add(new BlockPlacement(x, 0, z, FOUNDATION_BLOCK));
            }
        }
        
        // Walls (perimeter only, layers 1-2)
        for (int y = 1; y < height; y++) {
            for (int x = 0; x < sizeX; x++) {
                for (int z = 0; z < sizeZ; z++) {
                    // Only place on edges (perimeter)
                    if (x == 0 || x == sizeX - 1 || z == 0 || z == sizeZ - 1) {
                        // Leave space for door in front center
                        if (y == 1 && z == 0 && x == sizeX / 2) {
                            placements.add(new BlockPlacement(x, y, z, DOOR_BLOCK));
                        } else {
                            placements.add(new BlockPlacement(x, y, z, WALL_BLOCK));
                        }
                    }
                }
            }
        }
        
        // Roof (top layer)
        for (int x = 0; x < sizeX; x++) {
            for (int z = 0; z < sizeZ; z++) {
                placements.add(new BlockPlacement(x, height, z, ROOF_BLOCK));
            }
        }
        
        return placements.toArray(new BlockPlacement[0]);
    }

    /**
     * Represents a single block placement relative to building origin
     */
    public static class BlockPlacement {
        public final int offsetX;
        public final int offsetY;
        public final int offsetZ;
        public final String blockType;

        public BlockPlacement(int offsetX, int offsetY, int offsetZ, String blockType) {
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.offsetZ = offsetZ;
            this.blockType = blockType;
        }
    }
}
