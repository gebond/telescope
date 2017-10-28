package org.hackday.telescope;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.websocket.api.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 28/10/17.
 */
public class UberManager {
    public static List<Session> activeSessions = new ArrayList<Session>();
    public static ObjectMapper objectMapper = new ObjectMapper();

    public static String[] parseData(String message){

        return message.split(message, ',');
    }
}
