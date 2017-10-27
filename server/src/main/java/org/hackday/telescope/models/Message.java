package org.hackday.telescope.models;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

public class Message implements Comparable<Message> {
    private static final AtomicLong ID_COUNTER = new AtomicLong(0);

    private Long id;
    private String text;
    private LocalDateTime time;
    private User sender;
    private Chat chat;
    private Chat scope;

    public Message() {
        id = ID_COUNTER.incrementAndGet();
        time = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public User getSender() {
        return sender;
    }

    public Chat getChat() {
        return chat;
    }

    public Chat getScope() {
        return scope;
    }

    @Override
    public int compareTo(Message o) {
        return time.compareTo(o.time);
    }
}
