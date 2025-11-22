package com.roguelike.world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.roguelike.engine.Position;
import com.roguelike.entity.Enemy;
import com.roguelike.entity.Entity;
import com.roguelike.item.Item;

public class GameMap {
    private final Tile[][] tiles;
    private final boolean[][] explored;
    private final boolean[][] visible;
    private final List<Entity> entities;
    private final List<Item> items;
    private final List<Room> rooms;
    private final int width;
    private final int height;

    public GameMap(Tile[][] tiles, List<Room> rooms) {
        this.tiles = tiles;
        this.rooms = rooms;
        this.width = tiles.length;
        this.height = tiles[0].length;
        this.explored = new boolean[width][height];
        this.visible = new boolean[width][height];
        this.entities = new ArrayList<>();
        this.items = new ArrayList<>();
    }

    public void populateMonsters(int maxMonstersPerRoom) {
        Random random = new Random();

        // Skip first room (where player starts)
        for (int i = 1; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            int numMonsters = random.nextInt(maxMonstersPerRoom + 1);

            for (int j = 0; j < numMonsters; j++) {
                Position pos = room.getRandomPosition(random);
                if (!isBlocked(pos)) {
                    Enemy enemy = Enemy.createRandomEnemy(pos);
                    entities.add(enemy);
                }
            }
        }
    }

    public void populateItems(int maxItemsPerRoom) {
        Random random = new Random();

        for (int i = 1; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            int numItems = random.nextInt(maxItemsPerRoom + 1);

            for (int j = 0; j < numItems; j++) {
                Position pos = room.getRandomPosition(random);
                if (!isBlocked(pos)) {
                    Item item;
                    int itemType = random.nextInt(3);
                    switch (itemType) {
                        case 0: item = Item.createHealthPotion(pos); break;
                        case 1: item = Item.createSword(pos); break;
                        case 2: item = Item.createShield(pos); break;
                        default: item = Item.createHealthPotion(pos);
                    }
                    items.add(item);
                }
            }
        }
    }

    public Tile getTile(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return tiles[x][y];
        }
        return Tile.WALL;
    }

    public boolean isBlocked(Position pos) {
        if (!getTile(pos.x, pos.y).isWalkable()) {
            return true;
        }

        for (Entity entity : entities) {
            if (entity.isBlocking() && entity.getPosition().equals(pos)) {
                return true;
            }
        }

        return false;
    }

    public Entity getBlockingEntityAt(Position pos) {
        for (Entity entity : entities) {
            if (entity.isBlocking() && entity.getPosition().equals(pos)) {
                return entity;
            }
        }
        return null;
    }

    public Item getItemAt(Position pos) {
        for (Item item : items) {
            if (item.getPosition().equals(pos)) {
                return item;
            }
        }
        return null;
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public void removeDeadEntities() {
        Iterator<Entity> iterator = entities.iterator();
        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            if (!entity.isAlive()) {
                iterator.remove();
            }
        }
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public List<Item> getItems() {
        return items;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isExplored(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return explored[x][y];
        }
        return false;
    }

    public void setExplored(int x, int y, boolean value) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            explored[x][y] = value;
        }
    }

    public boolean isVisible(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return visible[x][y];
        }
        return false;
    }

    public void setVisible(int x, int y, boolean value) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            visible[x][y] = value;
        }
    }

    public void clearVisibility() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                visible[x][y] = false;
            }
        }
    }
}
