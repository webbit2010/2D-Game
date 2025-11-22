package com.roguelike.entity;

import com.roguelike.engine.Position;
import com.roguelike.item.Inventory;
import com.roguelike.item.Item;
import java.awt.Color;

public class Player extends Entity {
    private Inventory inventory;
    private int level;
    private int experience;
    private int experienceToLevel;

    // Equipment slots
    private Item equippedWeapon;
    private Item equippedArmor;

    // Base stats (without equipment)
    private int baseAttack;
    private int baseDefense;

    public Player(Position position) {
        super(position, '@', Color.YELLOW, "Player", 30, 5, 2, true);
        this.inventory = new Inventory(20);
        this.level = 1;
        this.experience = 0;
        this.experienceToLevel = 100;
        this.baseAttack = 5;
        this.baseDefense = 2;
        this.equippedWeapon = null;
        this.equippedArmor = null;
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
        baseAttack += 1;
        baseDefense += 1;
        updateStats();
    }

    public int getExperienceToLevel() {
        return experienceToLevel;
    }

    // Equipment methods
    public boolean equipItem(Item item) {
        if (item == null) return false;

        if (item.getType() == Item.ItemType.WEAPON) {
            if (equippedWeapon != null) {
                inventory.addItem(equippedWeapon);
            }
            equippedWeapon = item;
            updateStats();
            return true;
        } else if (item.getType() == Item.ItemType.ARMOR) {
            if (equippedArmor != null) {
                inventory.addItem(equippedArmor);
            }
            equippedArmor = item;
            updateStats();
            return true;
        }
        return false;
    }

    public boolean unequipWeapon() {
        if (equippedWeapon != null && !inventory.isFull()) {
            inventory.addItem(equippedWeapon);
            equippedWeapon = null;
            updateStats();
            return true;
        }
        return false;
    }

    public boolean unequipArmor() {
        if (equippedArmor != null && !inventory.isFull()) {
            inventory.addItem(equippedArmor);
            equippedArmor = null;
            updateStats();
            return true;
        }
        return false;
    }

    private void updateStats() {
        attack = baseAttack;
        defense = baseDefense;

        if (equippedWeapon != null) {
            attack += equippedWeapon.getValue();
        }
        if (equippedArmor != null) {
            defense += equippedArmor.getValue();
        }
    }

    public Item getEquippedWeapon() {
        return equippedWeapon;
    }

    public Item getEquippedArmor() {
        return equippedArmor;
    }

    public int getBaseAttack() {
        return baseAttack;
    }

    public int getBaseDefense() {
        return baseDefense;
    }
}
