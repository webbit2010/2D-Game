package com.roguelike.item;

import com.roguelike.engine.Position;
import java.awt.Color;

/**
 * Repräsentiert ein Item in der Spielwelt.
 * Items können Heiltränke, Waffen oder Rüstungen sein.
 */
public class Item {
    /** Position des Items auf der Karte */
    private Position position;

    /** Darstellungszeichen */
    private char character;

    /** Darstellungsfarbe */
    private Color color;

    /** Name des Items */
    private String name;

    /** Typ des Items (Heilung, Waffe, Rüstung) */
    private ItemType type;

    /** Wert des Items (Heilmenge, ATK-Bonus, DEF-Bonus) */
    private int value;

    /**
     * Konstruktor für ein neues Item.
     *
     * @param position Position auf der Karte
     * @param character Darstellungszeichen
     * @param color Darstellungsfarbe
     * @param name Name des Items
     * @param type Item-Typ
     * @param value Wert (abhängig vom Typ)
     */
    public Item(Position position, char character, Color color, String name, ItemType type, int value) {
        this.position = position;
        this.character = character;
        this.color = color;
        this.name = name;
        this.type = type;
        this.value = value;
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

    public ItemType getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    // Factory-Methoden für Item-Erstellung

    /**
     * Erstellt einen Heiltrank.
     * Heilt 20 HP beim Benutzen.
     *
     * @param position Spawnposition
     * @return neuer Heiltrank
     */
    public static Item createHealthPotion(Position position) {
        return new Item(position, '!', new Color(127, 0, 255), "Health Potion", ItemType.HEALING, 20);
    }

    /**
     * Erstellt ein Schwert.
     * Gewährt +3 ATK beim Ausrüsten.
     *
     * @param position Spawnposition
     * @return neues Schwert
     */
    public static Item createSword(Position position) {
        return new Item(position, '/', new Color(0, 191, 255), "Sword", ItemType.WEAPON, 3);
    }

    /**
     * Erstellt ein Schild.
     * Gewährt +2 DEF beim Ausrüsten.
     *
     * @param position Spawnposition
     * @return neues Schild
     */
    public static Item createShield(Position position) {
        return new Item(position, '[', new Color(139, 69, 19), "Shield", ItemType.ARMOR, 2);
    }

    /**
     * Aufzählung der Item-Typen.
     */
    public enum ItemType {
        /** Heiltrank - stellt HP wieder her */
        HEALING,

        /** Waffe - erhöht Angriff */
        WEAPON,

        /** Rüstung - erhöht Verteidigung */
        ARMOR
    }
}
