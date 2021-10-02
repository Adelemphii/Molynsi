package me.adelemphii.molynsi.mutations;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PreviousLifeSkill implements MutationBase, Listener {

    @Override
    public void givePlayerDBuffs(Player player) {

    }

    @Override
    public List<ItemStack> getAllowedItems() {
        return null;
    }
}
