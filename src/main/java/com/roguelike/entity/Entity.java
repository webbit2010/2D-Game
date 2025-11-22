package com.roguelike.entity;

import com.roguelike.engine.Position;
import java.awt.Color;

/**
 * Abstrakte Basisklasse für alle Spielentitäten (Spieler, Gegner).
 * Enthält gemeinsame Eigenschaften wie Position, Lebenspunkte und Kampfwerte.
 */
public abstract class Entity {
    /** Position der Entity auf der Karte */
    protected Position position;

    /** Zeichen zur Darstellung im Spiel */
    protected char character;

    /** Farbe zur Darstellung */
    protected Color color;

    /** Name der Entity */
    protected String name;

    /** Maximale Lebenspunkte */
    protected int maxHp;

    /** Aktuelle Lebenspunkte */
    protected int hp;

    /** Angriffswert */
    protected int attack;

    /** Verteidigungswert */
    protected int defense;

    /** Blockiert diese Entity Bewegung? */
    protected boolean blocking;

    /** Ist diese Entity am Leben? */
    protected boolean alive;

    /**
     * Konstruktor für eine neue Entity.
     *
     * @param position Startposition
     * @param character Darstellungszeichen
     * @param color Darstellungsfarbe
     * @param name Name der Entity
     * @param maxHp Maximale Lebenspunkte
     * @param attack Angriffswert
     * @param defense Verteidigungswert
     * @param blocking Blockiert Bewegung?
     */
    public Entity(Position position, char character, Color color, String name,
                  int maxHp, int attack, int defense, boolean blocking) {
        this.position = position;
        this.character = character;
        this.color = color;
        this.name = name;
        this.maxHp = maxHp;
        this.hp = maxHp; // Startet mit vollen HP
        this.attack = attack;
        this.defense = defense;
        this.blocking = blocking;
        this.alive = true;
    }

    // Getter und Setter

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public char getCharacter() {
        return character;
    }

    public Color getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public boolean isBlocking() {
        return blocking;
    }

    public boolean isAlive() {
        return alive;
    }

    /**
     * Fügt der Entity Schaden zu.
     * Ruft die() auf, wenn HP auf 0 oder weniger fallen.
     *
     * @param amount Schadenshöhe
     */
    public void takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) {
            hp = 0;
            die();
        }
    }

    /**
     * Heilt die Entity um eine bestimmte Menge.
     * HP können nicht über maxHp hinausgehen.
     *
     * @param amount Heilungsmenge
     */
    public void heal(int amount) {
        hp = Math.min(hp + amount, maxHp);
    }

    /**
     * Wird aufgerufen, wenn die Entity stirbt.
     * Ändert Darstellung zu Leiche und entfernt Blockierung.
     */
    protected void die() {
        alive = false;
        blocking = false; // Leichen blockieren nicht
        character = '%'; // Leichensymbol
        color = new Color(191, 0, 0); // Dunkles Rot
        name = "remains of " + name;
    }

    /**
     * Update-Methode für KI oder Spielerlogik.
     * Muss von Subklassen implementiert werden.
     */
    public abstract void update();
}
