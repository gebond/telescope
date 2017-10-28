package org.hackday.telescope.db.parsers;

import org.hackday.telescope.db.dao.UberDao;
import org.hackday.telescope.models.Chat;
import org.hackday.telescope.models.Message;
import org.hackday.telescope.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Mapper {

    private static UberDao dao = UberDao.getInstance();

    public static User mapUser(ResultSet rs) throws SQLException {
        Long id = rs.getLong(1);
        String name = rs.getString(2);

        return new User(id, name);
    }

    public static Chat mapChat(ResultSet rs) throws SQLException {
        Long id = rs.getLong(1);
        String name = rs.getString(2);
        Boolean scope = rs.getBoolean(3);

        return new Chat(id, name, scope);
    }

    public static Message mapMessage(ResultSet rs) throws SQLException {
        Long id = rs.getLong(1);
        String text = rs.getString(2);
        LocalDateTime time = rs.getTimestamp(3).toLocalDateTime();
        Long senderId = rs.getLong(4);

        // TODO: Relies on entity extraction order. Fuck it!
        User sender = dao.getUserById(senderId);

        return new Message(id, text, time, sender);
    }
}
