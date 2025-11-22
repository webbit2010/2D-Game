package com.roguelike.system;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class MessageLog {
    private List<Message> messages;
    private int maxMessages;

    public MessageLog(int maxMessages) {
        this.maxMessages = maxMessages;
        this.messages = new ArrayList<>();
    }

    public void addMessage(String text, MessageType type) {
        messages.add(new Message(text, type));
        if (messages.size() > maxMessages) {
            messages.remove(0);
        }
    }

    public void addMessage(String text) {
        addMessage(text, MessageType.INFO);
    }

    public List<Message> getMessages() {
        return new ArrayList<>(messages);
    }

    public static class Message {
        public final String text;
        public final MessageType type;
        public final Color color;

        public Message(String text, MessageType type) {
            this.text = text;
            this.type = type;
            this.color = type.getColor();
        }
    }

    public enum MessageType {
        INFO(Color.WHITE),
        COMBAT(new Color(255, 165, 0)),
        IMPORTANT(new Color(255, 0, 0)),
        PLAYER_ATTACK(new Color(224, 224, 224)),
        ENEMY_ATTACK(new Color(255, 192, 203));

        private final Color color;

        MessageType(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }
    }
}
