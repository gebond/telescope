package org.hackday.telescope.commands;

import org.eclipse.jetty.websocket.api.Session;
import org.hackday.telescope.dao.UberDao;
import org.hackday.telescope.models.Chat;
import org.hackday.telescope.models.Message;
import org.hackday.telescope.models.User;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
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
        scopeId = json.getLong("scope_id");
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
            dao.sendMessage(message, chat, scope);

            try {
                session.getRemote().sendString(new JSONObject() {{
                    put("status", "ok");
                }}.toString());

                new GetChatsForUserCommand(session, sender.getId()).run();
                Stream.concat(dao.getUsersByChat(chat).stream(), dao.getUsersByChat(scope).stream())
                        .map(User::getId)
                        .forEach(userId -> new GetChatsForUserCommand(session, userId).run());

            } catch (IOException e) {
                System.err.println("some shit happened during SendMessageCommand execution");
                e.printStackTrace();
            }
        }

        throw new IllegalStateException("Message is already sent!");
    }
}
