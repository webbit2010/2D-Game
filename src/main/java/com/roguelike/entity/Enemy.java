package com.roguelike.entity;

import com.roguelike.engine.Position;

import java.awt.Color;
import java.util.Random;

/**
 * Gegner-Entity mit KI und Erfahrungswert.
 * Bietet Factory-Methoden für verschiedene Gegnertypen (Goblin, Orc, Troll).
 */
public class Enemy extends Entity {
    /** Zufallszahlengenerator für Gegnererstellung */
    private static final Random random = new Random();

    /** Erfahrungspunkte, die der Spieler beim Töten dieses Gegners erhält */
    private final int expValue;

    /** KI-Komponente, die das Verhalten des Gegners steuert */
    private AI ai;

    /**
     * Konstruktor für einen neuen Gegner.
     *
     * @param position Startposition
     * @param name Name des Gegners
     * @param character Darstellungszeichen
     * @param color Darstellungsfarbe
     * @param maxHp Maximale Lebenspunkte
     * @param attack Angriffswert
     * @param defense Verteidigungswert
     * @param expValue Erfahrungspunkte beim Töten
     * @param ai KI-Komponente
     */
    public Enemy(Position position, String name, char character, Color color,
                 int maxHp, int attack, int defense, int expValue, AI ai) {
        super(position, character, color, name, maxHp, attack, defense, true);
        this.expValue = expValue;
        this.ai = ai;
    }

    public int getExpValue() {
        return expValue;
    }

    /**
     * Update-Methode - ruft die KI des Gegners auf.
     */
    @Override
    public void update() {
        if (ai != null && alive) {
            ai.update(this);
        }
    }

    public void setAI(AI ai) {
        this.ai = ai;
    }

    public AI getAI() {
        return ai;
    }

    // Factory-Methoden für verschiedene Gegnertypen

    /**
     * Erstellt einen Orc.
     * Stats: 10 HP, 3 ATK, 0 DEF, 35 EXP
     *
     * @param position Spawnposition
     * @return neuer Orc
     */
    public static Enemy createOrc(Position position) {
        return new Enemy(position, "Orc", 'o', new Color(0, 127, 0),
                        10, 3, 0, 35, new BasicAI());
    }

    /**
     * Erstellt einen Troll.
     * Stats: 16 HP, 4 ATK, 1 DEF, 100 EXP
     *
     * @param position Spawnposition
     * @return neuer Troll
     */
    public static Enemy createTroll(Position position) {
        return new Enemy(position, "Troll", 'T', new Color(0, 127, 0).darker(),
                        16, 4, 1, 100, new BasicAI());
    }

    /**
     * Erstellt einen Goblin.
     * Stats: 6 HP, 2 ATK, 0 DEF, 20 EXP
     *
     * @param position Spawnposition
     * @return neuer Goblin
     */
    public static Enemy createGoblin(Position position) {
        return new Enemy(position, "Goblin", 'g', new Color(127, 127, 0),
                        6, 2, 0, 20, new BasicAI());
    }

    /**
     * Erstellt einen zufälligen Gegner (Goblin, Orc oder Troll).
     *
     * @param position Spawnposition
     * @return zufälliger Gegner
     */
    public static Enemy createRandomEnemy(Position position) {
        int type = random.nextInt(3);
        switch (type) {
            case 0: return createGoblin(position);
            case 1: return createOrc(position);
            case 2: return createTroll(position);
            default: return createOrc(position);
        }
    }

    /**
     * Interface für Gegner-KI.
     * Implementierungen definieren das Verhalten von Gegnern.
     */
    public interface AI {
        /**
         * Update-Methode der KI.
         *
         * @param enemy Der Gegner, der von dieser KI gesteuert wird
         */
        void update(Enemy enemy);
    }

    /**
     * Basis-KI, die Gegner zum Spieler laufen und angreifen lässt.
     * Die eigentliche Logik wird in der Game-Klasse implementiert.
     */
    public static class BasicAI implements AI {
        @Override
        public void update(Enemy enemy) {
            // KI-Logik wird in der Game-Klasse implementiert,
            // da diese Zugriff auf den Spieler und die Karte hat
        }
    }
}
