package me.ghost.autopvp.controller;

import me.ghost.autopvp.PeeVeePee;
import meteordevelopment.meteorclient.systems.modules.Modules;

public class ControllerUtils {

    public static PeeVeePee getAutoPVP() {
        return Modules.get().get(PeeVeePee.class);
    }

}
