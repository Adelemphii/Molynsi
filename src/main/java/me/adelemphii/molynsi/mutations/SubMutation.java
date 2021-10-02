package me.adelemphii.molynsi.mutations;

import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public enum SubMutation {

    SWORD_HANDS(Material.STONE_SWORD, Material.WOODEN_SWORD);

    Material item1;
    Material item2;
    SubMutation(Material item1, Material item2) {
        this.item1 = item1;
        this.item2 = item2;
    }

    public List<Material> getItems() {
        return Arrays.asList(item1, item2);
    }
}
