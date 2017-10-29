package org.hackday.telescope.commands;

import org.eclipse.jetty.websocket.api.Session;
import org.hackday.telescope.db.dao.UberDao;
import org.hackday.telescope.models.Chat;
import org.hackday.telescope.models.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class GetScopesCommand extends Command {

    private final UberDao dao = UberDao.getInstance();

    private Long userId;

    public GetScopesCommand(Session session, String input) {
        super(session);

        userId = new JSONObject(input).getLong("user_id");
    }

    public GetScopesCommand(List<Session> sessions, Long userId) {
        super(sessions);

        this.userId = userId;
    }

    @Override
    public void run() {
        User user = dao.getUserById(userId);

        sessions.forEach(session -> {
            try {
                session.getRemote().sendString(
                        new JSONObject() {{
                            put("method", "get_scopes");
                            put("payload", new JSONObject() {{
                                put("scopes", new JSONArray(dao.getChatsByUser(user).stream()
                                        .filter(Chat::isScope)
                                        .map(chat -> new JSONObject() {{
                                            put("id", chat.getId());
                                            put("name", chat.getName());
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
