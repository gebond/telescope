package org.hackday.telescope.commands;

import org.hackday.telescope.dao.UberDao;
import org.hackday.telescope.models.Chat;
import org.hackday.telescope.models.Message;
import org.hackday.telescope.models.User;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicBoolean;

public class SendMessageCommand implements Command {

    private UberDao dao = UberDao.getInstance();

    private Long senderId;
    private Long chatId;
    private Long scopeId;
    private String text;
    private AtomicBoolean executed;

    public SendMessageCommand(String input) {
        JSONObject json = new JSONObject(input);

        senderId = json.getLong("sender_id");
        chatId = json.getLong("chat_id");
        scopeId = json.getLong("scope_id");
        text = json.getString("text");

        executed = new AtomicBoolean(false);
    }

    @Override
    public String call() {
        if (!executed.get()) {
            // Create and send new message
            User sender = dao.getUserById(senderId);

            Chat chat = dao.getChatById(chatId);
            Chat scope = scopeId != null ? dao.getChatById(scopeId) : null;

            Message message = new Message(sender, text);
            dao.sendMessage(message, chat, scope);

            // TODO: Notify receivers of the message
            return "kek";
        }
        return "not kek";
    }
}
