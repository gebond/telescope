package org.hackday.telescope.db.dao;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.hackday.telescope.Application;
import org.hackday.telescope.db.parsers.Mapper;
import org.hackday.telescope.db.queries.QueryProvider;
import org.hackday.telescope.models.Chat;
import org.hackday.telescope.models.Message;
import org.hackday.telescope.models.User;

import java.sql.*;
import java.util.*;
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
    private Map<Long, Long> messageId2ChatId;

    private UberDao() {
        users = new HashMap<>();
        chats = new HashMap<>();
        messages = new HashMap<>();

        chatId2UserIdMap = ArrayListMultimap.create();
        userId2ChatIdMap = ArrayListMultimap.create();
        chatId2MessageIdMap = ArrayListMultimap.create();
        messageId2ScopeId = new HashMap<>();
        messageId2ChatId = new HashMap<>();

        mock(); // todo mock cheer

        System.out.println("DAO initialized!");
    }

    public static synchronized UberDao getInstance() {
        if (instance == null) {
            instance = new UberDao();
        }
        return instance;
    }

    public void mock() {
        User gleb = getOrCreateUserByName("gleb");
        User vlad = getOrCreateUserByName("vlad");
        User guseyn = getOrCreateUserByName("guseyn");
        User ilya = getOrCreateUserByName("ilya");
        User admin = getOrCreateUserByName("admin");

        Chat chat1 = new Chat("Project - общий чат", false);
        Chat chat2 = new Chat("Office - общий чат", false);
        Chat chat3 = new Chat("SCOPE - Java Team", true);
        Chat chat4 = new Chat("SCOPE - UI Team", true);

        addChat(chat1);
        addChat(chat2);
        addChat(chat3);
        addChat(chat4);

        join2Chat(gleb, chat1);
        join2Chat(vlad, chat1);
        join2Chat(guseyn, chat1);
        join2Chat(ilya, chat1);

        join2Chat(gleb, chat2);
        join2Chat(vlad, chat2);
        join2Chat(guseyn, chat2);
        join2Chat(admin, chat2);

        join2Chat(vlad, chat3);
        join2Chat(ilya, chat3);

        join2Chat(gleb, chat4);
        join2Chat(guseyn, chat4);

        Message msg0 = new Message(admin, "Кто на хакатон хочет?");

        Message msg1 = new Message(vlad, "UI, Когда вы уже зальете фикс?");

        Message msg2 = new Message(gleb, "мы вообще его делали?");
        Message msg3 = new Message(guseyn, "первый раз вижу");

        Message msg5 = new Message(ilya, "Может это наш баг все-таки?");

        Message msg4 = new Message(gleb, "Скоро будет");

        Message msg6 = new Message(vlad, "Ну пусть сами разбираются)");

        sendMessage(msg1, chat1, null);
        sendMessage(msg2, chat1, chat4);
        sendMessage(msg3, chat1, chat4);

        sendMessage(msg0, chat2, null);

        //sendMessage(msg5, chat1, chat3);

        //sendMessage(msg4, chat1, null);

        //sendMessage(msg6, chat1, chat3);

//        chat1.setLastMessage(msg3);
//        chat2.setLastMessage(msg5);
//        chat3.setLastMessage(msg5);
//        chat4.setLastMessage(msg6);
    }

    // Add methods

    public User getOrCreateUserByName(String username) {
        // TODO: INSERT INTO USERS (DONE)

        User user = getUserByName(username);
        if (user == null) {
            user = new User(username);
            users.put(user.getId(), user);

            try (Connection conn = Application.getDbConnection()) {
                PreparedStatement insertUserPS = conn.prepareStatement(QueryProvider.INSERT_INTO_USERS);
                insertUserPS.setLong(1, user.getId());
                insertUserPS.setString(2, user.getName());
                insertUserPS.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println("getOrCreateUserByName - Only local DB mode?");
            }
        }

        return user;
    }

    public UberDao addChat(Chat chat) {
        // TODO: INSERT INTO CHATS

        chats.put(chat.getId(), chat);

        try (Connection conn = Application.getDbConnection()){
            PreparedStatement insertChatPS = conn.prepareStatement(QueryProvider.INSERT_INTO_CHATS);
            insertChatPS.setLong(1, chat.getId());
            insertChatPS.setString(2, chat.getName());
            insertChatPS.setBoolean(3, chat.isScope());
            insertChatPS.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("addChat - Only local DB mode?");
        }

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
        if (chat == null) return new ArrayList<>();
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

    public Chat getChatByMessage(Message message) {
        Long chatId = messageId2ChatId.get(message.getId());
        return getChatById(chatId);
    }

    // Join methods

    public UberDao join2Chat(User user, Chat chat) {
        // TODO: INSERT INTO USERS_TO_CHAT

        chatId2UserIdMap.put(chat.getId(), user.getId());
        userId2ChatIdMap.put(user.getId(), chat.getId());

        try (Connection conn = Application.getDbConnection()){
            PreparedStatement insertLinkPS = conn.prepareStatement(QueryProvider.INSERT_INTO_USERS_TO_CHATS);
            insertLinkPS.setLong(1, user.getId());
            insertLinkPS.setLong(2, chat.getId());
            insertLinkPS.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("join2Chat - Only local DB mode?");
        }

        return this;
    }

    // Sending methods

    public UberDao sendMessage(Message message, Chat chat, Chat scope) {
        // TODO: INSERT INTO MESSAGES

        messages.put(message.getId(), message);

        chatId2MessageIdMap.put(chat.getId(), message.getId());
        messageId2ChatId.put(message.getId(), chat.getId());

        if (scope != null) {
            chatId2MessageIdMap.put(scope.getId(), message.getId());
            messageId2ScopeId.put(message.getId(), scope.getId());
        }

        try (Connection conn = Application.getDbConnection()){
            PreparedStatement insertMessagePS = conn.prepareStatement(QueryProvider.INSERT_INTO_MESSAGES);
            insertMessagePS.setLong(1, message.getId());
            insertMessagePS.setString(2, message.getText());
            insertMessagePS.setTimestamp(3, Timestamp.valueOf(message.getTime()));
            insertMessagePS.setLong(4, message.getSender().getId());
            insertMessagePS.setLong(5, chat.getId());
            if (scope != null) {
                insertMessagePS.setLong(6, scope.getId());
            } else {
                insertMessagePS.setNull(6, Types.BIGINT);
            }
            insertMessagePS.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("sendMessage - Only local DB mode?");
        }

        return this;
    }

    // Remove methods

    public UberDao removeFromChat(User user, Chat chat) {
        // TODO: DELETE FROM USERS_TO_CHAT

        chatId2UserIdMap.remove(chat.getId(), user.getId());
        userId2ChatIdMap.remove(user.getId(), chat.getId());

        try (Connection conn = Application.getDbConnection()){
            PreparedStatement deleteFromLinksPS = conn.prepareStatement(QueryProvider.DELETE_FROM_USERS_TO_CHATS);
            deleteFromLinksPS.setLong(1, user.getId());
            deleteFromLinksPS.setLong(2, chat.getId());
            deleteFromLinksPS.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("removeFromChat - Only local DB mode?");
        }

        return this;
    }

    // EXTRACTOR

    public void extractAll() {
        System.out.println("Extracting entities from DB");
        try (Connection conn = Application.getDbConnection()){
            PreparedStatement selectAllUsers = conn.prepareStatement(QueryProvider.SELECT_ALL_FROM_USERS);
            PreparedStatement selectAllChats = conn.prepareStatement(QueryProvider.SELECT_ALL_FROM_CHATS);
            PreparedStatement selectAllMessages = conn.prepareStatement(QueryProvider.SELECT_ALL_FROM_MESSAGES);
            PreparedStatement selectAllLinks = conn.prepareStatement(QueryProvider.SELECT_ALL_FROM_USERS_TO_CHATS);

            ResultSet selectAllUsersRs = selectAllUsers.executeQuery();
            ResultSet selectAllChatsRs = selectAllChats.executeQuery();
            ResultSet selectAllMessagesRs = selectAllMessages.executeQuery();
            ResultSet selectAllLinksRs = selectAllLinks.executeQuery();

            while (selectAllUsersRs.next()) {
                User user = Mapper.mapUser(selectAllUsersRs);
                User.updateCounter(user.getId());
                users.put(user.getId(), user);
            }
            while (selectAllChatsRs.next()) {
                Chat chat = Mapper.mapChat(selectAllChatsRs);
                Chat.updateCounter(chat.getId());
                chats.put(chat.getId(), chat);
            }
            while (selectAllMessagesRs.next()) {
                Message message = Mapper.mapMessage(selectAllMessagesRs);
                Message.updateCounter(message.getId());
                messages.put(message.getId(), message);

                Long chatId = selectAllMessagesRs.getLong(5);
                Long scopeId = selectAllMessagesRs.getLong(6);
                scopeId = scopeId == 0 ? null : scopeId;

                chatId2MessageIdMap.put(chatId, message.getId());
                messageId2ChatId.put(message.getId(), chatId);
                messageId2ScopeId.put(message.getId(), scopeId);
            }
            while (selectAllLinksRs.next()) {
                Long userId = selectAllLinksRs.getLong(1);
                Long chatId = selectAllLinksRs.getLong(2);

                chatId2UserIdMap.put(chatId, userId);
                userId2ChatIdMap.put(userId, chatId);
            }

            for (Chat chat : chats.values()) {
                List<Message> collect = chatId2MessageIdMap.get(chat.getId()).stream()
                        .filter(messageId -> !messageId2ScopeId.containsKey(messageId))
                        .map(messages::get)
                        .sorted(Comparator.comparing(Message::getTime))
                        .collect(Collectors.toList());
                Message lastMessage = collect.isEmpty() ? null : collect.get(collect.size() - 1);
                chat.setLastMessage(lastMessage);
            }

            System.out.println("Extracting complete!");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
