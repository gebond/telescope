package org.hackday.telescope.commands;

/**
 * Created on 28/10/17.
 */
public enum FuncType {
    SEND_MESSAGE("send_message"),
    GET_CHATS("get_chats"),
    GET_MESSAGES("get_messages");

    private String value;
    private static String func_type_key = "func_type";

    FuncType(String message) {
        value = message;
    }

    public static String getFuncTypeKey() {
        return func_type_key;
    }

    public String getValue() {
        return value;
    }

    public static FuncType of(String message) {
        for (FuncType func_type : FuncType.values()) {
            if (func_type.value.endsWith(message)) {
                return func_type;
            }
        }
        throw new IllegalArgumentException("Funk type was not parsed" + message);
    }
}
