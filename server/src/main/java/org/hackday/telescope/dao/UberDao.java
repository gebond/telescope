package org.hackday.telescope.dao;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.hackday.telescope.commands.GetChatsForUserCommand;
import org.hackday.telescope.models.Chat;
import org.hackday.telescope.models.Message;
import org.hackday.telescope.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        mock(); // TODO :-)
    }

    private void mock() {
        User gleb = getOrCreateUserByName("gleb");
        User vlad = getOrCreateUserByName("vlad");
        User guseyn = getOrCreateUserByName("guseyn");
        User ilya = getOrCreateUserByName("ilya");

        Chat chat1 = new Chat("Chat my #1", false);
        Chat chat2 = new Chat("Chat my #2", false);
        Chat chat3 = new Chat("Chat scope", true);

        addChat(chat1);
        addChat(chat2);
        addChat(chat3);

        join2Chat(gleb, chat1);
        join2Chat(vlad, chat1);
        join2Chat(guseyn, chat1);
        join2Chat(ilya, chat1);

        join2Chat(gleb, chat2);
        join2Chat(vlad, chat2);
        join2Chat(guseyn, chat2);

        join2Chat(gleb, chat3);
        join2Chat(vlad, chat3);

        Message msg1 = new Message(vlad, "vlad msg");
        Message msg2 = new Message(gleb, "gleb msg");
        Message msg3 = new Message(guseyn, "guseyn msg");
        Message msg4 = new Message(ilya, "ilya msg");
        Message msg5 = new Message(vlad, "vlad msg 2");

        sendMessage(msg1, chat1, null);
        sendMessage(msg2, chat1, null);
        sendMessage(msg3, chat1, null);
        sendMessage(msg4, chat2, null);
        sendMessage(msg5, chat2, chat3);

        chat1.setLastMessage(msg3);
        chat2.setLastMessage(msg5);
        chat3.setLastMessage(msg5);

    }

    public static synchronized UberDao getInstance() {
        if (instance == null) {
            instance = new UberDao();
        }
        return instance;
    }

    // Add methods

    public User getOrCreateUserByName(String username) {
        User user = getUserByName(username);
        if (user == null) {
            user = new User(username);
            users.put(user.getId(), user);
        }

        return user;
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

    public User getUserByName(String username) {
        return users.values().stream()
                .filter(user -> user.getName().equals(username))
                .findFirst().orElse(null);
    }

    public List<User> getUsersByChat(Chat chat) {
        return chatId2UserIdMap.get(chat.getId()).stream()
                .map(users::get)
                .collect(Collectors.toList());
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
