package me.ghost.autopvp.task.combat;

import me.ghost.autopvp.utils.BlockUtils;
import me.ghost.autopvp.utils.Helper;
import me.ghost.autopvp.utils.HoleUtils;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.combat.AutoCity;
import meteordevelopment.meteorclient.utils.entity.TargetUtils;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import net.minecraft.entity.player.PlayerEntity;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class CityTask extends CombatTask{

    public CityTask() {
        super(Modules.get().get(AutoCity.class));
    }

    @Override
    public boolean selfCheck() {
        if (!Helper.getAutoPVP().useAutoCity.get()) return false;
        if (PlayerUtils.getTotalHealth() <= Helper.getMinHealth()) return false;
        return HoleUtils.isPlayerSafe(mc.player);
    }

    @Override
    public boolean targetCheck() {
        if (!Helper.getAutoPVP().useAutoCity.get()) return false;
        PlayerEntity target = Helper.getCurrentTarget();
        if (TargetUtils.isBadTarget(target, Helper.getTargetRange())) return false;
        if (!HoleUtils.isPlayerSafe(target)) return false; // Don't city unsurrounded players
        return !BlockUtils.isSafePos(target.getBlockPos());
    }
}
