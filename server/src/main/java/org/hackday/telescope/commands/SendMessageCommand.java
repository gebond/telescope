package org.hackday.telescope.commands;

import org.eclipse.jetty.websocket.api.Session;
import org.hackday.telescope.UberSocketHandler;
import org.hackday.telescope.db.dao.UberDao;
import org.hackday.telescope.models.Chat;
import org.hackday.telescope.models.Message;
import org.hackday.telescope.models.User;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

public class SendMessageCommand extends Command {

    private UberDao dao = UberDao.getInstance();

    private Long senderId;
    private Long chatId;
    private Long scopeId;
    private String text;
    private AtomicBoolean executed;

    public SendMessageCommand(Session session, String input) {
        super(session);

        JSONObject json = new JSONObject(input);
        senderId = json.getLong("sender_id");
        chatId = json.getLong("chat_id");
        scopeId = json.keySet().contains("scope_id") ? json.getLong("scope_id") : null;
        text = json.getString("text");

        executed = new AtomicBoolean(false);
    }

    @Override
    public void run() {
        if (!executed.get()) {
            // Create and send new message
            User sender = dao.getUserById(senderId);

            Chat chat = dao.getChatById(chatId);
            Chat scope = scopeId != null ? dao.getChatById(scopeId) : null;

            Message message = new Message(sender, text);
            chat.setLastMessage(message);
            dao.sendMessage(message, chat, scope);

            try {
                new GetChatsForUserCommand(UberSocketHandler.ACTIVE_SESSIONS, senderId).run();
                new GetMessagesForChatAndUserCommand(UberSocketHandler.ACTIVE_SESSIONS, senderId, chatId).run();

                Stream.concat(dao.getUsersByChat(chat).stream(), dao.getUsersByChat(scope).stream())
                        .map(User::getId)
                        .forEach(userId -> {
                            new GetChatsForUserCommand(sessions, userId).run();
                            new GetMessagesForChatAndUserCommand(sessions, userId, chatId).run();
                        });

            } catch (Exception e) {
                System.err.println("some shit happened during SendMessageCommand execution");
                e.printStackTrace();
            }
        }

        throw new IllegalStateException("Message is already sent!");
    }
}
