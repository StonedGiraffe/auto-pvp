package me.ghost.autopvp.utils;

import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class HoleUtils {

    public static boolean isPlayerSafe(PlayerEntity player) {
        return BlockUtils.isSafePos(player.getBlockPos());
    }

    public static BlockPos getHoleByTarget(PlayerEntity target) { // get the nearest safe hole (within combat range) of a target
        if (target == null) return null;
        BlockPos targetPos = target.getBlockPos();
        List<BlockPos> blocks = BlockUtils.getSphere(targetPos, 7, 3);
        blocks.removeIf(p -> p.equals(targetPos) || !BlockUtils.isSafePos(p) || BlockUtils.distanceBetween(targetPos, p) > 4.25D);
        filterOccupied(blocks);
        blocks.sort(Comparator.comparingDouble(PlayerUtils::distanceTo));
        if (blocks.isEmpty()) return null;
        else return blocks.get(0);
    }

    public static BlockPos getHoleBySelf() { // get the nearest safe hole
        BlockPos selfPos = mc.player.getBlockPos();
        List<BlockPos> blocks = BlockUtils.getSphere(selfPos, 7, 3);
        blocks.removeIf(p -> p.equals(selfPos) || !BlockUtils.isSafePos(p) || BlockUtils.distanceBetween(selfPos, p) > 6D);
        filterOccupied(blocks);
        blocks.sort(Comparator.comparingDouble(PlayerUtils::distanceTo));
        if (blocks.isEmpty()) return null;
        else return blocks.get(0);
    }

    public static void filterOccupied(List<BlockPos> blocks) { // remove holes occupied by other players
        ArrayList<BlockPos> occupied = new ArrayList<>();
        for (Entity e : mc.world.getEntities()) if (e instanceof PlayerEntity p) occupied.add(p.getBlockPos()); // get all nearby player positions
        blocks.removeIf(occupied::contains); // remove any that exist in the list
    }


}
