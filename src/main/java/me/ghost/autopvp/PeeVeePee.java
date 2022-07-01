package me.ghost.autopvp;

import me.ghost.autopvp.controllers.KAController;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.player.PlayerEntity;

public class PeeVeePee extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();


    private KAController kaController;

    public PeeVeePee() {
        super(AutoPVP.CATEGORY, "AutoPVP", "button pushing with no buttons");
    }
    public PlayerEntity target;


    private final Setting<Double> minHealth = sgGeneral.add(new DoubleSetting.Builder().name("min-combat-health").description("Min health to use combat modules").defaultValue(7).build());


    private void init() { // init da controllers
        kaController = new KAController();
        kaController.minHealth = minHealth.get();
        kaController.onTick();
    }



}
