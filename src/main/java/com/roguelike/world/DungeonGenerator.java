package com.roguelike.world;

import com.roguelike.engine.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DungeonGenerator {
    private static final int ROOM_MAX_SIZE = 10;
    private static final int ROOM_MIN_SIZE = 6;
    private static final int MAX_ROOMS = 30;

    private Random random;

    public DungeonGenerator(long seed) {
        this.random = new Random(seed);
    }

    public DungeonGenerator() {
        this(System.currentTimeMillis());
    }

    public GameMap generateDungeon(int width, int height) {
        Tile[][] tiles = new Tile[width][height];

        // Fill with walls
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = Tile.WALL;
            }
        }

        List<Room> rooms = new ArrayList<>();

        for (int i = 0; i < MAX_ROOMS; i++) {
            int w = ROOM_MIN_SIZE + random.nextInt(ROOM_MAX_SIZE - ROOM_MIN_SIZE + 1);
            int h = ROOM_MIN_SIZE + random.nextInt(ROOM_MAX_SIZE - ROOM_MIN_SIZE + 1);
            int x = random.nextInt(width - w - 1);
            int y = random.nextInt(height - h - 1);

            Room newRoom = new Room(x, y, w, h);

            boolean intersects = false;
            for (Room room : rooms) {
                if (newRoom.intersects(room)) {
                    intersects = true;
                    break;
                }
            }

            if (!intersects) {
                createRoom(tiles, newRoom);

                if (!rooms.isEmpty()) {
                    Position newCenter = newRoom.getCenter();
                    Position prevCenter = rooms.get(rooms.size() - 1).getCenter();

                    if (random.nextBoolean()) {
                        createHTunnel(tiles, prevCenter.x, newCenter.x, prevCenter.y);
                        createVTunnel(tiles, prevCenter.y, newCenter.y, newCenter.x);
                    } else {
                        createVTunnel(tiles, prevCenter.y, newCenter.y, prevCenter.x);
                        createHTunnel(tiles, prevCenter.x, newCenter.x, newCenter.y);
                    }
                }

                rooms.add(newRoom);
            }
        }

        // Add stairs
        if (!rooms.isEmpty()) {
            Position stairsPos = rooms.get(rooms.size() - 1).getCenter();
            tiles[stairsPos.x][stairsPos.y] = Tile.STAIRS_DOWN;
        }

        return new GameMap(tiles, rooms);
    }

    private void createRoom(Tile[][] tiles, Room room) {
        for (int x = room.getX1() + 1; x < room.getX2(); x++) {
            for (int y = room.getY1() + 1; y < room.getY2(); y++) {
                tiles[x][y] = Tile.FLOOR;
            }
        }
    }

    private void createHTunnel(Tile[][] tiles, int x1, int x2, int y) {
        for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
            if (x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length) {
                tiles[x][y] = Tile.FLOOR;
            }
        }
    }

    private void createVTunnel(Tile[][] tiles, int y1, int y2, int x) {
        for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
            if (x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length) {
                tiles[x][y] = Tile.FLOOR;
            }
        }
    }
}
