package org.hackday.telescope.dao;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.hackday.telescope.models.Chat;
import org.hackday.telescope.models.Message;
import org.hackday.telescope.models.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class UberDao {
    private static UberDao instance = null;

    private Map<Long, User> users;
    private Map<Long, Chat> chats;
    private Map<Long, Message> messages;

    private Multimap<Long, Long> chatId2UserIdMap;
    private Multimap<Long, Long> userId2ChatIdMap;

    private Multimap<Long, Long> chatId2MessageIdMap;
    private Map<Long, Long> messageId2ScopeId;

    private UberDao() {
        users = new HashMap<>();
        chats = new HashMap<>();
        messages = new HashMap<>();

        chatId2UserIdMap = ArrayListMultimap.create();
        userId2ChatIdMap = ArrayListMultimap.create();
        chatId2MessageIdMap = ArrayListMultimap.create();
        messageId2ScopeId = new HashMap<>();
    }

    public static synchronized UberDao getInstance() {
        if (instance == null) {
            instance = new UberDao();
        }
        return instance;
    }

    // Add methods

    public UberDao addUser(User user) {
        users.put(user.getId(), user);

        return this;
    }

    public UberDao addChat(Chat chat) {
        chats.put(chat.getId(), chat);

        return this;
    }

    // Get methods

    public Map<Long, User> getAllUsers() {
        return users;
    }

    public User getUserById(Long id) {
        return users.get(id);
    }

    public Chat getChatById(Long id) {
        return chats.get(id);
    }

    public List<Chat> getChatsByUser(User user) {
        return userId2ChatIdMap.get(user.getId()).stream()
                .map(chats::get)
                .collect(Collectors.toList());
    }

    public List<Message> getMessagesByChat(Chat chat) {
        return chatId2MessageIdMap.get(chat.getId()).stream()
                .map(messages::get)
                .sorted()
                .collect(Collectors.toList());
    }

    public Chat getScopeByMessage(Message message) {
        Long scopeId = messageId2ScopeId.get(message.getId());
        return scopeId == null ? null : getChatById(scopeId);
    }

    // Join methods

    public UberDao join2Chat(User user, Chat chat) {
        chatId2UserIdMap.put(chat.getId(), user.getId());
        userId2ChatIdMap.put(user.getId(), chat.getId());

        return this;
    }

    // Sending methods

    public UberDao sendMessage(Message message, Chat chat, Chat scope) {
        messages.put(message.getId(), message);

        chatId2MessageIdMap.put(chat.getId(), message.getId());

        if (scope != null) {
            chatId2MessageIdMap.put(scope.getId(), message.getId());
            messageId2ScopeId.put(message.getId(), scope.getId());
        }

        return this;
    }

    // Remove methods

    public UberDao removeFromChat(User user, Chat chat) {
        chatId2UserIdMap.remove(chat.getId(), user.getId());
        userId2ChatIdMap.remove(user.getId(), chat.getId());

        return this;
    }

    // Clear methods

    public UberDao clearUsers() {
        users.clear();

        return this;
    }
}
