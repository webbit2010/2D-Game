package com.roguelike.system;

import com.roguelike.engine.Position;
import com.roguelike.world.GameMap;

public class FOVSystem {

    public static void computeFOV(GameMap map, Position origin, int radius) {
        map.clearVisibility();

        for (int x = origin.x - radius; x <= origin.x + radius; x++) {
            for (int y = origin.y - radius; y <= origin.y + radius; y++) {
                if (x >= 0 && x < map.getWidth() && y >= 0 && y < map.getHeight()) {
                    double distance = origin.distanceTo(new Position(x, y));
                    if (distance <= radius) {
                        if (hasLineOfSight(map, origin, new Position(x, y))) {
                            map.setVisible(x, y, true);
                            map.setExplored(x, y, true);
                        }
                    }
                }
            }
        }
    }

    private static boolean hasLineOfSight(GameMap map, Position start, Position end) {
        int x0 = start.x;
        int y0 = start.y;
        int x1 = end.x;
        int y1 = end.y;

        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;

        while (true) {
            if (x0 == x1 && y0 == y1) {
                return true;
            }

            if (!map.getTile(x0, y0).isTransparent()) {
                return false;
            }

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
