package me.ghost.autopvp.controllers;

import meteordevelopment.meteorclient.systems.modules.Module;

public class Controller {

    private String name;
    private Module module;
    private boolean active;

    public Controller(String name) {
        this.name = name;
    }

    public Controller(String name, Module module) {
        this.name = name;
        this.module = module;
    }

    //public void init() {}

    public void onTick() {}

    public void startModule() { if (!this.module.isActive()) this.module.toggle(); active = true; }

    public void stopModule () { if (this.module.isActive()) this.module.toggle(); active = false; }

    public void setModule(Module module) { this.module = module; }

    public Module getModule() { return this.module; }

    public void setName(String name) { this.name = name; }

    public String getName() { return this.name; }

    public boolean isActive() { return this.active; }

}
