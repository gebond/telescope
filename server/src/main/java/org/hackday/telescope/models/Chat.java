package org.hackday.telescope.models;

import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by iistomin on 28/10/17.
 */
public class Chat {
    private static final AtomicLong ID_COUNTER = new AtomicLong(100001);

    private final Long id;
    private String name;
    private Boolean scope;

    private Message lastMessage;

    public Chat(String name, Boolean scope) {
        id = ID_COUNTER.incrementAndGet();

        this.name = name;
        this.scope = scope;
    }

    public Chat(Long id, String name, Boolean scope) {
        this.id = id;
        this.name = name;
        this.scope = scope;
    }

    public synchronized static void updateCounter(Long value) {
        if (ID_COUNTER.get() <= value) {
            ID_COUNTER.set(value + 1);
        }
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

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
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

    public enum COMPARATORS {

        BY_LAST_MESSAGE_DATE() {
            @Override
            public Comparator<Chat> getComparator() {
                // TODO fix NPEs when lastMessage is null
                return Comparator.comparing(chat -> chat.getLastMessage().getTime());
            }
        };

        public abstract Comparator<Chat> getComparator();
    }
}
