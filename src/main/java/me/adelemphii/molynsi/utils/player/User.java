package me.adelemphii.molynsi.utils.player;

import tech.adelemphii.molynsiapi.player.AbstractUser;

import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable {

    // Generic player info
    /**
     * UUID of the user.
     */
    private final UUID uuid;

    // Custom info for infection/turned
    /**
     * Alive status of the user.
     */
    private boolean alive;
    /**
     * Infected status of the user.
     */
    private boolean infected;
    /**
     * Turned status of the user.
     */
    private boolean turned;
    /**
     * Infected time of the user in seconds.
     */
    private Long timeInfected;

    // Class/Mutations for the player when infected, assigned upon turning.
    //private boolean statsApplied;

    public User(UUID uuid, boolean alive, boolean infected, boolean turned,
                Long timeInfected) {
        this.uuid = uuid;

        this.alive = alive;
        this.infected = infected;
        this.turned = turned;
        this.timeInfected = timeInfected;
    }

    public void setDefaults() {
        alive = true;
        infected = false;
        turned = false;
        timeInfected = null;
        //statsApplied = false;
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

    /*
    public boolean areStatsApplied() {
        return statsApplied;
    }

    public void setStatsApplied(boolean statsApplied) {
        this.statsApplied = statsApplied;
    }
     */

    @Override
    public String toString() {
        return "User{" +
                "uuid=" + uuid +
                ", alive=" + alive +
                ", infected=" + infected +
                ", turned=" + turned +
                ", timeInfected=" + timeInfected +
                '}';
    }
}
