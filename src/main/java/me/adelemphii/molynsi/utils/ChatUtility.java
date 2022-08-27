package me.adelemphii.molynsi.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ChatUtility {

    public static Component formatDebugMessage(String message) {
        return Component.text("[Molynsi] Debug: ").color(NamedTextColor.AQUA)
                .append(Component.text(message).color(NamedTextColor.LIGHT_PURPLE));
    }
}
