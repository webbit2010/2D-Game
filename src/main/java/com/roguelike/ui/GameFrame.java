package com.roguelike.ui;

import com.roguelike.engine.Game;
import com.roguelike.engine.GameState;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameFrame extends JFrame {
    private Game game;
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
            // Movement - Arrow keys
            case KeyEvent.VK_UP:
            case KeyEvent.VK_KP_UP:
                game.movePlayer(0, -1);
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_KP_DOWN:
                game.movePlayer(0, 1);
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_KP_LEFT:
                game.movePlayer(-1, 0);
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_KP_RIGHT:
                game.movePlayer(1, 0);
                break;

            // Movement - Numpad
            case KeyEvent.VK_NUMPAD7:
                game.movePlayer(-1, -1);
                break;
            case KeyEvent.VK_NUMPAD8:
                game.movePlayer(0, -1);
                break;
            case KeyEvent.VK_NUMPAD9:
                game.movePlayer(1, -1);
                break;
            case KeyEvent.VK_NUMPAD4:
                game.movePlayer(-1, 0);
                break;
            case KeyEvent.VK_NUMPAD6:
                game.movePlayer(1, 0);
                break;
            case KeyEvent.VK_NUMPAD1:
                game.movePlayer(-1, 1);
                break;
            case KeyEvent.VK_NUMPAD2:
                game.movePlayer(0, 1);
                break;
            case KeyEvent.VK_NUMPAD3:
                game.movePlayer(1, 1);
                break;

            // Movement - Vi keys
            case KeyEvent.VK_H:
                game.movePlayer(-1, 0);
                break;
            case KeyEvent.VK_J:
                game.movePlayer(0, 1);
                break;
            case KeyEvent.VK_K:
                game.movePlayer(0, -1);
                break;
            case KeyEvent.VK_L:
                game.movePlayer(1, 0);
                break;
            case KeyEvent.VK_Y:
                game.movePlayer(-1, -1);
                break;
            case KeyEvent.VK_U:
                game.movePlayer(1, -1);
                break;
            case KeyEvent.VK_B:
                game.movePlayer(-1, 1);
                break;
            case KeyEvent.VK_N:
                game.movePlayer(1, 1);
                break;

            // Actions
            case KeyEvent.VK_G:
                game.pickupItem();
                break;

            case KeyEvent.VK_PERIOD:
                if (e.isShiftDown()) { // Shift+. = >
                    game.descendStairs();
                }
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

            // Help
            case KeyEvent.VK_SLASH:
                if (e.isShiftDown()) { // Shift+/ = ?
                    showHelp();
                }
                break;
        }
    }

    private void showHelp() {
        String help = "=== CONTROLS ===\n\n" +
                "Movement:\n" +
                "  Arrow keys, Numpad, or Vi keys (hjkl/yubn)\n\n" +
                "Actions:\n" +
                "  g - Pick up item\n" +
                "  1-9 - Use item from inventory\n" +
                "  > - Descend stairs\n" +
                "  ? - Show this help\n\n" +
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
