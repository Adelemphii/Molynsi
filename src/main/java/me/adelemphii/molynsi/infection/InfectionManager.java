package me.adelemphii.molynsi.infection;

import me.adelemphii.molynsi.Molynsi;
import me.adelemphii.molynsi.utils.player.User;

import java.util.Map;

public class InfectionManager {

    Molynsi plugin;

    Map<Integer, User> userMap;

    // TODO: For managing and registering stuff related to player infection
    public InfectionManager(Molynsi plugin) {
        this.plugin = plugin;
        this.userMap = plugin.getUsers();
    }

}
