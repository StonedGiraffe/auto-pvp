package me.ghost.autopvp.task.safety;

import me.ghost.autopvp.PeeVeePee;
import me.ghost.autopvp.utils.Helper;
import me.ghost.autopvp.utils.HoleUtils;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.player.AutoGap;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;

public class GapTask extends SafetyTask {


    public GapTask() {
        super(Modules.get().get(AutoGap.class));
    }

    @Override
    public boolean check() {
        PeeVeePee p = Helper.getAutoPVP();
        return p.useAutoGap.get() && HoleUtils.isPlayerSafe() && !(PlayerUtils.getTotalHealth() > p.autoGapHP.get());
    }
}
