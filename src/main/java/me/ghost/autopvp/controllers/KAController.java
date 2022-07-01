package me.ghost.autopvp.controllers;

import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.combat.KillAura;

public class KAController extends Controller {

    public double minHealth = 0.0D;

    public KAController() {
        super("KillAura", Modules.get().get(KillAura.class));
    }

    @Override
    public void onTick() {
        //code here
    }
}
