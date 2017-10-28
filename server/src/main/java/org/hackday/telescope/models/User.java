package org.hackday.telescope.models;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class User {

    private static final AtomicLong ID_COUNTER = new AtomicLong(0);

    private final Long id;
    private String name;

    public User(String name) {
        id = ID_COUNTER.incrementAndGet();

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
