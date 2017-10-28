package org.hackday.telescope.commands;

import org.eclipse.jetty.websocket.api.Session;
import org.hackday.telescope.db.dao.UberDao;
import org.hackday.telescope.models.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.stream.Collectors;

public class GetChatsForUserCommand extends Command {

    private final UberDao dao = UberDao.getInstance();

    private Long userId;

    public GetChatsForUserCommand(Session session, String input) {
        super(session);

        userId = new JSONObject(input).getLong("user_id");
    }

    public GetChatsForUserCommand(Session session, Long userId) {
        super(session);

        this.userId = userId;
    }

    @Override
    public void run() {
        User user = dao.getUserById(userId);
        if (user == null) {
            return; // todo may be exception
        }
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
            System.err.println("some shit happened during GetChatsForUserCommand execution");
            e.printStackTrace();
        }
    }
}
