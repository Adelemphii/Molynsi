package me.adelemphii.molynsi.utils;

import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

public class WorldBorderUtils {

    public static void setBorder(Player player, int size) {
        WorldBorder worldBorder = player.getWorld().getWorldBorder();
        worldBorder.setCenter(player.getLocation());
        worldBorder.setSize(size);
    }
}
