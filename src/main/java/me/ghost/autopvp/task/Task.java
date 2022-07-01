package me.ghost.autopvp.task;

public class Task {

    private final String name;
    private boolean running, complete;

    public Task(String name) {
        this.name = name;
        this.running = false;
        this.complete = false;
    }

    public void tick() {}
    public void reset() {
        this.running = false;
        this.complete = false;
    }

    public String getName() { return this.name; }

    public boolean shouldRun() {
        return this.running && !this.complete;
    }
    public boolean isRunning() { return this.running; }
    public boolean isComplete() { return this.complete; }

    public void enable() { this.running = true; }
    public void disable() { this.running = false; }
    public void setIncomplete() { this.complete = false; }
    public void setComplete() {
        this.disable();
        this.complete = true;
    }

}
