package org.hackday.telescope.commands;

import org.eclipse.jetty.websocket.api.Session;
import org.hackday.telescope.db.dao.UberDao;
import org.hackday.telescope.models.User;
import org.json.JSONObject;

import java.io.IOException;

public class LoginCommand extends Command {

    private UberDao dao = UberDao.getInstance();

    private String username;

    public LoginCommand(Session session, String input) {
        super(session);

        username = new JSONObject(input).getString("username");
    }

    @Override
    public void run() {
        User user = dao.getOrCreateUserByName(username);

        sessions.forEach(session -> {
            try {
                session.getRemote().sendString(
                        new JSONObject() {{
                            put("method", "login");
                            put("payload", new JSONObject() {{
                                put("user_id", user.getId());
                            }});
                        }}.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
