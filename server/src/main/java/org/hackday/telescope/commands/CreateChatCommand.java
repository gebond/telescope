package org.hackday.telescope.commands;

import org.eclipse.jetty.websocket.api.Session;
import org.hackday.telescope.UberSocketHandler;
import org.hackday.telescope.db.dao.UberDao;
import org.hackday.telescope.models.Chat;
import org.hackday.telescope.models.User;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CreateChatCommand extends Command {

    private final UberDao dao = UberDao.getInstance();

    private Long creatorId;
    private List<Long> inviteeIds;
    private String chatName;
    private Boolean scope;

    public CreateChatCommand(Session session, String input) {
        super(session);

        JSONObject json = new JSONObject(input);

        creatorId = json.getLong("creator_id");
        inviteeIds = json.getJSONArray("invitee_ids").toList().stream()
                .map(Integer.class::cast)
                .map(Long::new)
                .collect(Collectors.toList());
        chatName = json.getString("chat_name");
        scope = json.getBoolean("is_scope");
    }

    @Override
    public void run() {
        Chat chat = new Chat(chatName, scope);
        dao.addChat(chat);
        List<User> users = new ArrayList<>();
        users.add(dao.getUserById(creatorId));
        users.addAll(inviteeIds.stream().map(dao::getUserById).collect(Collectors.toList()));
        users.forEach(user -> {
            dao.join2Chat(user, chat);
            Session sessionForUser = UberSocketHandler.getSessionForUser(user.getId());
            new GetChatsForUserCommand(Collections.singletonList(sessionForUser), user.getId()).run();
        });

        sessions.forEach(session -> {
            try {
                session.getRemote().sendString(new JSONObject() {{
                    put("chat_id", chat.getId());
                }}.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
