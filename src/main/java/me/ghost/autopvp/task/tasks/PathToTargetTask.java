package me.ghost.autopvp.task.tasks;

import me.ghost.autopvp.controller.ControllerUtils;
import me.ghost.autopvp.task.Task;
import me.ghost.autopvp.utils.BaritoneUtils;
import me.ghost.autopvp.utils.BlockUtils;
import me.ghost.autopvp.utils.HoleUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import static meteordevelopment.meteorclient.MeteorClient.mc;

//Path to the hole nearest the current target
public class PathToTargetTask extends Task {

    private PlayerEntity target;
    private BlockPos goalPos;

    public PathToTargetTask(PlayerEntity target) {
        super("PathToTarget");
        this.reset(); // reset everything & set new target
        this.target = target;
        this.enable(); // enable the task
    }

    public PlayerEntity getTarget() { return this.target; }
    public BlockPos getGoalPos() { return this.goalPos; }

    @Override
    public void reset() {
        this.setIncomplete();
        this.disable();
        if (BaritoneUtils.hasGoal()) BaritoneUtils.forceStopPathing();
        this.target = null;
        this.goalPos = null;
    }

    @Override
    public boolean shouldRun() {
        if (mc.player.distanceTo(target) >= ControllerUtils.getAutoPVP().targetRange.get() || !BlockUtils.isSafePos(mc.player.getBlockPos())) return true;
        return this.isRunning() && !this.isComplete();
    }


    @Override
    public void tick() {
        if (!this.shouldRun() || this.target == null) { // sanity check
            this.reset();
            return;
        }
        if (goalCheck()) { // see if we have a goal yet
            if (!BaritoneUtils.hasGoal()) { // start pathing if we haven't already
                BaritoneUtils.pathToBlockPos(this.goalPos);
            } else {
                if (BaritoneUtils.isAtGoal(this.goalPos)) { // if we reached the goal
                    BaritoneUtils.forceStopPathing();
                    this.setComplete(); // mark the task complete
                }
            }
        } else { // calculate and set the goal if we don't have one
            BlockPos bestHole = HoleUtils.getHoleByTarget(this.target);
            if (bestHole == null) { // reset if no hole could be found
                this.reset();
                return;
            }
            this.goalPos = bestHole; // set the goal (pathing starts next tick)
        }
    }

    public boolean goalCheck() { // see if the goal is set & if it's still safe
        return this.goalPos != null && BlockUtils.isSafePos(this.goalPos);
    }

}
