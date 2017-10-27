package org.hackday.telescope.dao;

import org.hackday.telescope.models.User;

import java.util.HashMap;
import java.util.Map;

public class UberDao {
    private static UberDao instance = null;

    private Map<Long, User> users;

    public static synchronized UberDao getInstance() {
        if (instance == null) {
            instance = new UberDao();
        }
        return instance;
    }

    public Map<Long, User> getAllUsers() {
        return users;
    }

    public User getUserById(Long id) {
        return users.get(id);
    }

    private UberDao() {
        this.users = new HashMap<>();
    }
}
