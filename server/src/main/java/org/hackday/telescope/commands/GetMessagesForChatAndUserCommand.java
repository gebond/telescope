package org.hackday.telescope.commands;

import org.hackday.telescope.dao.UberDao;
import org.hackday.telescope.models.Chat;
import org.hackday.telescope.models.Message;
import org.hackday.telescope.models.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GetMessagesForChatAndUserCommand implements Command {

    private UberDao dao = UberDao.getInstance();

    private Long userId;
    private Long chatId;

    public GetMessagesForChatAndUserCommand(String input) {
        JSONObject json = new JSONObject(input);

        userId = json.getLong("user_id");
        chatId = json.getLong("chat_id");
    }

    @Override
    public String call() {
        User user = dao.getUserById(userId);
        Chat chat = dao.getChatById(chatId);

        Set<Chat> userScopes = dao.getChatsByUser(user).stream()
                .filter(Chat::isScope)
                .collect(Collectors.toSet());

        List<Message> filteredMessages = dao.getMessagesByChat(chat).stream()
                .filter(message -> userScopes.contains(dao.getScopeByMessage(message)))
                .collect(Collectors.toList());

        return new JSONObject() {{
            put("messages", new JSONArray(filteredMessages.stream()
                    .map(message -> new JSONObject() {{
                        put("body", message.getText());
                        put("time", message.getTime());
                        put("sender_id", message.getSender().getId());
                        put("scope_id", dao.getScopeByMessage(message).getId());
                        put("scope_name", dao.getScopeByMessage(message).getName());
                    }})
                    .collect(Collectors.toList())));
        }}.toString();
    }
}
