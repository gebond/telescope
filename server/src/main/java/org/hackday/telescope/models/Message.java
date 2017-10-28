package org.hackday.telescope.models;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class Message implements Comparable<Message> {
    private static final AtomicLong ID_COUNTER = new AtomicLong(0);

    private Long id;
    private String text;
    private LocalDateTime time;
    private User sender;

    public Message(User sender, String text) {
        id = ID_COUNTER.incrementAndGet();
        time = LocalDateTime.now();

        this.sender = sender;
        this.text = text;
    }

    public Message(Long id, String text, LocalDateTime time, User sender) {
        this.id = id;
        this.text = text;
        this.time = time;
        this.sender = sender;
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

    @Override
    public int compareTo(Message o) {
        return time.compareTo(o.time);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(id, message.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
