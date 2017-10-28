package org.hackday.telescope;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;

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
        ACTIVE_SESSIONS.add(session);
        System.out.println("Session established");
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
//        String result = UberManager.execute(message);
//        session.getRemote().sendString(result);
//
//        if ()

        System.out.println("Message: " + message);
    }
}