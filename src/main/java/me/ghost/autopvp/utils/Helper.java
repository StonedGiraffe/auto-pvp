package me.ghost.autopvp.utils;

import me.ghost.autopvp.PeeVeePee;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.combat.Surround;
import net.minecraft.entity.player.PlayerEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Helper {

    public static ExecutorService ex = Executors.newCachedThreadPool();

    public static PeeVeePee getAutoPVP() {
        return Modules.get().get(PeeVeePee.class);
    }

    public static Surround getSurround() {
        return Modules.get().get(Surround.class);
    }

    public static double getTargetRange() {
        return getAutoPVP().targetRange.get();
    }

    public static double getMinHealth() {
        return getAutoPVP().minCombatHealth.get();
    }

    public static PlayerEntity getCurrentTarget() {
        return getAutoPVP().target;
    }


    public static void sleep(long d) {
        try {
            TimeUnit.MILLISECONDS.sleep(d);
        } catch (Exception ignored) {}
    }

    public static void quickSurround() {
        Surround s = getSurround();
        if (!s.isActive()) s.toggle();
        ex.execute(() -> {
            sleep(300);
            Surround ns = getSurround();
            if (ns.isActive()) ns.toggle();
        });
    }

}
