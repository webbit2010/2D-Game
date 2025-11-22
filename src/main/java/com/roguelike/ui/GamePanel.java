package com.roguelike.ui;

import com.roguelike.engine.Game;
import com.roguelike.engine.Position;
import com.roguelike.entity.Entity;
import com.roguelike.entity.Player;
import com.roguelike.item.Item;
import com.roguelike.system.MessageLog;
import com.roguelike.world.GameMap;
import com.roguelike.world.Tile;

import javax.swing.*;

import java.awt.*;
import java.util.List;

public class GamePanel extends JPanel {
    private static final int TILE_SIZE = 12;
    private static final int MAP_VIEW_WIDTH = 80;
    private static final int MAP_VIEW_HEIGHT = 45;
    private static final int SIDEBAR_WIDTH = 30;
    private static final int MESSAGE_HEIGHT = 10;

    private final Game game;
    private final Font gameFont;

    public GamePanel(Game game) {
        this.game = game;
        this.gameFont = new Font("Courier New", Font.PLAIN, TILE_SIZE);

        int width = (MAP_VIEW_WIDTH + SIDEBAR_WIDTH) * TILE_SIZE;
        int height = (MAP_VIEW_HEIGHT + MESSAGE_HEIGHT) * TILE_SIZE;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(gameFont);

        if (null == game.getGameState()) {
            drawGame(g2d);
        } else switch (game.getGameState()) {
            case MAIN_MENU:
                drawMainMenu(g2d);
                break;
            case GAME_OVER:
                drawGame(g2d);
                drawGameOver(g2d);
                break;
            default:
                drawGame(g2d);
                break;
        }
    }

    private void drawMainMenu(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Courier New", Font.BOLD, 24));

        String title = "ROGUELIKE DUNGEON";
        int titleWidth = g.getFontMetrics().stringWidth(title);
        g.drawString(title, (getWidth() - titleWidth) / 2, getHeight() / 2 - 50);

        g.setFont(gameFont);
        String start = "Press ENTER to start";
        int startWidth = g.getFontMetrics().stringWidth(start);
        g.drawString(start, (getWidth() - startWidth) / 2, getHeight() / 2 + 20);
    }

    private void drawGameOver(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.RED);
        g.setFont(new Font("Courier New", Font.BOLD, 24));

        String gameOver = "GAME OVER";
        int width = g.getFontMetrics().stringWidth(gameOver);
        g.drawString(gameOver, (getWidth() - width) / 2, getHeight() / 2 - 50);

        g.setFont(gameFont);
        g.setColor(Color.WHITE);
        String restart = "Press 'r' to restart";
        int restartWidth = g.getFontMetrics().stringWidth(restart);
        g.drawString(restart, (getWidth() - restartWidth) / 2, getHeight() / 2 + 20);
    }

    private void drawGame(Graphics2D g) {
        drawMap(g);
        drawEntities(g);
        drawItems(g);
        drawSidebar(g);
        drawMessages(g);
    }

    private void drawMap(Graphics2D g) {
        GameMap map = game.getGameMap();
        Player player = game.getPlayer();

        int startX = Math.max(0, player.getPosition().x - MAP_VIEW_WIDTH / 2);
        int startY = Math.max(0, player.getPosition().y - MAP_VIEW_HEIGHT / 2);
        int endX = Math.min(map.getWidth(), startX + MAP_VIEW_WIDTH);
        int endY = Math.min(map.getHeight(), startY + MAP_VIEW_HEIGHT);

        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                int screenX = (x - startX) * TILE_SIZE;
                int screenY = (y - startY) * TILE_SIZE;

                if (map.isVisible(x, y)) {
                    Tile tile = map.getTile(x, y);
                    g.setColor(tile.getColor());
                    g.drawString(String.valueOf(tile.getCharacter()), screenX, screenY + TILE_SIZE);
                } else if (map.isExplored(x, y)) {
                    Tile tile = map.getTile(x, y);
                    Color darkColor = tile.getColor().darker().darker();
                    g.setColor(darkColor);
                    g.drawString(String.valueOf(tile.getCharacter()), screenX, screenY + TILE_SIZE);
                }
            }
        }
    }

    private void drawEntities(Graphics2D g) {
        GameMap map = game.getGameMap();
        Player player = game.getPlayer();

        int startX = Math.max(0, player.getPosition().x - MAP_VIEW_WIDTH / 2);
        int startY = Math.max(0, player.getPosition().y - MAP_VIEW_HEIGHT / 2);

        for (Entity entity : map.getEntities()) {
            Position pos = entity.getPosition();
            if (map.isVisible(pos.x, pos.y)) {
                int screenX = (pos.x - startX) * TILE_SIZE;
                int screenY = (pos.y - startY) * TILE_SIZE;

                g.setColor(entity.getColor());
                g.drawString(String.valueOf(entity.getCharacter()), screenX, screenY + TILE_SIZE);
            }
        }

        // Draw player
        Position pos = player.getPosition();
        int screenX = (pos.x - startX) * TILE_SIZE;
        int screenY = (pos.y - startY) * TILE_SIZE;
        g.setColor(player.getColor());
        g.drawString(String.valueOf(player.getCharacter()), screenX, screenY + TILE_SIZE);
    }

    private void drawItems(Graphics2D g) {
        GameMap map = game.getGameMap();
        Player player = game.getPlayer();

        int startX = Math.max(0, player.getPosition().x - MAP_VIEW_WIDTH / 2);
        int startY = Math.max(0, player.getPosition().y - MAP_VIEW_HEIGHT / 2);

        for (Item item : map.getItems()) {
            Position pos = item.getPosition();
            if (map.isVisible(pos.x, pos.y)) {
                int screenX = (pos.x - startX) * TILE_SIZE;
                int screenY = (pos.y - startY) * TILE_SIZE;

                g.setColor(item.getColor());
                g.drawString(String.valueOf(item.getCharacter()), screenX, screenY + TILE_SIZE);
            }
        }
    }

    private void drawSidebar(Graphics2D g) {
        int sidebarX = MAP_VIEW_WIDTH * TILE_SIZE;
        int y = TILE_SIZE;

        Player player = game.getPlayer();

        g.setColor(Color.WHITE);
        g.drawString("=== STATUS ===", sidebarX, y);
        y += TILE_SIZE * 2;

        g.drawString("Level: " + player.getLevel(), sidebarX, y);
        y += TILE_SIZE;

        g.drawString("Dungeon: " + game.getDungeonLevel(), sidebarX, y);
        y += TILE_SIZE * 2;

        // HP bar
        g.setColor(Color.RED);
        g.drawString("HP: " + player.getHp() + "/" + player.getMaxHp(), sidebarX, y);
        y += TILE_SIZE;

        int barWidth = (SIDEBAR_WIDTH - 2) * TILE_SIZE / 2;
        int barHeight = TILE_SIZE / 2;
        g.setColor(Color.DARK_GRAY);
        g.fillRect(sidebarX, y - barHeight, barWidth, barHeight);

        int hpWidth = (int) ((double) player.getHp() / player.getMaxHp() * barWidth);
        g.setColor(Color.RED);
        g.fillRect(sidebarX, y - barHeight, hpWidth, barHeight);
        y += TILE_SIZE * 2;

        // Stats
        g.setColor(Color.WHITE);
        g.drawString("ATK: " + player.getAttack(), sidebarX, y);
        y += TILE_SIZE;
        g.drawString("DEF: " + player.getDefense(), sidebarX, y);
        y += TILE_SIZE * 2;

        // Experience
        g.drawString("EXP: " + player.getExperience() + "/" +
                player.getExperienceToLevel(), sidebarX, y);
        y += TILE_SIZE * 2;

        // Equipment
        g.drawString("=== EQUIPMENT ===", sidebarX, y);
        y += TILE_SIZE;

        Item weapon = player.getEquippedWeapon();
        if (weapon != null) {
            g.setColor(weapon.getColor());
            g.drawString("Weapon: " + weapon.getName(), sidebarX, y);
        } else {
            g.setColor(Color.GRAY);
            g.drawString("Weapon: None", sidebarX, y);
        }
        y += TILE_SIZE;

        Item armor = player.getEquippedArmor();
        if (armor != null) {
            g.setColor(armor.getColor());
            g.drawString("Armor: " + armor.getName(), sidebarX, y);
        } else {
            g.setColor(Color.GRAY);
            g.drawString("Armor: None", sidebarX, y);
        }
        y += TILE_SIZE * 2;

        // Inventory
        g.setColor(Color.WHITE);
        g.drawString("=== INVENTORY ===", sidebarX, y);
        y += TILE_SIZE;

        List<Item> items = player.getInventory().getItems();
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            g.setColor(item.getColor());
            g.drawString((i + 1) + ". " + item.getName(), sidebarX, y);
            y += TILE_SIZE;
        }

        if (items.isEmpty()) {
            g.setColor(Color.GRAY);
            g.drawString("(empty)", sidebarX, y);
        }
    }

    private void drawMessages(Graphics2D g) {
        int messageY = MAP_VIEW_HEIGHT * TILE_SIZE + TILE_SIZE;
        int x = TILE_SIZE;

        g.setColor(Color.GRAY);
        g.drawString("=== MESSAGES ===", x, messageY);
        messageY += TILE_SIZE;

        List<MessageLog.Message> messages = game.getMessageLog().getMessages();
        int start = Math.max(0, messages.size() - 8);

        for (int i = start; i < messages.size(); i++) {
            MessageLog.Message msg = messages.get(i);
            g.setColor(msg.color);
            g.drawString(msg.text, x, messageY);
            messageY += TILE_SIZE;
        }
    }
}
