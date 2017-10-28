package org.hackday.telescope.rest;

import org.hackday.telescope.db.dao.UberDao;
import org.hackday.telescope.models.User;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RestServlet extends HttpServlet {

    private UberDao dao = UberDao.getInstance();

    public void init() throws ServletException {
        System.out.println("RestServlet initialization...");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");

        String username = request.getParameter("username");

        User user = dao.getOrCreateUserByName(username);
        String responseString = new JSONObject() {{
            put("method", "login");
            put("payload", new JSONObject() {{
                put("user_id", user.getId());
            }});
        }}.toString();

        response.getWriter().println(responseString);
        response.addHeader("Access-Control-Allow-Origin", "*");
    }

    public void destroy() {
        // do nothing.
    }
}