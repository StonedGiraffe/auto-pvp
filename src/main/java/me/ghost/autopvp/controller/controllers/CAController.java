package me.ghost.autopvp.controller.controllers;

import me.ghost.autopvp.PeeVeePee;
import me.ghost.autopvp.controller.Controller;
import me.ghost.autopvp.controller.ControllerUtils;
import me.ghost.autopvp.utils.BlockUtils;
import me.ghost.autopvp.utils.HoleUtils;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.combat.CrystalAura;
import meteordevelopment.meteorclient.utils.entity.EntityUtils;
import meteordevelopment.meteorclient.utils.entity.TargetUtils;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import net.minecraft.entity.player.PlayerEntity;


public class CAController extends Controller {

    public double minHealth = 0.0D;
    public PeeVeePee p;

    public CAController() {
        super("CrystalAura");
    }

    @Override
    public void tick() {
        p = ControllerUtils.getAutoPVP();
        PlayerEntity target = p.target; // get target from AutoPVP
        CrystalAura ca = Modules.get().get(CrystalAura.class);
        if (!HoleUtils.isPlayerSafe() || TargetUtils.isBadTarget(target, 4.5D) || PlayerUtils.getTotalHealth() < minHealth || !this.isActive() || !p.useCrystalAura.get()) {
            if (ca.isActive()) ca.toggle(); // disable ca if we're unsafe or can't target
            this.deactivate();
        }
        if (shouldCA(target)) ca.toggle(); // enable ca if we should
    }

    public boolean shouldCA(PlayerEntity target) {
        if (EntityUtils.getTotalHealth(target) <= 10) return true;
        return !BlockUtils.isSafePos(target.getBlockPos());
    }
}
