package org.hackday.telescope.db.queries;

public class QueryProvider {

    public static final String INSERT_INTO_USERS = "INSERT INTO users (id, name) VALUES (?, ?)";
    public static final String INSERT_INTO_CHATS = "INSERT INTO chats (id, name, is_scope) VALUES (?, ?, ?)";
    public static final String INSERT_INTO_MESSAGES = "INSERT INTO messages (id, text, time, sender_id, chat_id, scope_id) VALUES (?, ?, ?, ?, ?, ?)";
    public static final String INSERT_INTO_USERS_TO_CHATS = "INSERT INTO users_to_chats (user_id, chat_id) VALUES (?, ?)";

    public static final String SELECT_ALL_FROM_USERS = "SELECT * FROM users";
    public static final String SELECT_ALL_FROM_CHATS = "SELECT * FROM chats";
    public static final String SELECT_ALL_FROM_MESSAGES = "SELECT * FROM messages";
    public static final String SELECT_ALL_FROM_USERS_TO_CHATS = "SELECT * FROM users_to_chats";

    public static final String DELETE_FROM_USERS_TO_CHATS = "DELETE FROM users_to_chats WHERE user_id = ? AND chat_id = ?";

    private QueryProvider() {
        throw new AssertionError("Utility class!");
    }
}
