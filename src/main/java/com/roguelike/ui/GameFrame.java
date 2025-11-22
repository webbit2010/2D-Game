package com.roguelike.ui;

import com.roguelike.engine.Game;
import com.roguelike.engine.GameState;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameFrame extends JFrame {
    private final Game game;
    private GamePanel gamePanel;

    public GameFrame() {
        game = new Game();
        gamePanel = new GamePanel(game);

        setTitle("Roguelike Dungeon Crawler");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        add(gamePanel);
        pack();

        setLocationRelativeTo(null);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleInput(e);
                gamePanel.repaint();
            }
        });

        setVisible(true);
    }

    private void handleInput(KeyEvent e) {
        GameState state = game.getGameState();

        if (state == GameState.MAIN_MENU) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                game.startNewGame();
            }
            return;
        }

        if (state == GameState.GAME_OVER) {
            if (e.getKeyCode() == KeyEvent.VK_R) {
                game.startNewGame();
            }
            return;
        }

        if (state != GameState.PLAYER_TURN) {
            return;
        }

        switch (e.getKeyCode()) {
            // Movement - WASD
            case KeyEvent.VK_W:
                game.movePlayer(0, -1);
                break;
            case KeyEvent.VK_A:
                game.movePlayer(-1, 0);
                break;
            case KeyEvent.VK_S:
                game.movePlayer(0, 1);
                break;
            case KeyEvent.VK_D:
                game.movePlayer(1, 0);
                break;

            // Movement - Arrow keys (alternative)
            case KeyEvent.VK_UP:
                game.movePlayer(0, -1);
                break;
            case KeyEvent.VK_DOWN:
                game.movePlayer(0, 1);
                break;
            case KeyEvent.VK_LEFT:
                game.movePlayer(-1, 0);
                break;
            case KeyEvent.VK_RIGHT:
                game.movePlayer(1, 0);
                break;

            // Actions
            case KeyEvent.VK_E:
                game.pickupItem();
                break;

            case KeyEvent.VK_R:
                game.descendStairs();
                break;

            case KeyEvent.VK_H:
                showHelp();
                break;

            // Inventory usage (1-9)
            case KeyEvent.VK_1:
                game.useItem(0);
                break;
            case KeyEvent.VK_2:
                game.useItem(1);
                break;
            case KeyEvent.VK_3:
                game.useItem(2);
                break;
            case KeyEvent.VK_4:
                game.useItem(3);
                break;
            case KeyEvent.VK_5:
                game.useItem(4);
                break;
            case KeyEvent.VK_6:
                game.useItem(5);
                break;
            case KeyEvent.VK_7:
                game.useItem(6);
                break;
            case KeyEvent.VK_8:
                game.useItem(7);
                break;
            case KeyEvent.VK_9:
                game.useItem(8);
                break;
        }
    }

    private void showHelp() {
        String help = "=== CONTROLS ===\n\n" +
                "Movement:\n" +
                "  W - Move up\n" +
                "  A - Move left\n" +
                "  S - Move down\n" +
                "  D - Move right\n" +
                "  (Arrow keys also work)\n\n" +
                "Actions:\n" +
                "  E - Pick up item\n" +
                "  R - Descend stairs\n" +
                "  H - Show this help\n" +
                "  1-9 - Use item from inventory\n\n" +
                "Goal:\n" +
                "  Explore the dungeon, defeat monsters,\n" +
                "  collect items, and descend deeper!\n\n" +
                "Features:\n" +
                "  - Procedurally generated dungeons\n" +
                "  - Turn-based combat\n" +
                "  - Character progression\n" +
                "  - Field of view\n" +
                "  - Permadeath!";

        JOptionPane.showMessageDialog(this, help, "Help", JOptionPane.INFORMATION_MESSAGE);
    }
}
