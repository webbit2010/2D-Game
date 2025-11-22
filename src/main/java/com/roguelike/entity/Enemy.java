package com.roguelike.entity;

import com.roguelike.engine.Position;
import com.roguelike.world.GameMap;
import java.awt.Color;
import java.util.Random;

public class Enemy extends Entity {
    private static final Random random = new Random();
    private int expValue;
    private AI ai;

    public Enemy(Position position, String name, char character, Color color,
                 int maxHp, int attack, int defense, int expValue, AI ai) {
        super(position, character, color, name, maxHp, attack, defense, true);
        this.expValue = expValue;
        this.ai = ai;
    }

    public int getExpValue() {
        return expValue;
    }

    @Override
    public void update() {
        if (ai != null && alive) {
            ai.update(this);
        }
    }

    public void setAI(AI ai) {
        this.ai = ai;
    }

    public AI getAI() {
        return ai;
    }

    // Factory methods for different enemy types
    public static Enemy createOrc(Position position) {
        return new Enemy(position, "Orc", 'o', new Color(0, 127, 0),
                        10, 3, 0, 35, new BasicAI());
    }

    public static Enemy createTroll(Position position) {
        return new Enemy(position, "Troll", 'T', new Color(0, 127, 0).darker(),
                        16, 4, 1, 100, new BasicAI());
    }

    public static Enemy createGoblin(Position position) {
        return new Enemy(position, "Goblin", 'g', new Color(127, 127, 0),
                        6, 2, 0, 20, new BasicAI());
    }

    public static Enemy createRandomEnemy(Position position) {
        int type = random.nextInt(3);
        switch (type) {
            case 0: return createGoblin(position);
            case 1: return createOrc(position);
            case 2: return createTroll(position);
            default: return createOrc(position);
        }
    }

    // AI interface
    public interface AI {
        void update(Enemy enemy);
    }

    // Basic chase AI
    public static class BasicAI implements AI {
        @Override
        public void update(Enemy enemy) {
            // AI logic will be implemented in the game loop
            // This is called from the Game class which has access to the player
        }
    }
}
