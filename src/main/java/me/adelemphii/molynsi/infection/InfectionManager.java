package me.adelemphii.molynsi.infection;

import me.adelemphii.molynsi.Molynsi;
import me.adelemphii.molynsi.utils.player.User;

import java.util.Map;
import java.util.UUID;

public class InfectionManager {

    private final Molynsi plugin;

    private Map<UUID, User> userMap;

    // TODO: For managing and registering stuff related to player infection
    public InfectionManager(Molynsi plugin) {
        this.plugin = plugin;
        this.userMap = plugin.getUserManager().getUsers();
    }

    public boolean start() {
        return false;
    }

}
