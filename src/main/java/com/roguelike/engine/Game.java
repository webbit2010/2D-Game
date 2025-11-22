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

public class Game {
    private static final int MAP_WIDTH = 80;
    private static final int MAP_HEIGHT = 50;
    private static final int FOV_RADIUS = 10;

    private GameMap gameMap;
    private Player player;
    private GameState gameState;
    private MessageLog messageLog;
    private int dungeonLevel;

    public Game() {
        this.messageLog = new MessageLog(100);
        this.dungeonLevel = 1;
        startNewGame();
    }

    public void startNewGame() {
        gameState = GameState.PLAYER_TURN;
        dungeonLevel = 1;
        messageLog.addMessage("Welcome to the dungeon! Press 'H' for help.", MessageLog.MessageType.IMPORTANT);

        generateLevel();
    }

    private void generateLevel() {
        DungeonGenerator generator = new DungeonGenerator();
        gameMap = generator.generateDungeon(MAP_WIDTH, MAP_HEIGHT);

        // Place player in first room
        Position startPos = gameMap.getRooms().get(0).getCenter();
        if (player == null) {
            player = new Player(startPos);
        } else {
            player.setPosition(startPos);
            player.heal(player.getMaxHp() / 2); // Heal half health when descending
        }

        // Populate dungeon
        int maxMonstersPerRoom = 2 + dungeonLevel / 2;
        int maxItemsPerRoom = 2;
        gameMap.populateMonsters(maxMonstersPerRoom);
        gameMap.populateItems(maxItemsPerRoom);

        // Compute initial FOV
        FOVSystem.computeFOV(gameMap, player.getPosition(), FOV_RADIUS);

        messageLog.addMessage(String.format("You descend to dungeon level %d.", dungeonLevel),
                MessageLog.MessageType.IMPORTANT);
    }

    public void movePlayer(int dx, int dy) {
        if (gameState != GameState.PLAYER_TURN) {
            return;
        }

        Position newPos = player.getPosition().add(dx, dy);

        // Check for combat
        Entity target = gameMap.getBlockingEntityAt(newPos);
        if (target != null && target.isAlive()) {
            CombatSystem.attack(player, target, messageLog);
            endPlayerTurn();
            return;
        }

        // Check if blocked
        if (!gameMap.isBlocked(newPos)) {
            player.setPosition(newPos);
            FOVSystem.computeFOV(gameMap, player.getPosition(), FOV_RADIUS);

            // Check for stairs
            if (gameMap.getTile(newPos.x, newPos.y).getCharacter() == '>') {
                messageLog.addMessage("You see stairs leading down. Press 'R' to descend.",
                        MessageLog.MessageType.INFO);
            }

            // Check for items
            Item item = gameMap.getItemAt(newPos);
            if (item != null) {
                messageLog.addMessage("You see " + item.getName() + " here. Press 'E' to pick it up.",
                        MessageLog.MessageType.INFO);
            }

            endPlayerTurn();
        }
    }

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

    public void useItem(int index) {
        if (gameState != GameState.PLAYER_TURN) {
            return;
        }

        Item item = player.getInventory().getItem(index);
        if (item != null) {
            switch (item.getType()) {
                case HEALING:
                    player.heal(item.getValue());
                    messageLog.addMessage(String.format("You use %s and heal %d HP!",
                            item.getName(), item.getValue()), MessageLog.MessageType.INFO);
                    player.getInventory().removeItem(item);
                    endPlayerTurn();
                    break;
                case WEAPON:
                    messageLog.addMessage("You can't use that yet!", MessageLog.MessageType.INFO);
                    break;
                case ARMOR:
                    messageLog.addMessage("You can't use that yet!", MessageLog.MessageType.INFO);
                    break;
            }
        }
    }

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

    private void endPlayerTurn() {
        gameState = GameState.ENEMY_TURN;
        enemyTurn();
        gameState = GameState.PLAYER_TURN;

        // Check if player died
        if (!player.isAlive()) {
            gameState = GameState.GAME_OVER;
            messageLog.addMessage("You died! Press 'r' to restart.", MessageLog.MessageType.IMPORTANT);
        }
    }

    private void enemyTurn() {
        List<Entity> entities = new ArrayList<>(gameMap.getEntities());

        for (Entity entity : entities) {
            if (!entity.isAlive()) {
                continue;
            }

            if (entity instanceof Enemy) {
                Enemy enemy = (Enemy) entity;
                Position enemyPos = enemy.getPosition();

                // Check if enemy can see player
                if (gameMap.isVisible(enemyPos.x, enemyPos.y)) {
                    double distance = enemyPos.distanceTo(player.getPosition());

                    if (distance <= 1.5) {
                        // Attack player
                        CombatSystem.attack(enemy, player, messageLog);
                    } else {
                        // Move towards player using simple pathfinding
                        int dx = Integer.compare(player.getPosition().x, enemyPos.x);
                        int dy = Integer.compare(player.getPosition().y, enemyPos.y);

                        Position newPos = enemyPos.add(dx, dy);
                        if (!gameMap.isBlocked(newPos)) {
                            enemy.setPosition(newPos);
                        } else if (dx != 0) {
                            newPos = enemyPos.add(dx, 0);
                            if (!gameMap.isBlocked(newPos)) {
                                enemy.setPosition(newPos);
                            }
                        } else if (dy != 0) {
                            newPos = enemyPos.add(0, dy);
                            if (!gameMap.isBlocked(newPos)) {
                                enemy.setPosition(newPos);
                            }
                        }
                    }
                }
            }
        }

        gameMap.removeDeadEntities();
    }

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
