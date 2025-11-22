package com.roguelike.world;

import com.roguelike.engine.Position;
import java.util.Random;

public class Room {
    private int x1, y1, x2, y2;

    public Room(int x, int y, int width, int height) {
        this.x1 = x;
        this.y1 = y;
        this.x2 = x + width;
        this.y2 = y + height;
    }

    public Position getCenter() {
        int centerX = (x1 + x2) / 2;
        int centerY = (y1 + y2) / 2;
        return new Position(centerX, centerY);
    }

    public Position getRandomPosition(Random random) {
        int x = x1 + 1 + random.nextInt(x2 - x1 - 1);
        int y = y1 + 1 + random.nextInt(y2 - y1 - 1);
        return new Position(x, y);
    }

    public boolean intersects(Room other) {
        return (x1 <= other.x2 && x2 >= other.x1 &&
                y1 <= other.y2 && y2 >= other.y1);
    }

    public int getX1() { return x1; }
    public int getY1() { return y1; }
    public int getX2() { return x2; }
    public int getY2() { return y2; }
}
