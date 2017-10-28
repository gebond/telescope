package org.hackday.telescope.commands;

import org.eclipse.jetty.websocket.api.Session;

import java.util.concurrent.Callable;

public abstract class Command implements Runnable {

    protected Session session;

    public Command(Session session) {
        this.session = session;
    }
}
