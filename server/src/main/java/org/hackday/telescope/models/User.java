package org.hackday.telescope.models;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class User {
    private static final AtomicLong ID_COUNTER = new AtomicLong(0);

    private final Long id;
    private String name;
    private List<Chat> chats;

    public User(String name) {
        id = ID_COUNTER.incrementAndGet();
        chats = new ArrayList<>();

        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
