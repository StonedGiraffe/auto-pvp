package me.ghost.autopvp.task.tasks;

import me.ghost.autopvp.task.Task;
import net.minecraft.entity.player.PlayerEntity;

public class PathToTargetTask extends Task {

    private PlayerEntity target;

    public PathToTargetTask(PlayerEntity target) {
        super("PathToTarget");
        this.target = target;
    }


}
