package me.ghost.autopvp.controller;

import me.ghost.autopvp.PeeVeePee;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.combat.CrystalAura;
import meteordevelopment.meteorclient.systems.modules.combat.KillAura;
import meteordevelopment.meteorclient.systems.modules.player.AutoGap;

public class ControllerUtils {


    public static PeeVeePee getAutoPVP() {
        return Modules.get().get(PeeVeePee.class);
    }

    public static KillAura getKA() {
        return Modules.get().get(KillAura.class);
    }

    public static CrystalAura getCA() {
        return Modules.get().get(CrystalAura.class);
    }

    public static AutoGap getAutoGap() {
        return Modules.get().get(AutoGap.class);
    }

    public static void ensureSafePathing() {
        KillAura ka = getKA();
        CrystalAura ca = getCA();
        if (ka.isActive()) ka.toggle();
        if (ca.isActive()) ca.toggle();
    }

}
