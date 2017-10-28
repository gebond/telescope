package org.hackday.telescope;

import org.eclipse.jetty.websocket.api.Session;
import org.hackday.telescope.commands.Command;
import org.hackday.telescope.commands.GetChatsForUserCommand;
import org.hackday.telescope.commands.GetMessagesForChatAndUserCommand;
import org.hackday.telescope.commands.SendMessageCommand;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 28/10/17.
 */
public class UberManager {
    public static final List<Session> ACTIVE_SESSIONS = new ArrayList<>();

    public static CommandResult execute(String message) throws Exception {
        for (Session activeSession : ACTIVE_SESSIONS) {
            activeSession.
        }
        JSONObject jsonObject = new JSONObject(message);
        Command command = Method.valueOf(jsonObject.getString("method").toUpperCase())
                .createCommand(jsonObject.getString("payload"));
        return command.call();
    }

    enum Method {
        SEND_MESSAGE {
            @Override
            public Command createCommand(String input) {
                return new SendMessageCommand(input);
            }
        },
        GET_CHATS {
            @Override
            public Command createCommand(String string) {
                return new GetChatsForUserCommand(string);
            }
        },
        GET_MESSAGES {
            @Override
            public Command createCommand(String input) {
                return new GetMessagesForChatAndUserCommand(input);
            }
        };

        public abstract Command createCommand(String input);
    }
}
