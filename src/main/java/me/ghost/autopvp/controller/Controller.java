package me.ghost.autopvp.controller;

public class Controller {

    private String name;
    public boolean active;

    public Controller(String name) {
        this.name = name;
    }

    public void tick() {}


    public boolean isActive() { return this.active; }
    public void activate() { this.active = true; }
    public void deactivate() { this.active = false; }


}
