package com.argel6767.tailor.ai.message.utils;

import com.argel6767.tailor.ai.message.Message;

import java.util.List;

/**
 * Util class that holds logic for flattening Message Lists (aka Message History)
 * into one String to allow for sending context back to AI
 */
public class MessageHistoryFlattener {

    /*
     * flattens message history
     */
    public static String flattenMessageHistory(List<Message> messages) {
        StringBuilder historyFlattened = new StringBuilder();
        for (Message message : messages) {
            historyFlattened.append(message.getAuthor());
            historyFlattened.append(": ");
            historyFlattened.append(message.getBody());
            historyFlattened.append("\n");
        }
        return historyFlattened.toString();
    }

    /*
     * adds new user message to entire message to flattenedHistory
     */
    public static String addNewUserMessage(String userMessage, String flattenedHistory) {
        return flattenedHistory + "User: " + userMessage + "\nAssistant";
    }
}
