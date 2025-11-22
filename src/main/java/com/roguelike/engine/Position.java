package com.roguelike.engine;

import java.util.Objects;

/**
 * Repräsentiert eine 2D-Position auf der Spielkarte.
 * Unveränderlich (immutable) - alle Werte sind final.
 */
public class Position {
    /** X-Koordinate auf der Karte */
    public final int x;

    /** Y-Koordinate auf der Karte */
    public final int y;

    /**
     * Erstellt eine neue Position.
     *
     * @param x die X-Koordinate
     * @param y die Y-Koordinate
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Addiert einen Versatz zur aktuellen Position und gibt eine neue Position zurück.
     * Nützlich für Bewegungsberechnungen.
     *
     * @param dx Versatz in X-Richtung
     * @param dy Versatz in Y-Richtung
     * @return neue Position mit addiertem Versatz
     */
    public Position add(int dx, int dy) {
        return new Position(x + dx, y + dy);
    }

    /**
     * Berechnet die euklidische Distanz zu einer anderen Position.
     * Verwendet den Satz des Pythagoras: sqrt(dx² + dy²)
     *
     * @param other die andere Position
     * @return die Distanz als Fließkommazahl
     */
    public double distanceTo(Position other) {
        int dx = x - other.x;
        int dy = y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Vergleicht diese Position mit einem anderen Objekt auf Gleichheit.
     * Zwei Positionen sind gleich, wenn ihre X- und Y-Koordinaten gleich sind.
     *
     * @param o das zu vergleichende Objekt
     * @return true wenn gleich, sonst false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    /**
     * Generiert einen Hashcode für diese Position.
     * Wird für HashMap und HashSet verwendet.
     *
     * @return der Hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Gibt eine String-Repräsentation dieser Position zurück.
     *
     * @return String im Format "(x, y)"
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
