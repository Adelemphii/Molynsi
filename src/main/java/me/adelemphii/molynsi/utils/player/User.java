package me.adelemphii.molynsi.utils.player;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

    // Generic player info
    String uuid;
    int id;

    // Custom info for infection/zombified
    boolean alive;
    boolean infected;
    boolean turned;
    Date timeInfected;

    // Custom stats for the player when infected, generated upon turning.
    boolean statsApplied;
    double maxHealth;
    float speed;

    public User(String uuid, int id, boolean alive, boolean infected, boolean turned,
                Date timeInfected, boolean statsApplied, double maxHealth, float speed) {
        this.uuid = uuid;
        this.id = id;

        this.alive = alive;
        this.infected = infected;
        this.turned = turned;
        this.timeInfected = timeInfected;

        this.statsApplied = statsApplied;
        this.maxHealth = maxHealth;
        this.speed = speed;
    }

    // TODO: add documentation for these methods
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Date getTimeInfected() {
        return timeInfected;
    }

    public void setTimeInfected(Date timeInfected) {
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
                ", id=" + id +
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
