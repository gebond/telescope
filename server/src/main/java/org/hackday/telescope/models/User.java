package org.hackday.telescope.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {

    private final Long id;
    private String name;
    private List<Chat> chats;

    public User(String name) {
        id = Chat.ID_COUNTER.incrementAndGet();
        chats = new ArrayList<>();

        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}