package org.hackday.telescope;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.hackday.telescope.rest.RestServlet;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


/**
 * Created on 28/10/17.
 */
public class Application {

//    private static final HikariConfig config = new HikariConfig();
//    private static DataSource ds;

    public static void main(String[] args) throws Exception {

        int port = 5000;

        if (args.length > 0) {
            port = Integer.valueOf(args[0]);
        }

//        config.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
//        config.setUsername("postgres");
//        config.setPassword("telescope");
//        ds = new HikariDataSource(config);

        Server server = new Server(port);

        WebSocketHandler wsHandler = new WebSocketHandler() {
            @Override
            public void configure(WebSocketServletFactory factory) {
                factory.register(UberSocketHandler.class);
            }
        };

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/rest");
        context.addServlet(new ServletHolder(new RestServlet()), "/login");

        HandlerCollection handlerCollection = new HandlerCollection();
        handlerCollection.setHandlers(new Handler[] {
                context,
                wsHandler
        });

        server.setHandler(handlerCollection);

        server.start();
        server.join();
    }

//    public static Connection getDbConnection() throws SQLException {
//        return ds.getConnection();
//    }
}
