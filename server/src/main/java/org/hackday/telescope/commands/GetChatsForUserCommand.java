package org.hackday.telescope.commands;

import org.eclipse.jetty.websocket.api.Session;
import org.hackday.telescope.UberSocketHandler;
import org.hackday.telescope.db.dao.UberDao;
import org.hackday.telescope.models.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GetChatsForUserCommand extends Command {

    private final UberDao dao = UberDao.getInstance();

    private Long userId;

    public GetChatsForUserCommand(Session session, String input) {
        super(Collections.singletonList(session));

        userId = new JSONObject(input).getLong("user_id");

        UberSocketHandler.SESSIONS_MAP.putIfAbsent(session, userId);
    }

    public GetChatsForUserCommand(List<Session> sessions, Long userId) {
        super(sessions);

        this.userId = userId;
    }

    @Override
    public void run() {
        User user = dao.getUserById(userId);
        if (user == null) {
            return; // todo may be exception
        }

        sessions.forEach(session -> {
            try {
                session.getRemote().sendString(
                        new JSONObject() {{
                            put("method", "get_chats");
                            put("payload", new JSONObject() {{
                                put("chats", new JSONArray(dao.getChatsByUser(user).stream()
                                        .map(chat -> new JSONObject() {{
                                            put("id", chat.getId());
                                            put("name", chat.getName());
                                            put("is_scope", chat.isScope());
                                            put("lastMessageText", chat.getLastMessage() != null
                                                    ? chat.getLastMessage().getText()
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
