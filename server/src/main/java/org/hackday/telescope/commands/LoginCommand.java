package org.hackday.telescope.commands;

import org.hackday.telescope.dao.UberDao;
import org.hackday.telescope.models.User;
import org.json.JSONObject;

public class LoginCommand implements Command {

    private UberDao dao = UberDao.getInstance();

    private String username;

    public LoginCommand(String input) {
        username = new JSONObject(input).getString("username");
    }

    @Override
    public String call() throws Exception {
        User user = dao.getOrCreateUserByName(username);
        return new JSONObject() {{
            put("user_id", user.getId());
        }}.toString();
    }
}
