package me.adelemphii.molynsi.utils;

import me.adelemphii.molynsi.utils.player.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserManager {

    private Map<UUID, User> users = new HashMap<>();

    public UserManager() {

    }

    public Map<UUID, User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.put(user.getUuid(), user);
    }

    public void setUsers(Map<UUID, User> users) {
        this.users = users;
    }
}
