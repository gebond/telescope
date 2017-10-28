package org.hackday.telescope;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.hackday.telescope.commands.*;
import org.json.JSONObject;

@WebSocket
public class UberSocketHandler {

    public static final List<Session> ACTIVE_SESSIONS = new ArrayList<Session>();

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        ACTIVE_SESSIONS.remove(session);
        System.out.println("Close: statusCode=" + statusCode + ", reason=" + reason);
    }

    @OnWebSocketError
    public void onError(Session session, Throwable t) {
        ACTIVE_SESSIONS.remove(session);
        System.out.println("Error: " + t.getMessage());
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("Connect: " + session.getRemoteAddress().getAddress());
        ACTIVE_SESSIONS.add(session);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            Command command = Method.valueOf(jsonObject.getString("method").toUpperCase())
                    .createCommand(session, jsonObject.getString("payload"));
            command.run();
        } catch (Exception e) {
            // poshel nahuy
        }

        System.out.println("Message: " + message);
    }

    public enum Method {
        SEND_MESSAGE {
            @Override
            public Command createCommand(Session session, String input) {
                return new SendMessageCommand(session, input);
            }
        },
        GET_CHATS {
            @Override
            public Command createCommand(Session session, String string) {
                return new GetChatsForUserCommand(session, string);
            }
        },
        GET_MESSAGES {
            @Override
            public Command createCommand(Session session, String input) {
                return new GetMessagesForChatAndUserCommand(session, input);
            }
        },
        LOGIN {
            @Override
            public Command createCommand(Session session, String input) {
                return new LoginCommand(session, input);
            }
        },
        CREATE_CHAT {
            @Override
            public Command createCommand(Session session, String input) {
                return new CreateChatCommand(session, input);
            }
        },
        INVITE_TO_CHAT {
            @Override
            public Command createCommand(Session session, String input) {
                return new InviteToChatCommand(session, input);
            }
        },
        FORWARD_MESSAGE {
            @Override
            public Command createCommand(Session session, String input) {
                return null; // TODO: input: message_id, from_chat_id, to_chat_id
            }
        };

        public abstract Command createCommand(Session session, String input);
    }
}