package me.ghost.autopvp.task.combat;

import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;

public class CombatTask {

    private final String moduleName;

    public CombatTask(Module module) {
        this.moduleName = module.name;
    }

    public Module getModule() {
        return Modules.get().get(this.moduleName);
    }

    public void disableModule() {
        Module m = getModule();
        if (m.isActive()) m.toggle();
    }

    public void enableModule() {
        Module m = getModule();
        if (!m.isActive()) m.toggle();
    }

    public boolean selfCheck() {
        return false;
    }

    public boolean targetCheck() {
        return false;
    }

    public void run() {
        if (!this.selfCheck() || !this.targetCheck()) this.disableModule();
        else this.enableModule();
    }
}
