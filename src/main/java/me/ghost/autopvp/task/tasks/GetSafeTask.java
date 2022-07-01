package me.ghost.autopvp.task.tasks;

import me.ghost.autopvp.task.Task;
import me.ghost.autopvp.utils.BaritoneUtils;
import me.ghost.autopvp.utils.BlockUtils;
import net.minecraft.util.math.BlockPos;

import static meteordevelopment.meteorclient.MeteorClient.mc;

//Path to the nearest (to self) safe hole
public class GetSafeTask extends Task {

    private BlockPos goalPos;



    public GetSafeTask() {
        super("GetSafe");
        this.reset(); // reset and set the goal to the nearest safe hole
        this.goalPos = getSafeHole();
    }

    @Override
    public void tick() {
        if (!this.shouldRun()) return;
        if (goalCheck()) { // see if we have a goal
            if (!BaritoneUtils.hasGoal()) BaritoneUtils.pathToBlockPos(this.goalPos); // start pathing to it if we haven't
            else {
                if (BaritoneUtils.isAtGoal(this.goalPos)) { // see if we reached the goal
                    BaritoneUtils.forceStopPathing();
                    this.setComplete();
                }
            }
        } else { // set the goal if we don't have one
            BlockPos bestHole = getSafeHole();
            if (bestHole == null) this.reset();
            this.goalPos = bestHole;
        }
    }


    public BlockPos getSafeHole() {
        return mc.player.getBlockPos();
        //todo code here
    }

    public boolean goalCheck() {
        return this.goalPos != null && BlockUtils.isSafePos(this.goalPos);
    }
}
