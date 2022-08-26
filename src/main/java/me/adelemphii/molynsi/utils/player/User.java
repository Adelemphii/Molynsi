package me.adelemphii.molynsi.utils.player;

import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable {

    // Generic player info
    private UUID uuid;

    // Custom info for infection/zombified
    private boolean alive;
    private boolean infected;
    private boolean turned;
    private Long timeInfected;

    // Custom stats for the player when infected, generated upon turning.
    private boolean statsApplied;
    private double maxHealth;
    private float speed;

    public User(UUID uuid, boolean alive, boolean infected, boolean turned,
                Long timeInfected, boolean statsApplied, double maxHealth, float speed) {
        this.uuid = uuid;

        this.alive = alive;
        this.infected = infected;
        this.turned = turned;
        this.timeInfected = timeInfected;

        this.statsApplied = statsApplied;
        this.maxHealth = maxHealth;
        this.speed = speed;
    }

    // TODO: add documentation for these methods
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isInfected() {
        return infected;
    }

    public void setInfected(boolean infected) {
        this.infected = infected;
    }

    public boolean isTurned() {
        return turned;
    }

    public void setTurned(boolean turned) {
        this.turned = turned;
    }

    public Long getTimeInfected() {
        return timeInfected;
    }

    public void setTimeInfected(Long timeInfected) {
        this.timeInfected = timeInfected;
    }

    public boolean isStatsApplied() {
        return statsApplied;
    }

    public void setStatsApplied(boolean statsApplied) {
        this.statsApplied = statsApplied;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "User{" +
                "uuid='" + uuid + '\'' +
                ", alive=" + alive +
                ", infected=" + infected +
                ", turned=" + turned +
                ", timeInfected=" + timeInfected +
                ", statsApplied=" + statsApplied +
                ", maxHealth=" + maxHealth +
                ", speed=" + speed +
                '}';
    }
}
