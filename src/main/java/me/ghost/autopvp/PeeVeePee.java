package me.ghost.autopvp;

import me.ghost.autopvp.controllers.KAController;
import me.ghost.autopvp.task.tasks.GetSafeTask;
import me.ghost.autopvp.task.tasks.PathToTargetTask;
import me.ghost.autopvp.utils.BlockUtils;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.entity.SortPriority;
import meteordevelopment.meteorclient.utils.entity.TargetUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.player.PlayerEntity;

public class PeeVeePee extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();


    private KAController kaController = new KAController();

    private GetSafeTask gst;
    private PathToTargetTask pttt;

    public PeeVeePee() {
        super(AutoPVP.CATEGORY, "AutoPVP", "button pushing with no buttons");
    }
    public PlayerEntity target;


    private final Setting<Double> minHealth = sgGeneral.add(new DoubleSetting.Builder().name("min-combat-health").description("Min health to use combat modules").defaultValue(7).build());
    private final Setting<Double> targetRange = sgGeneral.add(new DoubleSetting.Builder().name("range").description("The maximum range the entity can be to attack it.").defaultValue(4.5).min(0).sliderMax(6).build());
    private final Setting<SortPriority> priority = sgGeneral.add(new EnumSetting.Builder<SortPriority>().name("priority").description("How to filter targets within range.").defaultValue(SortPriority.LowestHealth).build());



    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (TargetUtils.isBadTarget(target, targetRange.get())) { // targeting
            target = TargetUtils.getPlayerTarget(targetRange.get(), priority.get());
            if (TargetUtils.isBadTarget(target, targetRange.get())) doSafeStuff(); // if we can't target anybody get to a safe position and do misc tasks, else do regular combat stuff
        } else doCombatStuff();
    }

    public void doSafeStuff() {
        if (!BlockUtils.isSafePos(mc.player.getBlockPos())) { // keep the player in a safe position
            if (gst == null) gst = new GetSafeTask();
            gst.tick();
            if (gst.isComplete()) gst = null;
        }
        //todo tasks for health, armor dura management

    }


    public void doCombatStuff() {
        if (mc.player.distanceTo(target) >= targetRange.get() || !BlockUtils.isSafePos(mc.player.getBlockPos())) { // if the target is out of range, or we aren't safe
            if (pttt == null) pttt = new PathToTargetTask(target); // start pathing to a hole near the target
            pttt.tick();
            if (pttt.isComplete()) pttt = null;
        } else { // once the target is in range & we're safe, do the actual combat stuff
            //todo module controllers onTicks here
        }

    }


}
