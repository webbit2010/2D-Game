package com.roguelike.system;

import com.roguelike.engine.Position;
import com.roguelike.world.GameMap;

/**
 * Field of View (FOV) System - berechnet Sichtfeld und Sichtlinie.
 * Nutzt Raytracing mit Bresenham-Algorithmus für Line-of-Sight.
 */
public class FOVSystem {

    /**
     * Berechnet das Sichtfeld von einer Position aus.
     * Markiert alle sichtbaren Tiles als visible und explored.
     *
     * @param map Die Spielkarte
     * @param origin Ursprungsposition (z.B. Spielerposition)
     * @param radius Sichtreichweite in Tiles
     */
    public static void computeFOV(GameMap map, Position origin, int radius) {
        // Setze alle Tiles auf nicht-sichtbar
        map.clearVisibility();

        // Prüfe alle Tiles im Sichtradius
        for (int x = origin.x - radius; x <= origin.x + radius; x++) {
            for (int y = origin.y - radius; y <= origin.y + radius; y++) {
                // Prüfe Kartengrenzen
                if (x >= 0 && x < map.getWidth() && y >= 0 && y < map.getHeight()) {
                    double distance = origin.distanceTo(new Position(x, y));
                    if (distance <= radius) {
                        // Prüfe Line-of-Sight
                        if (hasLineOfSight(map, origin, new Position(x, y))) {
                            map.setVisible(x, y, true);
                            map.setExplored(x, y, true);
                        }
                    }
                }
            }
        }
    }

    /**
     * Prüft ob Sichtlinie zwischen zwei Punkten existiert.
     * Verwendet Bresenham's Line Algorithm für Raycasting.
     *
     * @param map Die Spielkarte
     * @param start Startpunkt
     * @param end Endpunkt
     * @return true wenn Sichtlinie frei, false wenn blockiert
     */
    private static boolean hasLineOfSight(GameMap map, Position start, Position end) {
        int x0 = start.x;
        int y0 = start.y;
        int x1 = end.x;
        int y1 = end.y;

        // Bresenham's Line Algorithm
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1; // Schrittrichtung X
        int sy = y0 < y1 ? 1 : -1; // Schrittrichtung Y
        int err = dx - dy;

        while (true) {
            // Ziel erreicht
            if (x0 == x1 && y0 == y1) {
                return true;
            }

            // Prüfe ob aktuelles Tile Sicht blockiert (Wand)
            if (!map.getTile(x0, y0).isTransparent()) {
                return false;
            }

            // Bresenham-Schritt
            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x0 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y0 += sy;
            }
        }
    }
}
