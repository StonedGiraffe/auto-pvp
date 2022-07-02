package me.ghost.autopvp.task.safety;

import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;

public class SafetyTask {

    private final String moduleName;

    public SafetyTask(Module module) {
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

    public boolean check() {
        return false;
    }

    public void run() {
        if (!this.check()) this.disableModule();
        else this.enableModule();
    }
}
