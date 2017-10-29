package org.hackday.telescope.commands;

import org.eclipse.jetty.websocket.api.Session;
import org.hackday.telescope.UberSocketHandler;
import org.hackday.telescope.db.dao.UberDao;
import org.hackday.telescope.models.Chat;
import org.hackday.telescope.models.User;
import org.json.JSONObject;

import java.util.Collections;

public class InviteToChatCommand extends Command {

    private final UberDao dao = UberDao.getInstance();

    private Long targetUserId;
    private Long targetChatId;

    public InviteToChatCommand(Session session, String input) {
        super(session);

        JSONObject json = new JSONObject(input);

        targetUserId = json.getLong("target_user_id");
        targetChatId = json.getLong("target_chat_id");
    }

    @Override
    public void run() {
        Chat chat = dao.getChatById(targetChatId);
        User user = dao.getUserById(targetUserId);

        dao.join2Chat(user, chat);
        Session sessionForUser = UberSocketHandler.getSessionForUser(user.getId());
        new GetChatsForUserCommand(Collections.singletonList(sessionForUser), user.getId()).run();
        new GetScopesCommand(Collections.singletonList(sessionForUser), user.getId()).run();
    }
}
