package me.adelemphii.molynsi.mutations;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface MutationBase {

    void givePlayerDBuffs(Player player);
    List<ItemStack> getAllowedItems();
}
