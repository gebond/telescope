package org.hackday.telescope.commands;

import org.hackday.telescope.models.Message;

import java.util.concurrent.atomic.AtomicBoolean;

public class SendMessageCommand implements Command {

    private Long senderId;
    private Long chatId;
    private Long scopeId;
    private String text;
    private AtomicBoolean executed;

    public SendMessageCommand(String input) {
        //Do magic
        senderId = 42L;
        chatId = 42L;
        scopeId = 42L;
        text = input;
        executed = new AtomicBoolean(false);
    }

    @Override
    public void run() {
        if (!executed.get()) {
            //Create new message
        }
    }
}
