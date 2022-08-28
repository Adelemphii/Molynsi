package me.adelemphii.molynsi.utils.player;

import tech.adelemphii.molynsiapi.player.AbstractUser;

import java.io.Serializable;
import java.util.UUID;

public class User extends AbstractUser implements Serializable {

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

    public void setDefaults() {
        alive = true;
        infected = false;
        turned = false;
        timeInfected = null;

        statsApplied = false;
        maxHealth = 20;
        speed = 1.0f;
    }

    protected void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    @Override
    public boolean isInfected() {
        return infected;
    }

    @Override
    public void setInfected(boolean infected) {
        this.infected = infected;
    }

    @Override
    public boolean isTurned() {
        return turned;
    }

    @Override
    public void setTurned(boolean turned) {
        this.turned = turned;
    }

    @Override
    public Long getTimeInfected() {
        return timeInfected;
    }

    @Override
    public void setTimeInfected(Long timeInfected) {
        this.timeInfected = timeInfected;
    }

    @Override
    public boolean areStatsApplied() {
        return statsApplied;
    }

    @Override
    public void setStatsApplied(boolean statsApplied) {
        this.statsApplied = statsApplied;
    }

    @Override
    public double getMaxHealth() {
        return maxHealth;
    }

    @Override
    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    @Override
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
