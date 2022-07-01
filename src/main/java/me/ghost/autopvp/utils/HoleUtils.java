package me.ghost.autopvp.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class HoleUtils {

    public static BlockPos getHoleByTarget(PlayerEntity target) {
        if (target == null) return null;
        BlockPos targetPos = target.getBlockPos();
        List<BlockPos> blocks = BlockUtils.getSphere(targetPos, 7, 3);
        if (blocks.isEmpty()) return null;
        blocks.removeIf(p -> p.equals(targetPos) || !BlockUtils.isSafePos(p) || BlockUtils.distanceBetween(targetPos, p) > 4.25D);
        return null;//todo finish
    }


}
