package me.adelemphii.molynsi.infection.events;

import me.adelemphii.molynsi.utils.player.User;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Called before the infection pulse
 */
public class InfectionPrePulseEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean isCancelled;

    private final User whoUser;
    private final Player cause;
    private int chance;
    private int needed;

    public InfectionPrePulseEvent(@NotNull Player who, Player cause, User whoUser, int chance, int needed) {
        super(who);
        this.whoUser = whoUser;
        this.cause = cause;
        this.isCancelled = false;
        this.chance = chance;
        this.needed = needed;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public User getWhoUser() {
        return whoUser;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    /**
     * Get the infected player who is spreading the infection.
     * @return The Player that is spreading the infection
     */
    public Player getCause() {
        return cause;
    }

    /**
     * Get the chance of a player being infected.
     * Higher is a less chance of being infected.
     * @return The chance from 1-100
     */
    public int getChance() {
        return chance;
    }

    /**
     * Set the chance of a player being infected.
     * Higher is a less chance of being infected.
     * @param chance The chance from 1-100
     */
    public void setChance(int chance) {
        this.chance = chance;
    }

    /**
     * Get the needed number for a player to be infected.
     * Higher values mean a higher chance of being infected.
     * @return The needed number from 1-100
     */
    public int getNeeded() {
        return needed;
    }

    /**
     * Set the needed number for a player to be infected.
     * Higher values mean a higher chance of being infected.
     * @param needed The needed number from 1-100
     */
    public void setNeeded(int needed) {
        this.needed = needed;
    }
}
