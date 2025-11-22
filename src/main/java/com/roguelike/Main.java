package com.roguelike;

import com.roguelike.ui.GameFrame;

import javax.swing.SwingUtilities;

/**
 * Hauptklasse des Roguelike-Spiels.
 * Startet das Spiel und initialisiert das GUI-Fenster.
 */
public class Main {
    /**
     * Einstiegspunkt der Anwendung.
     * Erstellt das Spielfenster im Event Dispatch Thread von Swing,
     * um Thread-Sicherheit zu gewährleisten.
     *
     * @param args Kommandozeilenargumente (nicht verwendet)
     */
    public static void main(String[] args) {
        // Führe die GUI-Erstellung im Swing Event Dispatch Thread aus
        SwingUtilities.invokeLater(() -> {
            GameFrame frame = new GameFrame();
            frame.setVisible(true);
        });
    }
}
