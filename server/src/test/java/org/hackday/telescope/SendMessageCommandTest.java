package org.hackday.telescope;

import org.hackday.telescope.commands.SendMessageCommand;
import org.hackday.telescope.dao.UberDao;
import org.hackday.telescope.models.Chat;
import org.hackday.telescope.models.Message;
import org.hackday.telescope.models.User;
import org.junit.Test;

/**
 * {@link org.hackday.telescope.commands.SendMessageCommand}
 */
public class SendMessageCommandTest {

    @Test
    public void test() {
        // given
        UberDao dao = UberDao.getInstance();

        User user1 = new User("User 1");
        User user2 = new User("User 2");

        dao.addUser(user1).addUser(user2);

        Chat chat = new Chat("chat 1", false);

        Message message = new Message(user1, "ti urod");

        // when
        String result = new SendMessageCommand("{\"usedId\": \"1\"}").call();

        // then

    }
}
