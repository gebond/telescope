package org.hackday.telescope.commands;

import org.hackday.telescope.dao.UberDao;
import org.hackday.telescope.models.Chat;
import org.hackday.telescope.models.User;

import java.util.List;

public class GetChatsForUserCommand implements Command {

    private UberDao dao = UberDao.getInstance();

    private Long userId;

    public GetChatsForUserCommand(String input) {
        // Do magic
        userId = 42L;
    }

    @Override
    public String call() {
        User user = dao.getUserById(userId);

        List<Chat> chats = dao.getChatsByUser(user);

        // TODO: Send result somewhere
        return "kek";
    }
}
