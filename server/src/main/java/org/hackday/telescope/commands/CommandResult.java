package org.hackday.telescope.commands;

import org.json.JSONObject;

import java.util.List;

public class CommandResult {
    private JSONObject json;
    private List<Long> affectedUsers;

    public CommandResult(JSONObject json, List<Long> affectedUsers) {
        this.json = json;
        this.affectedUsers = affectedUsers;
    }

    public JSONObject getJson() {
        return json;
    }

    public List<Long> getAffectedUsers() {
        return affectedUsers;
    }
}
