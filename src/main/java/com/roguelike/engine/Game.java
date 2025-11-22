package com.roguelike.engine;

import com.roguelike.entity.Enemy;
import com.roguelike.entity.Entity;
import com.roguelike.entity.Player;
import com.roguelike.item.Item;
import com.roguelike.system.CombatSystem;
import com.roguelike.system.FOVSystem;
import com.roguelike.system.MessageLog;
import com.roguelike.world.DungeonGenerator;
import com.roguelike.world.GameMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Hauptspielklasse - verwaltet den gesamten Spielablauf.
 * Koordiniert Spieler-Aktionen, Gegner-KI, Level-Generierung und Spiellogik.
 */
public class Game {
    /** Breite der Spielkarte in Tiles */
    private static final int MAP_WIDTH = 80;

    /** Höhe der Spielkarte in Tiles */
    private static final int MAP_HEIGHT = 50;

    /** Sichtradius des Spielers in Tiles */
    private static final int FOV_RADIUS = 10;

    /** Die aktuelle Spielkarte */
    private GameMap gameMap;

    /** Der Spieler-Charakter */
    private Player player;

    /** Aktueller Spielzustand */
    private GameState gameState;

    /** Nachrichtenprotokoll für Spielereignisse */
    private MessageLog messageLog;

    /** Aktuelle Dungeon-Ebene (Schwierigkeitsgrad) */
    private int dungeonLevel;

    /**
     * Konstruktor - initialisiert ein neues Spiel.
     * Erstellt MessageLog und startet automatisch ein neues Spiel.
     */
    public Game() {
        this.messageLog = new MessageLog(100);
        this.dungeonLevel = 1;
        startNewGame();
    }

    /**
     * Startet ein komplett neues Spiel.
     * Setzt alle Werte zurück und generiert das erste Level.
     */
    public void startNewGame() {
        gameState = GameState.PLAYER_TURN;
        dungeonLevel = 1;
        messageLog.addMessage("Welcome to the dungeon! Press 'H' for help.", MessageLog.MessageType.IMPORTANT);

        generateLevel();
    }

    /**
     * Generiert ein neues Dungeon-Level.
     * Platziert den Spieler, spawnt Monster und Items, berechnet das initiale Sichtfeld.
     * Die Schwierigkeit steigt mit der Dungeon-Ebene.
     */
    private void generateLevel() {
        DungeonGenerator generator = new DungeonGenerator();
        gameMap = generator.generateDungeon(MAP_WIDTH, MAP_HEIGHT);

        // Platziere Spieler im ersten Raum
        Position startPos = gameMap.getRooms().get(0).getCenter();
        if (player == null) {
            // Erstelle neuen Spieler beim ersten Level
            player = new Player(startPos);
        } else {
            // Bewege existierenden Spieler und heile teilweise
            player.setPosition(startPos);
            player.heal(player.getMaxHp() / 2);
        }

        // Bevölkere Dungeon mit Gegnern und Items
        // Schwierigkeit steigt mit Level
        int maxMonstersPerRoom = 2 + dungeonLevel / 2;
        int maxItemsPerRoom = 2;
        gameMap.populateMonsters(maxMonstersPerRoom);
        gameMap.populateItems(maxItemsPerRoom);

        // Berechne initiales Sichtfeld
        FOVSystem.computeFOV(gameMap, player.getPosition(), FOV_RADIUS);

        messageLog.addMessage(String.format("You descend to dungeon level %d.", dungeonLevel),
                MessageLog.MessageType.IMPORTANT);
    }

    /**
     * Bewegt den Spieler in eine Richtung.
     * Prüft auf Kampf, blockierte Tiles, Treppen und Items.
     *
     * @param dx Bewegung in X-Richtung (-1, 0, oder 1)
     * @param dy Bewegung in Y-Richtung (-1, 0, oder 1)
     */
    public void movePlayer(int dx, int dy) {
        if (gameState != GameState.PLAYER_TURN) {
            return;
        }

        Position newPos = player.getPosition().add(dx, dy);

        // Prüfe auf Kampf mit Gegner
        Entity target = gameMap.getBlockingEntityAt(newPos);
        if (target != null && target.isAlive()) {
            CombatSystem.attack(player, target, messageLog);
            endPlayerTurn();
            return;
        }

        // Prüfe ob Feld begehbar ist
        if (!gameMap.isBlocked(newPos)) {
            // Bewege Spieler
            player.setPosition(newPos);
            // Aktualisiere Sichtfeld
            FOVSystem.computeFOV(gameMap, player.getPosition(), FOV_RADIUS);

            // Prüfe auf Treppen
            if (gameMap.getTile(newPos.x, newPos.y).getCharacter() == '>') {
                messageLog.addMessage("You see stairs leading down. Press 'R' to descend.",
                        MessageLog.MessageType.INFO);
            }

            // Prüfe auf Items
            Item item = gameMap.getItemAt(newPos);
            if (item != null) {
                messageLog.addMessage("You see " + item.getName() + " here. Press 'E' to pick it up.",
                        MessageLog.MessageType.INFO);
            }

            endPlayerTurn();
        }
    }

    /**
     * Hebt ein Item an der aktuellen Spielerposition auf.
     * Fügt das Item dem Inventar hinzu oder zeigt eine Fehlermeldung.
     */
    public void pickupItem() {
        if (gameState != GameState.PLAYER_TURN) {
            return;
        }

        Item item = gameMap.getItemAt(player.getPosition());
        if (item != null) {
            if (player.getInventory().addItem(item)) {
                gameMap.removeItem(item);
                messageLog.addMessage("You picked up " + item.getName() + "!", MessageLog.MessageType.INFO);
            } else {
                messageLog.addMessage("Your inventory is full!", MessageLog.MessageType.IMPORTANT);
            }
        } else {
            messageLog.addMessage("There is nothing to pick up here.", MessageLog.MessageType.INFO);
        }
    }

    /**
     * Benutzt ein Item aus dem Inventar.
     * Je nach Item-Typ: Heilen, Waffe ausrüsten oder Rüstung ausrüsten.
     *
     * @param index Index des Items im Inventar (0-basiert)
     */
    public void useItem(int index) {
        if (gameState != GameState.PLAYER_TURN) {
            return;
        }

        Item item = player.getInventory().getItem(index);
        if (item != null) {
            switch (item.getType()) {
                case HEALING:
                    // Heiltrank: Sofortige Heilung
                    player.heal(item.getValue());
                    messageLog.addMessage(String.format("You use %s and heal %d HP!",
                            item.getName(), item.getValue()), MessageLog.MessageType.INFO);
                    player.getInventory().removeItem(item);
                    endPlayerTurn();
                    break;
                case WEAPON:
                    // Waffe: Rüste aus und erhöhe Angriff
                    player.getInventory().removeItem(item);
                    if (player.equipItem(item)) {
                        messageLog.addMessage(String.format("You equip the %s! (+%d ATK)",
                                item.getName(), item.getValue()), MessageLog.MessageType.INFO);
                        endPlayerTurn();
                    } else {
                        // Fehler beim Ausrüsten - Item zurück ins Inventar
                        player.getInventory().addItem(item);
                        messageLog.addMessage("Failed to equip weapon!", MessageLog.MessageType.IMPORTANT);
                    }
                    break;
                case ARMOR:
                    // Rüstung: Rüste aus und erhöhe Verteidigung
                    player.getInventory().removeItem(item);
                    if (player.equipItem(item)) {
                        messageLog.addMessage(String.format("You equip the %s! (+%d DEF)",
                                item.getName(), item.getValue()), MessageLog.MessageType.INFO);
                        endPlayerTurn();
                    } else {
                        // Fehler beim Ausrüsten - Item zurück ins Inventar
                        player.getInventory().addItem(item);
                        messageLog.addMessage("Failed to equip armor!", MessageLog.MessageType.IMPORTANT);
                    }
                    break;
            }
        }
    }

    /**
     * Steigt Treppen hinab zum nächsten Level.
     * Erhöht die Schwierigkeit und generiert ein neues Level.
     */
    public void descendStairs() {
        if (gameState != GameState.PLAYER_TURN) {
            return;
        }

        Position pos = player.getPosition();
        if (gameMap.getTile(pos.x, pos.y).getCharacter() == '>') {
            dungeonLevel++;
            generateLevel();
        } else {
            messageLog.addMessage("There are no stairs here.", MessageLog.MessageType.INFO);
        }
    }

    /**
     * Beendet den Spielerzug und startet den Gegnerzug.
     * Prüft nach dem Gegnerzug, ob der Spieler noch lebt.
     */
    private void endPlayerTurn() {
        gameState = GameState.ENEMY_TURN;
        enemyTurn();
        gameState = GameState.PLAYER_TURN;

        // Prüfe ob Spieler gestorben ist
        if (!player.isAlive()) {
            gameState = GameState.GAME_OVER;
            messageLog.addMessage("You died! Press 'r' to restart.", MessageLog.MessageType.IMPORTANT);
        }
    }

    /**
     * Führt die KI-Aktionen für alle Gegner aus.
     * Gegner greifen an oder bewegen sich zum Spieler, wenn sie ihn sehen können.
     * Verwendet einfaches Pathfinding basierend auf direkter Linie.
     */
    private void enemyTurn() {
        // Erstelle Kopie der Entity-Liste um ConcurrentModificationException zu vermeiden
        List<Entity> entities = new ArrayList<>(gameMap.getEntities());

        for (Entity entity : entities) {
            if (!entity.isAlive()) {
                continue;
            }

            if (entity instanceof Enemy) {
                Enemy enemy = (Enemy) entity;
                Position enemyPos = enemy.getPosition();

                // Prüfe ob Gegner den Spieler sehen kann (FOV)
                if (gameMap.isVisible(enemyPos.x, enemyPos.y)) {
                    double distance = enemyPos.distanceTo(player.getPosition());

                    if (distance <= 1.5) {
                        // Spieler ist in Reichweite - greife an
                        CombatSystem.attack(enemy, player, messageLog);
                    } else {
                        // Bewege dich zum Spieler - einfaches Pathfinding
                        // Berechne Richtung zum Spieler
                        int dx = Integer.compare(player.getPosition().x, enemyPos.x);
                        int dy = Integer.compare(player.getPosition().y, enemyPos.y);

                        // Versuche diagonale Bewegung
                        Position newPos = enemyPos.add(dx, dy);
                        if (!gameMap.isBlocked(newPos)) {
                            enemy.setPosition(newPos);
                        } else if (dx != 0) {
                            // Diagonal blockiert, versuche horizontale Bewegung
                            newPos = enemyPos.add(dx, 0);
                            if (!gameMap.isBlocked(newPos)) {
                                enemy.setPosition(newPos);
                            }
                        } else if (dy != 0) {
                            // Horizontal blockiert, versuche vertikale Bewegung
                            newPos = enemyPos.add(0, dy);
                            if (!gameMap.isBlocked(newPos)) {
                                enemy.setPosition(newPos);
                            }
                        }
                    }
                }
            }
        }

        // Entferne tote Gegner von der Karte
        gameMap.removeDeadEntities();
    }

    // Getter-Methoden

    public GameMap getGameMap() {
        return gameMap;
    }

    public Player getPlayer() {
        return player;
    }

    public GameState getGameState() {
        return gameState;
    }

    public MessageLog getMessageLog() {
        return messageLog;
    }

    public int getDungeonLevel() {
        return dungeonLevel;
    }
}
