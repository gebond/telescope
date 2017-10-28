package org.hackday.telescope.commands;

import org.hackday.telescope.dao.UberDao;
import org.hackday.telescope.models.Chat;
import org.hackday.telescope.models.Message;
import org.hackday.telescope.models.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GetMessagesForChatAndUserCommand implements Command {

    private UberDao dao = UberDao.getInstance();

    private Long userId;
    private Long chatId;

    public GetMessagesForChatAndUserCommand(String input) {
        // Do magic
        userId = 42L;
        chatId = 42L;
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

        // TODO:
        return "kek";
    }
}
