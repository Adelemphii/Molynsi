package me.adelemphii.molynsi.infection.events;

import me.adelemphii.molynsi.utils.player.User;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player is infected.
 */
public class PlayerInfectEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean isCancelled;

    private final User user;
    private final Player cause;

    public PlayerInfectEvent(@NotNull Player who, Player cause, User user) {
        super(who);
        this.user = user;
        this.cause = cause;
        this.isCancelled = false;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    /**
     * Get the user who is infected.
     * @return The User who is infected
     */
    public User getUser() {
        return user;
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
     * Get the player who caused the infection.
     * @return The Player who caused the infection
     */
    public Player getCause() {
        return cause;
    }
}
