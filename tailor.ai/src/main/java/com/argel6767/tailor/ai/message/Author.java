package com.argel6767.tailor.ai.message;

/**
 * defines who is the author of a message, either the user, ChatGPT, or a System,
 * ie any instructions given to the Model to act/do a specific task when prompted by user
 */
public enum Author {
    USER,
    ASSISTANT,
    SYSTEM;
}
