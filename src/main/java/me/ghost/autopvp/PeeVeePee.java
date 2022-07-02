package me.ghost.autopvp;

import me.ghost.autopvp.controller.Controller;
import me.ghost.autopvp.controller.controllers.AutoGapController;
import me.ghost.autopvp.controller.controllers.CAController;
import me.ghost.autopvp.controller.controllers.KAController;
import me.ghost.autopvp.task.tasks.GetSafeTask;
import me.ghost.autopvp.task.tasks.PathToTargetTask;
import me.ghost.autopvp.utils.BlockUtils;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.entity.SortPriority;
import meteordevelopment.meteorclient.utils.entity.TargetUtils;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;

public class PeeVeePee extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgCombat = settings.createGroup("Combat");
    private final SettingGroup sgSafety = settings.createGroup("Safety");

    // controllers
    public static KAController kaControl = new KAController();
    public static CAController caControl = new CAController();
    public static AutoGapController gapControl = new AutoGapController();
    public static List<Controller> pvpControllers = List.of(kaControl, caControl);

    private GetSafeTask gst;
    private PathToTargetTask pttt;


    public PlayerEntity target;

    public enum State {SafePath, TargetPath, Combat, Idle}
    public State state = State.SafePath;

    public final Setting<Double> minHealth = sgGeneral.add(new DoubleSetting.Builder().name("min-combat-health").description("Min health to use combat modules").defaultValue(7).build());
    public final Setting<Double> targetRange = sgGeneral.add(new DoubleSetting.Builder().name("target-range").description("The maximum range the entity can be to attack it.").defaultValue(4.5).min(0).sliderMax(6).build());
    private final Setting<SortPriority> priority = sgGeneral.add(new EnumSetting.Builder<SortPriority>().name("priority").description("How to filter targets within range.").defaultValue(SortPriority.LowestHealth).build());

    private final Setting<Double> combatHoleRange = sgCombat.add(new DoubleSetting.Builder().name("max-combat-hole-range").description("Max distance between safe holes and the target").defaultValue(4.5).min(0).sliderMax(6).build());
    public final Setting<Boolean> useKillaura = sgCombat.add(new BoolSetting.Builder().name("use-killaura").defaultValue(true).build());
    public final Setting<Boolean> useCrystalAura = sgCombat.add(new BoolSetting.Builder().name("use-crystalaura").defaultValue(true).build());
    //private final Setting<Boolean> useBedAura = sgCombat.add(new BoolSetting.Builder().name("use-bedaura").defaultValue(true).build());
    //private final Setting<Boolean> useAnchorAura = sgCombat.add(new BoolSetting.Builder().name("use-anchoraura").defaultValue(true).build());

    public final Setting<Double> safeHoleRange = sgSafety.add(new DoubleSetting.Builder().name("max-safe-hole-range").description("Max distance between your position and safe holes").defaultValue(7.5).min(0).sliderMax(6).build());
    public final Setting<Boolean> useAutoGap = sgSafety.add(new BoolSetting.Builder().name("use-autogap").defaultValue(true).build());

    public PeeVeePee() {
        super(AutoPVP.CATEGORY, "AutoPVP", "button pushing with no buttons");
    }

    @Override
    public void onActivate() {
        kaControl = new KAController();
        caControl = new CAController();
        kaControl.minHealth = minHealth.get();
        caControl.minHealth = minHealth.get();
        pvpControllers = List.of(kaControl, caControl);
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (targetCheck() && PlayerUtils.getTotalHealth() > minHealth.get()) doCombatStuff();
        else doSafeStuff();
    }

    private boolean targetCheck() {
        if (TargetUtils.isBadTarget(target, targetRange.get())) { // if the current target is invalid
            target = TargetUtils.getPlayerTarget(targetRange.get(), priority.get());
            return false;
        }
        return true;
    }


    private void doSafeStuff() {
        pvpControllers.forEach(Controller::deactivate); // make sure all pvp controllers do nothing
        if (!BlockUtils.isSafePos(mc.player.getBlockPos())) { // only worry if the current pos isn't safe
            state = State.SafePath;
            if (gst == null) gst = new GetSafeTask(); // get to a safe spot
            gst.enable();
            gst.tick();
            if (gst.isComplete()) {
                gst.reset();
                gst.disable();
            }
        } else {
            gst.reset();
            gst.disable();
            gapControl.activate();
            gapControl.tick();
            state = State.Idle;
        }
        //todo tasks for health, armor dura management
    }


    private void doCombatStuff() {
        if (mc.player.distanceTo(target) >= targetRange.get() || !BlockUtils.isSafePos(mc.player.getBlockPos())) { // if the target is out of range, or we aren't safe
            state = State.TargetPath;
            if (pttt == null) pttt = new PathToTargetTask(target); // start pathing to a hole near the target
            pttt.enable();
            pttt.tick();
            if (pttt.isComplete()) {
                pttt.reset();
                pttt.disable();
            }
        } else { // once the target is in range & we're safe, do the actual combat stuff
            pttt.reset();
            pttt.disable();
            state = State.Combat;
            pvpControllers.forEach(controller -> { // (re)activate + tick pvp controllers
                controller.activate();
                controller.tick();
            });
        }

    }

    @Override
    public String getInfoString() {
        String rev = "cope";
        switch (state) {
            case Idle -> rev = "Idle.";
            case TargetPath -> rev = "PathToTarget";
            case SafePath -> rev = "PathToSafeHole";
            case Combat -> rev = "InCombat";
        }
        return rev;
    }

}
