package me.ghost.autopvp.controller.controllers;

import me.ghost.autopvp.PeeVeePee;
import me.ghost.autopvp.controller.Controller;
import me.ghost.autopvp.controller.ControllerUtils;
import me.ghost.autopvp.utils.HoleUtils;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.combat.KillAura;
import meteordevelopment.meteorclient.utils.entity.TargetUtils;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import net.minecraft.entity.player.PlayerEntity;

public class KAController extends Controller {

    public double minHealth = 0.0D;
    public PeeVeePee p;

    public KAController() {
        super("KillAura");
    }

    @Override
    public void tick() {
        p = ControllerUtils.getAutoPVP();
        PlayerEntity target = p.target; // get target from AutoPVP
        KillAura ka = Modules.get().get(KillAura.class);
        if (!HoleUtils.isPlayerSafe() || TargetUtils.isBadTarget(target, 4.25D) || PlayerUtils.getTotalHealth() < minHealth || !this.isActive() || !p.useKillaura.get()) {
            if (ka.isActive()) ka.toggle(); // disable if we aren't safe or the target is bad
            this.deactivate();
            return;
        }
        if (!ka.isActive()) ka.toggle(); // enable otherwise
    }

    @Override
    public void activate() {
        this.active = true;
        this.tick();
    }

    @Override
    public void deactivate() {
        this.active = false;
        KillAura ka = Modules.get().get(KillAura.class);
        if (ka.isActive()) ka.toggle();
    }
}
