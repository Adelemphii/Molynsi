package me.adelemphii.molynsi.utils;

import me.adelemphii.molynsi.Molynsi;

import java.util.Arrays;
import java.util.List;

/**
 * Manager Class for containing the Config values.
 */
public class ConfigManager {

    private final Molynsi plugin;
    private int countdownTimer;
    private int peaceTimeSeconds;
    private int infectedTurnTimeSeconds;
    private int infectChanceDuringPeace;
    private int infectChance;

    private boolean debug;

    private List<String> turnMessages;

    public ConfigManager(Molynsi plugin) {
        this.plugin = plugin;
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

    /**
     * Get the countdown timer length.
     * @return countdown timer length in seconds
     */
    public int getCountdownTimer() {
        return countdownTimer;
    }

    /**
     * Set the countdown timer length.
     * @param countdownTimer Countdown timer length in seconds
     */
    public void setCountdownTimer(int countdownTimer) {
        this.countdownTimer = countdownTimer;
    }

    /**
     * Get how long Peace Time lasts in seconds.
     * @return Peace Time length in seconds.
     */
    public int getPeaceTimeSeconds() {
        return peaceTimeSeconds;
    }

    /**
     * Set how long Peace Time lasts in seconds.
     * @param peaceTimeSeconds Peace time length in seconds.
     */
    public void setPeaceTimeSeconds(int peaceTimeSeconds) {
        this.peaceTimeSeconds = peaceTimeSeconds;
    }

    /**
     * Get the time it takes for an infected to turn in seconds.
     * @return The time in seconds
     */
    public int getInfectedTurnTimeSeconds() {
        return infectedTurnTimeSeconds;
    }

    /**
     * Set the time it takes for an infected to turn in seconds.
     * @param infectedTurnTimeSeconds The time in seconds
     */
    public void setInfectedTurnTimeSeconds(int infectedTurnTimeSeconds) {
        this.infectedTurnTimeSeconds = infectedTurnTimeSeconds;
    }

    /**
     * Get the list of messages that play when a player turns into an undead
     * @return List of Strings
     */
    public List<String> getTurnMessages() {
        return turnMessages;
    }

    /**
     * Set the list of messages that play when a player turns into an undead.
     * @param turnMessages List of Strings to play
     */
    public void setTurnMessages(List<String> turnMessages) {
        this.turnMessages = turnMessages;
    }

    /**
     * Get the default chance needed for a player to get infected during Peace Time.
     * @return Default chance
     */
    public int getInfectChanceDuringPeace() {
        return infectChanceDuringPeace;
    }

    /**
     * Set the default chance needed for a player to get infected during Peace Time.
     * @param infectChanceDuringPeace Default chance
     */
    public void setInfectChanceDuringPeace(int infectChanceDuringPeace) {
        this.infectChanceDuringPeace = infectChanceDuringPeace;
    }

    /**
     * Get the default chance needed for a player to get infected outside of Peace Time.
     * @return Default chance
     */
    public int getInfectChance() {
        return infectChance;
    }

    /**
     * Set the default chance needed for a player to get infected outside of Peace Time.
     * @param infectChance Default chance
     */
    public void setInfectChance(int infectChance) {
        this.infectChance = infectChance;
    }

    /**
     * Get if the plugin is in debug mode.
     * @return if debug
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * Set if the plugin is in debug mode.
     * @param debug if debug
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
