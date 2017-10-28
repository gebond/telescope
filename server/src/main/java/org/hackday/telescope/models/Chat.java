package org.hackday.telescope.models;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by iistomin on 28/10/17.
 */
public class Chat {
    public static final AtomicLong ID_COUNTER = new AtomicLong(0);

    private final Long id;
    private String name;
    private Boolean scope;

//    private List<User> users;
//    private Set<Message> messages; //TreeSet

    public Chat(String name, Boolean scope) {
        id = ID_COUNTER.incrementAndGet();
//        users = new ArrayList<>();
//        messages = new TreeSet<>();

        this.name = name;
        this.scope = scope;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean isScope() {
        return scope;
    }

//    public List<User> getUsers() {
//        return users;
//    }
//
//    public Set<Message> getMessages() {
//        return messages;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return Objects.equals(id, chat.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
