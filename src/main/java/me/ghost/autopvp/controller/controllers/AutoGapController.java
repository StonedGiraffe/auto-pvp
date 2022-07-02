package me.ghost.autopvp.controller.controllers;

import me.ghost.autopvp.AutoPVP;
import me.ghost.autopvp.PeeVeePee;
import me.ghost.autopvp.controller.Controller;
import me.ghost.autopvp.controller.ControllerUtils;
import me.ghost.autopvp.utils.HoleUtils;
import meteordevelopment.meteorclient.systems.modules.player.AutoGap;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;

public class AutoGapController extends Controller {

    public AutoGapController() {
        super("AutoGap");
    }

    @Override
    public void activate() {
        if (!HoleUtils.isPlayerSafe() ) return;
        AutoGap gap = ControllerUtils.getAutoGap();
        if (!gap.isActive()) gap.toggle();
        this.active = true;
    }

    @Override
    public void deactivate() {
        AutoGap gap = ControllerUtils.getAutoGap();
        if (gap.isActive()) gap.toggle();
        this.active = false;
    }

    @Override
    public void tick() {
        AutoGap gap = ControllerUtils.getAutoGap();
        PeeVeePee pvp = ControllerUtils.getAutoPVP();
        if (!pvp.useAutoGap.get() || PlayerUtils.getTotalHealth() > pvp.minHealth.get() + 0.8D || !HoleUtils.isPlayerSafe()) {
            this.deactivate();
            return;
        }
        if (HoleUtils.isPlayerSafe() && !gap.isActive()) gap.toggle();
    }
}
