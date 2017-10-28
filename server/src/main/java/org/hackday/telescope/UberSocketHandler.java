package org.hackday.telescope;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.simple.parser.ParseException;

import static org.hackday.telescope.UberManager.ACTIVE_SESSIONS;

@WebSocket
public class UberSocketHandler {

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
        try {
            session.getRemote().sendString("Hello Webbrowser");
            ACTIVE_SESSIONS.add(session);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws ParseException {
        String[] args = UberManager.execute(message);
        System.out.println("Message: " + message);
    }
}