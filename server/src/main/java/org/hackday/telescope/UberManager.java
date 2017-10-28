package org.hackday.telescope;

import org.eclipse.jetty.websocket.api.Session;
import org.hackday.telescope.commands.FuncType;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 28/10/17.
 */
public class UberManager {
    public static List<Session> activeSessions = new ArrayList<Session>();
    static JSONParser parser = new JSONParser();

    public static String[] parseData(String message) throws ParseException {
        Object obj = parser.parse(message);
        JSONObject jsonObject = (JSONObject) obj;
        FuncType funcType = FuncType.of((String) jsonObject.get(FuncType.getFuncTypeKey()));
        switch (funcType) {
            case GET_CHATS:
                break;
            case GET_MESSAGES:
                break;
            case SEND_MESSAGE:
                break;
            default:
                throw new IllegalArgumentException("Wrong funcType");
        }
        return null;
    }

}
