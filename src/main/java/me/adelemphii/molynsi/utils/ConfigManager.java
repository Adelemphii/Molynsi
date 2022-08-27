package me.adelemphii.molynsi.utils;

import me.adelemphii.molynsi.Molynsi;

import java.util.Arrays;
import java.util.List;

public class ConfigManager {

    private Molynsi plugin;
    private int countdownTimer;
    private int peaceTimeSeconds;
    private int infectedTurnTimeSeconds;
    private int infectChanceDuringPeace;
    private int infectChance;

    private boolean debug;

    private List<String> turnMessages;

    public ConfigManager(Molynsi plugin) {
        this.countdownTimer = plugin.getConfig().getInt("countdown-timer", 10);
        this.peaceTimeSeconds = plugin.getConfig().getInt("peace-time-seconds", 300);
        this.infectedTurnTimeSeconds = plugin.getConfig().getInt("infected-turn-time-seconds", 300);
        this.turnMessages = plugin.getConfig().getStringList("turn-messages");
        this.infectChance = plugin.getConfig().getInt("infect-chance", 10);
        this.infectChanceDuringPeace = plugin.getConfig().getInt("infect-chance-during-peace", 5);
        this.debug = plugin.getConfig().getBoolean("debug", false);

        if(turnMessages.isEmpty()) {
            turnMessages = Arrays.asList(
                    "Urgh... I don't feel so well.",
                    "I crave... Flesh.",
                    "Bluurghhh...."
            );
        }
    }

    public int getCountdownTimer() {
        return countdownTimer;
    }

    public void setCountdownTimer(int countdownTimer) {
        this.countdownTimer = countdownTimer;
    }

    public int getPeaceTimeSeconds() {
        return peaceTimeSeconds;
    }

    public void setPeaceTimeSeconds(int peaceTimeSeconds) {
        this.peaceTimeSeconds = peaceTimeSeconds;
    }

    public Molynsi getPlugin() {
        return plugin;
    }

    public void setPlugin(Molynsi plugin) {
        this.plugin = plugin;
    }

    public int getInfectedTurnTimeSeconds() {
        return infectedTurnTimeSeconds;
    }

    public void setInfectedTurnTimeSeconds(int infectedTurnTimeSeconds) {
        this.infectedTurnTimeSeconds = infectedTurnTimeSeconds;
    }

    public List<String> getTurnMessages() {
        return turnMessages;
    }

    public void setTurnMessages(List<String> turnMessages) {
        this.turnMessages = turnMessages;
    }

    public int getInfectChanceDuringPeace() {
        return infectChanceDuringPeace;
    }

    public void setInfectChanceDuringPeace(int infectChanceDuringPeace) {
        this.infectChanceDuringPeace = infectChanceDuringPeace;
    }

    public int getInfectChance() {
        return infectChance;
    }

    public void setInfectChance(int infectChance) {
        this.infectChance = infectChance;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
