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
    public static final List<Session> ACTIVE_SESSIONS = new ArrayList<Session>();

    public static String[] execute(String message) throws Exception {
        JSONObject jsonObject = new JSONObject(message);
        Command command = Method.valueOf(jsonObject.getString("method").toUpperCase())
                .createCommand(jsonObject.getString("payload"));
        command.call();
        return null; // TODO
    }

    public enum Method {
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
        },
        CREATE_CHAT {
            @Override
            public Command createCommand(String input) {
                return null; // TODO: input: creator_id, chat_name; output: chat object
            }
        },
        INVITE_TO_CHAT {
            @Override
            public Command createCommand(String input) {
                return null; // TODO: input: target_user_id, target_chat_id; should notify target_user
            }
        },
        FORWARD_MESSAGE {
            @Override
            public Command createCommand(String input) {
                return null; // TODO: input: message_id, from_chat_id, to_chat_id
            }
        };


        public abstract Command createCommand(String input);
    }
}
