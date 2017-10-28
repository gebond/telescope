package org.hackday.telescope.commands;

import org.hackday.telescope.UberManager.Method;
import org.json.JSONObject;

import java.util.List;

public class CommandResult {
    private JSONObject json;
    private List<Long> affectedUsers;

    public CommandResult(JSONObject json, List<Long> affectedUsers) {
        this.json = json;
        this.affectedUsers = affectedUsers;
    }
}
