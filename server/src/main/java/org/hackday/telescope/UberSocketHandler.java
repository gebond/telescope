package org.hackday.telescope;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.simple.parser.ParseException;

import static org.hackday.telescope.UberManager.activeSessions;

@WebSocket
public class UberSocketHandler {

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        activeSessions.remove(session);
        System.out.println("Close: statusCode=" + statusCode + ", reason=" + reason);
    }

    @OnWebSocketError
    public void onError(Session session, Throwable t) {
        activeSessions.remove(session);
        System.out.println("Error: " + t.getMessage());
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("Connect: " + session.getRemoteAddress().getAddress());
//        try {
        //session.getRemote().sendString("Hello Webbrowser");
        activeSessions.add(session);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws ParseException, IOException {
        //String[] args = UberManager.parseData(message);
        session.getRemote().sendString("{\"method\":\"get_chats\", \"payload\":\"{}\"}");
        System.out.println("Message: " + message);
    }
}