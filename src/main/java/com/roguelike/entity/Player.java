package com.roguelike.entity;

import com.roguelike.engine.Position;
import com.roguelike.item.Inventory;
import java.awt.Color;

public class Player extends Entity {
    private Inventory inventory;
    private int level;
    private int experience;
    private int experienceToLevel;

    public Player(Position position) {
        super(position, '@', Color.YELLOW, "Player", 30, 5, 2, true);
        this.inventory = new Inventory(20);
        this.level = 1;
        this.experience = 0;
        this.experienceToLevel = 100;
    }

    @Override
    public void update() {
        // Player is controlled by input, not AI
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public void gainExperience(int amount) {
        experience += amount;
        if (experience >= experienceToLevel) {
            levelUp();
        }
    }

    private void levelUp() {
        level++;
        experience -= experienceToLevel;
        experienceToLevel = (int) (experienceToLevel * 1.5);

        maxHp += 5;
        hp = maxHp;
        attack += 1;
        defense += 1;
    }

    public int getExperienceToLevel() {
        return experienceToLevel;
    }
}
