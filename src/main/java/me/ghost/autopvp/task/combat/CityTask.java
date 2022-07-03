package me.ghost.autopvp.task.combat;

import me.ghost.autopvp.utils.BlockUtils;
import me.ghost.autopvp.utils.Helper;
import me.ghost.autopvp.utils.HoleUtils;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.combat.AutoCity;
import meteordevelopment.meteorclient.utils.entity.TargetUtils;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import net.minecraft.entity.player.PlayerEntity;

public class CityTask extends CombatTask{

    public CityTask() {
        super(Modules.get().get(AutoCity.class));
    }

    @Override
    public boolean selfCheck() {
        if (!Helper.getAutoPVP().useCrystalAura.get()) return false;
        if (PlayerUtils.getTotalHealth() <= Helper.getMinHealth()) return false;
        return HoleUtils.isPlayerSafe();
    }

    @Override
    public boolean targetCheck() {
        if (!Helper.getAutoPVP().useCrystalAura.get()) return false;
        PlayerEntity target = Helper.getCurrentTarget();
        if (TargetUtils.isBadTarget(target, Helper.getTargetRange())) return false;
        return !BlockUtils.isSafePos(target.getBlockPos());
    }
}
