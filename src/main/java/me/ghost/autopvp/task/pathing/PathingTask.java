package me.ghost.autopvp.task.pathing;

import me.ghost.autopvp.utils.BaritoneUtils;
import me.ghost.autopvp.utils.BlockUtils;
import net.minecraft.util.math.BlockPos;

public class PathingTask {

    private BlockPos goalPos;
    private PathStatus status;

    public PathingTask() {
        this.goalPos = null;
        this.status = PathStatus.Init_NoGoal;
    }

    public PathingTask(BlockPos goal) {
        this.goalPos = goal;
        this.status = PathStatus.Init;
    }

    public void setGoal(BlockPos goal) {
        this.goalPos = goal;
        this.status = PathStatus.Init;
        this.run();
    }

    public void reset() {
        this.goalPos = null;
        this.status = PathStatus.Init_NoGoal;
        BaritoneUtils.clearGoal();
        BaritoneUtils.forceStopPathing();
    }

    public BlockPos getGoal() { return this.goalPos; }
    public PathStatus getStatus() { return this.status; }


    public void run() {
        if (this.goalPos == null) {
            this.status = PathStatus.Invalidated;
            return;
        }
        switch (this.status) {
            case Init -> { // reset and start pathing
                BaritoneUtils.forceStopPathing();
                BaritoneUtils.pathToBlockPos(this.goalPos);
                this.status = PathStatus.Pathing;
            }
            case Pathing -> { // set status if the safe hole expired, or we made it
                if (!BlockUtils.isSafePos(this.goalPos)) this.status = PathStatus.Invalidated;
                if (BaritoneUtils.isAtGoal(this.goalPos)) this.status = PathStatus.Complete;
            }
        }
    }

}
