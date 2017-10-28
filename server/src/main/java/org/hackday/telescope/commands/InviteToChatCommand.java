package org.hackday.telescope.commands;

import org.eclipse.jetty.websocket.api.Session;
import org.hackday.telescope.dao.UberDao;
import org.hackday.telescope.models.Chat;
import org.hackday.telescope.models.User;
import org.json.JSONObject;

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
        new GetChatsForUserCommand(session, user.getId()).run();

    }
}
