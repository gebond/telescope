package org.hackday.telescope.commands;

import org.eclipse.jetty.websocket.api.Session;

import java.util.Collections;
import java.util.List;

public abstract class Command implements Runnable {

    protected List<Session> sessions;

    public Command(Session session) {
        this.sessions = Collections.singletonList(session);
    }

    public Command(List<Session> sessions) {
        this.sessions = sessions;
    }
}
