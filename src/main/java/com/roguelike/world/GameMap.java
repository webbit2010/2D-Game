package com.roguelike.world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.roguelike.engine.Position;
import com.roguelike.entity.Enemy;
import com.roguelike.entity.Entity;
import com.roguelike.item.Item;

/**
 * Spielkarte mit Tiles, Entities und Items.
 * Verwaltet die Dungeon-Struktur, Sichtbarkeit und alle Spielobjekte.
 */
public class GameMap {
    /** 2D-Array der Tiles (Wände, Böden, etc.) */
    private final Tile[][] tiles;

    /** Array zur Markierung erkundeter Bereiche (Fog of War) */
    private final boolean[][] explored;

    /** Array zur Markierung aktuell sichtbarer Bereiche (FOV) */
    private final boolean[][] visible;

    /** Liste aller Entities auf der Karte */
    private final List<Entity> entities;

    /** Liste aller Items auf der Karte */
    private final List<Item> items;

    /** Liste aller Räume im Dungeon */
    private final List<Room> rooms;

    /** Breite der Karte */
    private final int width;

    /** Höhe der Karte */
    private final int height;

    /**
     * Erstellt eine neue GameMap aus Tiles und Räumen.
     *
     * @param tiles 2D-Array der Dungeon-Tiles
     * @param rooms Liste der generierten Räume
     */
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

    /**
     * Bevölkert die Karte mit zufälligen Gegnern.
     * Überspringt den ersten Raum (Spieler-Startposition).
     *
     * @param maxMonstersPerRoom Maximale Anzahl Monster pro Raum
     */
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

    /**
     * Bevölkert die Karte mit zufälligen Items.
     * Überspringt den ersten Raum (Spieler-Startposition).
     *
     * @param maxItemsPerRoom Maximale Anzahl Items pro Raum
     */
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

    /**
     * Holt den Tile an einer bestimmten Position.
     * Gibt WALL zurück wenn Position außerhalb der Karte liegt.
     *
     * @param x X-Koordinate
     * @param y Y-Koordinate
     * @return Tile an der Position oder WALL wenn außerhalb
     */
    public Tile getTile(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return tiles[x][y];
        }
        return Tile.WALL;
    }

    /**
     * Prüft ob eine Position blockiert ist.
     * Berücksichtigt sowohl Tiles als auch blockierende Entities.
     *
     * @param pos Zu prüfende Position
     * @return true wenn blockiert, false sonst
     */
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

    /**
     * Findet die blockierende Entity an einer Position.
     *
     * @param pos Position zum Prüfen
     * @return Die blockierende Entity oder null wenn keine gefunden
     */
    public Entity getBlockingEntityAt(Position pos) {
        for (Entity entity : entities) {
            if (entity.isBlocking() && entity.getPosition().equals(pos)) {
                return entity;
            }
        }
        return null;
    }

    /**
     * Findet ein Item an einer Position.
     *
     * @param pos Position zum Prüfen
     * @return Das Item an der Position oder null wenn keines gefunden
     */
    public Item getItemAt(Position pos) {
        for (Item item : items) {
            if (item.getPosition().equals(pos)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Entfernt ein Item von der Karte.
     *
     * @param item Das zu entfernende Item
     */
    public void removeItem(Item item) {
        items.remove(item);
    }

    /**
     * Entfernt alle toten Entities von der Karte.
     * Wird nach jedem Spielerzug aufgerufen.
     */
    public void removeDeadEntities() {
        Iterator<Entity> iterator = entities.iterator();
        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            if (!entity.isAlive()) {
                iterator.remove();
            }
        }
    }

    /**
     * @return Liste aller Entities auf der Karte
     */
    public List<Entity> getEntities() {
        return entities;
    }

    /**
     * @return Liste aller Items auf der Karte
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * @return Liste aller Räume im Dungeon
     */
    public List<Room> getRooms() {
        return rooms;
    }

    /**
     * @return Breite der Karte
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return Höhe der Karte
     */
    public int getHeight() {
        return height;
    }

    /**
     * Prüft ob ein Tile bereits erkundet wurde (Fog of War).
     *
     * @param x X-Koordinate
     * @param y Y-Koordinate
     * @return true wenn erkundet, false sonst
     */
    public boolean isExplored(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return explored[x][y];
        }
        return false;
    }

    /**
     * Markiert einen Tile als erkundet oder nicht erkundet.
     *
     * @param x X-Koordinate
     * @param y Y-Koordinate
     * @param value true für erkundet, false für nicht erkundet
     */
    public void setExplored(int x, int y, boolean value) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            explored[x][y] = value;
        }
    }

    /**
     * Prüft ob ein Tile aktuell sichtbar ist (FOV).
     *
     * @param x X-Koordinate
     * @param y Y-Koordinate
     * @return true wenn sichtbar, false sonst
     */
    public boolean isVisible(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return visible[x][y];
        }
        return false;
    }

    /**
     * Setzt die Sichtbarkeit eines Tiles.
     *
     * @param x X-Koordinate
     * @param y Y-Koordinate
     * @param value true für sichtbar, false für nicht sichtbar
     */
    public void setVisible(int x, int y, boolean value) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            visible[x][y] = value;
        }
    }

    /**
     * Setzt alle Tiles auf nicht sichtbar.
     * Wird vor jedem FOV-Update aufgerufen.
     */
    public void clearVisibility() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                visible[x][y] = false;
            }
        }
    }
}
