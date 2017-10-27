package org.hackday.telescope.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by iistomin on 28/10/17.
 */
public class Chat {
    private static final AtomicLong ID_COUNTER = new AtomicLong(0);

    private final Long id;
    private String name;
    private Boolean isScope;

    private List<User> users;
    private Set<Message> messages; //TreeSet

    public Chat(String name, Boolean isScope) {
        id = ID_COUNTER.incrementAndGet();
        users = new ArrayList<>();
        messages = new TreeSet<>();

        this.name = name;
        this.isScope = isScope;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean getScope() {
        return isScope;
    }

    public List<User> getUsers() {
        return users;
    }

    public Set<Message> getMessages() {
        return messages;
    }
}
