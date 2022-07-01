package me.ghost.autopvp.utils;

import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.world.CardinalDirection;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class BlockUtils {

    public static boolean isSafePos(BlockPos pos) {
        if (!getState(pos).isAir() || !getState(pos.up()).isAir() || !getState(pos.up(2)).isAir()) return false;
        for (CardinalDirection c : CardinalDirection.values()) {
            if (getBlock(pos.offset(c.toDirection())).getBlastResistance() < 600) return false;
        }
        return true;
    }


    public static List<BlockPos> getSphere(BlockPos centerPos, int radius, int height) {
        ArrayList<BlockPos> blocks = new ArrayList<>();
        for (int i = centerPos.getX() - radius; i < centerPos.getX() + radius; i++) {
            for (int j = centerPos.getY() - height; j < centerPos.getY() + height; j++) {
                for (int k = centerPos.getZ() - radius; k < centerPos.getZ() + radius; k++) {
                    BlockPos pos = new BlockPos(i, j, k);
                    if (distanceBetween(centerPos, pos) <= radius && !blocks.contains(pos)) blocks.add(pos);
                }
            }
        }
        return blocks;
    }

    public static Block getBlock(BlockPos p) {return mc.world.getBlockState(p).getBlock();}
    public static BlockState getState(BlockPos p) {return mc.world.getBlockState(p);}


    public static double distanceTo(BlockPos pos) {
        return distanceBetween(mc.player.getBlockPos(), pos);
    }

    public static double distanceTo(Vec3d vec) {
        return distance(Utils.vec3d(mc.player.getBlockPos()), vec);
    }

    public static double distanceBetween(BlockPos pos1, BlockPos pos2) {
        double d = pos1.getX() - pos2.getX();
        double e = pos1.getY() - pos2.getY();
        double f = pos1.getZ() - pos2.getZ();
        return MathHelper.sqrt((float) (d * d + e * e + f * f));
    }

    public static double distance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double dX = x2 - x1;
        double dY = y2 - y1;
        double dZ = z2 - z1;
        return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
    }

    public static double distance(double y1, double y2) {
        double dY = y2 - y1;
        return Math.sqrt(dY * dY);
    }

    public static double distance(Vec3d vec1, Vec3d vec2) {
        double dX = vec2.x - vec1.x;
        double dY = vec2.y - vec1.y;
        double dZ = vec2.z - vec1.z;
        return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
    }
}
