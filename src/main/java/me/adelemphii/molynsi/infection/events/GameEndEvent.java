package me.adelemphii.molynsi.infection.events;

import me.adelemphii.molynsi.utils.player.User;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

/**
 * Called when the game ends.
 */
public class GameEndEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Map<UUID, User> participants;

    public GameEndEvent(Map<UUID, User> participants) {
        this.participants = participants;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * Get a map of all users who participated in the game.
     * @return map of users.
     */
    public Map<UUID, User> getParticipants() {
        return participants;
    }
}
