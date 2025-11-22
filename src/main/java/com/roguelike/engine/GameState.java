package com.roguelike.engine;

/**
 * Aufzählung der verschiedenen Spielzustände.
 * Steuert den Ablauf des Spiels und bestimmt, welche Aktionen erlaubt sind.
 */
public enum GameState {
    /** Hauptmenü - Spiel wurde noch nicht gestartet */
    MAIN_MENU,

    /** Allgemeiner Spielzustand - wird derzeit nicht aktiv verwendet */
    PLAYING,

    /** Spieler ist am Zug - Spieler kann Aktionen ausführen */
    PLAYER_TURN,

    /** Gegner sind am Zug - KI führt Aktionen aus */
    ENEMY_TURN,

    /** Spiel ist vorbei - Spieler ist gestorben */
    GAME_OVER,

    /** Sieg - Spieler hat gewonnen (derzeit nicht verwendet) */
    VICTORY
}
