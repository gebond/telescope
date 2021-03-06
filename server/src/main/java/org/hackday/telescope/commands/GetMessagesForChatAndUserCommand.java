package org.hackday.telescope.commands;

import org.eclipse.jetty.websocket.api.Session;
import org.hackday.telescope.UberSocketHandler;
import org.hackday.telescope.db.dao.UberDao;
import org.hackday.telescope.models.Chat;
import org.hackday.telescope.models.Message;
import org.hackday.telescope.models.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GetMessagesForChatAndUserCommand extends Command {

    private UberDao dao = UberDao.getInstance();

    private Long userId;
    private Long chatId;

    public GetMessagesForChatAndUserCommand(Session session, String input) {
        super(session);

        JSONObject json = new JSONObject(input);
        userId = json.getLong("user_id");
        chatId = json.getLong("chat_id");

        UberSocketHandler.SESSIONS_MAP.putIfAbsent(session, userId);
    }

    public GetMessagesForChatAndUserCommand(List<Session> sessions, Long userId, Long chatId) {
        super(sessions);

        this.userId = userId;
        this.chatId = chatId;
    }

    @Override
    public void run() {
        User user = dao.getUserById(userId);
        Chat chat = dao.getChatById(chatId);

        Set<Chat> userScopes = dao.getChatsByUser(user).stream()
                .filter(Chat::isScope)
                .collect(Collectors.toSet());

        List<Message> filteredMessages = dao.getMessagesByChat(chat).stream()
                .filter(message ->
                        dao.getScopeByMessage(message) == null
                                || userScopes.contains(dao.getScopeByMessage(message)))
                .collect(Collectors.toList());

        sessions.forEach(session -> {
            try {
                session.getRemote().sendString(
                        new JSONObject() {{
                            put("method", "get_messages");
                            put("payload",
                                    new JSONObject() {{
                                        put("messages", new JSONArray(filteredMessages.stream()
                                                .map(message -> new JSONObject() {{
                                                    put("body", message.getText());
                                                    put("time", message.getTime());
                                                    put("sender_id", message.getSender().getName());
                                                    put("chat_id", dao.getChatByMessage(message).getId());
                                                    put("chat_name", dao.getChatByMessage(message).getName());
                                                    put("scope_id", dao.getScopeByMessage(message) != null
                                                            ? dao.getScopeByMessage(message).getId()
                                                            : null);
                                                    put("scope_name", dao.getScopeByMessage(message) != null
                                                            ? dao.getScopeByMessage(message).getName()
                                                            : null);
                                                }})
                                                .collect(Collectors.toList())));
                                    }});
                        }}.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
