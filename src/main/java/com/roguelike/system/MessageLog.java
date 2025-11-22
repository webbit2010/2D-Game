package com.roguelike.system;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Nachrichtenprotokoll für Spielereignisse.
 * Speichert farbcodierte Nachrichten mit automatischer Größenbegrenzung.
 */
public class MessageLog {
    /** Liste der Nachrichten */
    private List<Message> messages;

    /** Maximale Anzahl gespeicherter Nachrichten */
    private int maxMessages;

    /**
     * Erstellt ein neues MessageLog.
     *
     * @param maxMessages Maximale Anzahl Nachrichten (älteste werden gelöscht)
     */
    public MessageLog(int maxMessages) {
        this.maxMessages = maxMessages;
        this.messages = new ArrayList<>();
    }

    /**
     * Fügt eine Nachricht mit Typ hinzu.
     *
     * @param text Nachrichtentext
     * @param type Nachrichtentyp (bestimmt Farbe)
     */
    public void addMessage(String text, MessageType type) {
        messages.add(new Message(text, type));
        // Entferne älteste Nachricht bei Überschreitung
        if (messages.size() > maxMessages) {
            messages.remove(0);
        }
    }

    /**
     * Fügt eine Info-Nachricht hinzu (weiße Farbe).
     *
     * @param text Nachrichtentext
     */
    public void addMessage(String text) {
        addMessage(text, MessageType.INFO);
    }

    /**
     * Gibt Kopie aller Nachrichten zurück.
     *
     * @return Liste aller Nachrichten
     */
    public List<Message> getMessages() {
        return new ArrayList<>(messages);
    }

    /**
     * Repräsentiert eine einzelne Nachricht.
     */
    public static class Message {
        /** Nachrichtentext */
        public final String text;

        /** Nachrichtentyp */
        public final MessageType type;

        /** Farbe der Nachricht */
        public final Color color;

        public Message(String text, MessageType type) {
            this.text = text;
            this.type = type;
            this.color = type.getColor();
        }
    }

    /**
     * Typen von Nachrichten mit zugehörigen Farben.
     */
    public enum MessageType {
        /** Allgemeine Information (Weiß) */
        INFO(Color.WHITE),

        /** Kampfnachricht (Orange) */
        COMBAT(new Color(255, 165, 0)),

        /** Wichtige Nachricht (Rot) */
        IMPORTANT(new Color(255, 0, 0)),

        /** Spieler-Angriff (Hellgrau) */
        PLAYER_ATTACK(new Color(224, 224, 224)),

        /** Gegner-Angriff (Rosa) */
        ENEMY_ATTACK(new Color(255, 192, 203));

        /** Farbe des Nachrichtentyps */
        private final Color color;

        MessageType(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }
    }
}
