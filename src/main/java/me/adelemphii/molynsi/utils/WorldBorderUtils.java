package me.adelemphii.molynsi.utils;

import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

/**
 * Utility Class to manage the world border.
 */
public class WorldBorderUtils {

    /**
     * Set the border on the location of a player with the given size.
     * @param player Player to center the border on
     * @param size Size of the border - 1/2 side length
     */
    public static void setBorder(Player player, int size) {
        WorldBorder worldBorder = player.getWorld().getWorldBorder();
        worldBorder.setCenter(player.getLocation());
        worldBorder.setSize(size);
    }
}
