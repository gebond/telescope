package org.hackday.telescope.commands;

import jdk.nashorn.internal.parser.JSONParser;
import org.hackday.telescope.dao.UberDao;
import org.hackday.telescope.models.Chat;
import org.hackday.telescope.models.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.stream.Collectors;

public class GetChatsForUserCommand implements Command {

    private UberDao dao = UberDao.getInstance();

    private Long userId;

    public GetChatsForUserCommand(String input) {
        userId = new JSONObject(input).getLong("user_id");
    }

    @Override
    public String call() {
        User user = dao.getUserById(userId);

        return new JSONObject() {{
            put("chats", new JSONArray(dao.getChatsByUser(user).stream()
                    .map(chat -> new JSONObject() {{
                        put("id", chat.getId());
                        put("name", chat.getName());
                        put("lastMessageText", chat.getLastMessage().getText());
                    }})
            .collect(Collectors.toList())));
        }}.toString();
    }
}
