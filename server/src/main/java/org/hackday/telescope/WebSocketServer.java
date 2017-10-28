package org.hackday.telescope;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;


/**
 * Created on 28/10/17.
 */
public class WebSocketServer {

    public static void main(String[] args) throws Exception {
        int port = 5000;

        if (args.length > 0) {
            port = Integer.valueOf(args[0]);
        }

        Server server = new Server(port);
        WebSocketHandler wsHandler = new WebSocketHandler() {
            @Override
            public void configure(WebSocketServletFactory factory) {
                factory.register(UberSocketHandler.class);
            }
        };

        server.setHandler(wsHandler);
        server.start();
        server.join();
    }
}
