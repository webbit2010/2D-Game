package com.roguelike.world;

import java.awt.Color;

/**
 * Enum für Dungeon-Tiles.
 * Jeder Tile-Typ hat Zeichen, Farbe, Begehbarkeit und Transparenz.
 */
public enum Tile {
    /** Wand - blockiert Bewegung und Sicht */
    WALL('#', new Color(130, 110, 50), false, false),

    /** Boden - begehbar und transparent */
    FLOOR('.', new Color(50, 50, 150), true, true),

    /** Treppe nach unten */
    STAIRS_DOWN('>', Color.WHITE, true, true),

    /** Treppe nach oben (derzeit nicht verwendet) */
    STAIRS_UP('<', Color.WHITE, true, true);

    /** Darstellungszeichen */
    private final char character;

    /** Darstellungsfarbe */
    private final Color color;

    /** Kann man darauf laufen? */
    private final boolean walkable;

    /** Ist es transparent für Sichtlinien? */
    private final boolean transparent;

    /**
     * Konstruktor für Tile-Typ.
     *
     * @param character Darstellungszeichen
     * @param color Farbe
     * @param walkable Begehbar?
     * @param transparent Transparent für FOV?
     */
    Tile(char character, Color color, boolean walkable, boolean transparent) {
        this.character = character;
        this.color = color;
        this.walkable = walkable;
        this.transparent = transparent;
    }

    public char getCharacter() {
        return character;
    }

    public Color getColor() {
        return color;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public boolean isTransparent() {
        return transparent;
    }
}
