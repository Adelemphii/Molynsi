package me.adelemphii.molynsi.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * Utility class for Chat
 */
public class ChatUtility {

    /**
     * Make a formatted debug message component.
     * @param message String to format
     * @return A formatted component
     */
    public static Component formatDebugMessage(String message) {
        return Component.text("[Molynsi] Debug: ").color(NamedTextColor.AQUA)
                .append(Component.text(message).color(NamedTextColor.LIGHT_PURPLE));
    }
}
