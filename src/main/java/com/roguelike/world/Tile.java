package com.roguelike.world;

import java.awt.Color;

public enum Tile {
    WALL('#', new Color(130, 110, 50), false, false),
    FLOOR('.', new Color(50, 50, 150), true, true),
    STAIRS_DOWN('>', Color.WHITE, true, true),
    STAIRS_UP('<', Color.WHITE, true, true);

    private final char character;
    private final Color color;
    private final boolean walkable;
    private final boolean transparent;

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
