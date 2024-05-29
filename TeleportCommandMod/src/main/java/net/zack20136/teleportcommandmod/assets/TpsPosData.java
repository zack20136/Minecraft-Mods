package net.zack20136.teleportcommandmod.assets;

import net.minecraft.util.math.BlockPos;

public class TpsPosData {
    private BlockPos pos;
    private String dimension;
    private String description;

    public TpsPosData(BlockPos pos, String dimension, String description) {
        this.pos = pos;
        this.dimension = dimension;
        this.description = description;
    }

    public BlockPos getPos() {
        return pos;
    }
    public String getDimension() { return dimension; }
    public String getDescription() {
        return description;
    }
}
