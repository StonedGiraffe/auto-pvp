package me.ghost.autopvp.task;

public class Task {

    private final String name;

    public Task(String name) {
        this.name = name;
    }

    public void tick() {}
    public void reset() {}
    public String getName() { return this.name; }

}
