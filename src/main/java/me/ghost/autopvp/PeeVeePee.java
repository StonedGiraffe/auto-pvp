package me.ghost.autopvp;


import me.ghost.autopvp.task.combat.CATask;
import me.ghost.autopvp.task.pathing.PathStatus;
import me.ghost.autopvp.task.pathing.PathingTask;
import me.ghost.autopvp.task.safety.GapTask;
import me.ghost.autopvp.utils.Helper;
import me.ghost.autopvp.utils.HoleUtils;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.entity.SortPriority;
import meteordevelopment.meteorclient.utils.entity.TargetUtils;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class PeeVeePee extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgCombat = settings.createGroup("Combat");
    private final SettingGroup sgSafety = settings.createGroup("Safety");


    // Pathing
    private final PathingTask targetPathing = new PathingTask();
    private final PathingTask safePathing = new PathingTask();

    // Combat Tasks
    private final CATask ca = new CATask();

    // Safety Tasks
    private final GapTask autoGap = new GapTask();

    public PlayerEntity target;

    public enum State {SafePath, TargetPath, Combat, Idle}
    public State state = State.SafePath;
    public String ext = "";

    public final Setting<Double> minCombatHealth = sgGeneral.add(new DoubleSetting.Builder().name("min-combat-health").description("Min health to use combat modules").defaultValue(15).build());
    public final Setting<Double> distRatio = sgGeneral.add(new DoubleSetting.Builder().name("dist-ratio").description("what distance to maintain from your target").defaultValue(3).min(0).sliderMax(6).build());
    public final Setting<Double> targetRange = sgGeneral.add(new DoubleSetting.Builder().name("target-range").description("Max range for targeting").defaultValue(4.5).min(0).sliderMax(6).build());
    private final Setting<SortPriority> priority = sgGeneral.add(new EnumSetting.Builder<SortPriority>().name("priority").description("How to filter targets within range.").defaultValue(SortPriority.LowestHealth).build());

    //private final Setting<Double> combatHoleRange = sgCombat.add(new DoubleSetting.Builder().name("max-combat-hole-range").description("Max distance between safe holes and the target").defaultValue(4.5).min(0).sliderMax(6).build());
    public final Setting<Boolean> useKillaura = sgCombat.add(new BoolSetting.Builder().name("use-killaura").defaultValue(true).build());
    public final Setting<Boolean> useCrystalAura = sgCombat.add(new BoolSetting.Builder().name("use-crystalaura").defaultValue(true).build());
    //private final Setting<Boolean> useBedAura = sgCombat.add(new BoolSetting.Builder().name("use-bedaura").defaultValue(true).build());
    //private final Setting<Boolean> useAnchorAura = sgCombat.add(new BoolSetting.Builder().name("use-anchoraura").defaultValue(true).build());

    //public final Setting<Double> safeHoleRange = sgSafety.add(new DoubleSetting.Builder().name("max-safe-hole-range").description("Max distance between your position and safe holes").defaultValue(7.5).min(0).sliderMax(6).build());
    public final Setting<Boolean> surroundFallback = sgSafety.add(new BoolSetting.Builder().name("allow-surround-fallback").defaultValue(true).build());
    public final Setting<Boolean> useAutoGap = sgSafety.add(new BoolSetting.Builder().name("use-autogap").defaultValue(true).build());
    public final Setting<Double> autoGapHP = sgGeneral.add(new DoubleSetting.Builder().name("autogap-hp").description("What health auto gap activates on").defaultValue(10).build());


    public PeeVeePee() {
        super(AutoPVP.CATEGORY, "AutoPVP", "button pushing with no buttons");
    }

    @Override
    public void onActivate() {
        targetPathing.reset();
        safePathing.reset();
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (!isSafe()) { // first make sure our current pos is safe
            unsafeModCheck();
            if (hasTarget()) targetPathSeq(); // path to a safe hole by the target / by ourselves
            else safePathSeq();
        } else { // if we are safe
            if (PlayerUtils.getTotalHealth() <= minCombatHealth.get() || !hasTarget()) safeSeq(); // if we're below min health or without a target, do safe stuff
            else combatSeq(); // if our health is good, and we have a target, do combat stuff
        }
    }


    private void unsafeModCheck() {
        autoGap.disableModule();
        ca.disableModule();
    }

    private void targetPathSeq() {
        state = State.TargetPath;
        PathStatus tStatus = targetPathing.getStatus(); // check current status
        if (tStatus == PathStatus.Init_NoGoal || tStatus == PathStatus.Invalidated || tStatus == PathStatus.Complete) { // reset goal
            BlockPos bestHole = HoleUtils.getHoleByTarget(target);
            if (bestHole != null) targetPathing.setGoal(bestHole);
            else safePathSeq(); // fallback to safe pathing if we can't find a safe hole close enough to the target
        }
        if (tStatus == PathStatus.Pathing) targetPathing.run(); // tick target pathing
    }

    private void safePathSeq() {
        state = State.SafePath;
        PathStatus sStatus = safePathing.getStatus(); // check current status
        if (sStatus == PathStatus.Init_NoGoal || sStatus == PathStatus.Invalidated || sStatus == PathStatus.Complete) { // reset goal
            BlockPos bestHole = HoleUtils.getHoleBySelf();
            if (bestHole != null) safePathing.setGoal(bestHole);
            else { // fallback if we can't find any safe holes
                if (surroundFallback.get()) Helper.quickSurround();
                //todo make it able to use pearls and chorus?
            }
        }
        if (sStatus == PathStatus.Pathing) safePathing.run(); // tick safe pathing
    }


    private void combatSeq() {
        targetPathing.reset();
        autoGap.disableModule();
        if (mc.player.distanceTo(target) > distRatio.get()) { // if we aren't close enough to the target anymore
            ca.disableModule();
            targetPathSeq();
        }
        state = State.Combat;
        ca.run();
    }

    private void safeSeq() {
        safePathing.reset();
        state = State.Idle;
        autoGap.run();
        if (autoGap.check()) ext = " (AUTOGAP)";
    }


    private boolean isSafe() {
        return HoleUtils.isPlayerSafe();
    }

    private boolean hasTarget() {
        if (TargetUtils.isBadTarget(target, targetRange.get())) {
            target = TargetUtils.getPlayerTarget(targetRange.get(), priority.get());
            return false;
        }
        return true;
    }



    @Override
    public String getInfoString() {
        String rev = "cope";
        switch (state) {
            case Idle -> rev = "Idle." + ext;
            case TargetPath -> {
                rev = "PathToTarget";
                switch (targetPathing.getStatus()) {
                    case Init -> rev += " (TINIT)";
                    case Pathing -> rev += "(TPATH)";
                    case Init_NoGoal -> rev += " (TINIT_NOGOAL)";
                    case Invalidated -> rev += " (TINVALID)";
                    case Complete -> rev += " (TCOMPLETE)";
                }
            }
            case SafePath -> {
                rev = "PathToSafeHole";
                switch (safePathing.getStatus()) {
                    case Init -> rev += " (SINIT)";
                    case Pathing -> rev += "(SPATH)";
                    case Init_NoGoal -> rev += " (SINIT_NOGOAL)";
                    case Invalidated -> rev += " (SINVALID)";
                    case Complete -> rev += " (SCOMPLETE)";
                }
            }
            case Combat -> rev = "InCombat";
        }
        return rev;
    }

}
