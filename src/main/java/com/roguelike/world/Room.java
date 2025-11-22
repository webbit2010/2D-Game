package com.roguelike.world;

import java.util.Random;

import com.roguelike.engine.Position;

/**
 * Repräsentiert einen rechteckigen Raum im Dungeon.
 * Definiert durch zwei Eckpunkte (x1,y1) und (x2,y2).
 */
public class Room {
    /** Koordinaten der beiden Eckpunkte */
    private final int x1, y1, x2, y2;

    /**
     * Erstellt einen neuen Raum.
     *
     * @param x Linke obere Ecke X
     * @param y Linke obere Ecke Y
     * @param width Breite des Raums
     * @param height Höhe des Raums
     */
    public Room(int x, int y, int width, int height) {
        this.x1 = x;
        this.y1 = y;
        this.x2 = x + width;
        this.y2 = y + height;
    }

    /**
     * Berechnet die Mitte des Raums.
     *
     * @return Position der Raummitte
     */
    public Position getCenter() {
        int centerX = (x1 + x2) / 2;
        int centerY = (y1 + y2) / 2;
        return new Position(centerX, centerY);
    }

    /**
     * Gibt eine zufällige Position innerhalb des Raums zurück.
     * Position ist immer mindestens 1 Tile vom Rand entfernt.
     *
     * @param random Zufallsgenerator
     * @return Zufällige Position im Raum
     */
    public Position getRandomPosition(Random random) {
        int x = x1 + 1 + random.nextInt(x2 - x1 - 1);
        int y = y1 + 1 + random.nextInt(y2 - y1 - 1);
        return new Position(x, y);
    }

    /**
     * Prüft ob dieser Raum einen anderen Raum überschneidet.
     *
     * @param other Der andere Raum
     * @return true wenn Überschneidung, false sonst
     */
    public boolean intersects(Room other) {
        return (x1 <= other.x2 && x2 >= other.x1 &&
                y1 <= other.y2 && y2 >= other.y1);
    }

    // Getter für Eckpunkte

    public int getX1() { return x1; }
    public int getY1() { return y1; }
    public int getX2() { return x2; }
    public int getY2() { return y2; }
}
