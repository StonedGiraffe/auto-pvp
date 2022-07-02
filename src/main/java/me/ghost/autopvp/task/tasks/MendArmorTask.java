package me.ghost.autopvp.task.tasks;

import me.ghost.autopvp.task.Task;
import me.ghost.autopvp.utils.BlockUtils;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.combat.AutoEXP;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class MendArmorTask extends Task {


    public MendArmorTask() {
        super("MendArmor");
    }

    @Override
    public void tick() {
        //todo finish
        if (!this.shouldRun() || !BlockUtils.isSafePos(mc.player.getBlockPos())) return;
        AutoEXP autoXP = Modules.get().get(AutoEXP.class);
    }
}
