package com.roguelike.entity;

import com.roguelike.engine.Position;
import com.roguelike.item.Inventory;
import com.roguelike.item.Item;
import java.awt.Color;

/**
 * Spieler-Charakter mit Inventar, Level-System und Equipment.
 * Erbt von Entity und fügt Erfahrung, Leveling und Ausrüstung hinzu.
 */
public class Player extends Entity {
    /** Inventar des Spielers */
    private Inventory inventory;

    /** Aktuelles Level des Spielers */
    private int level;

    /** Aktuelle Erfahrungspunkte */
    private int experience;

    /** Erfahrungspunkte bis zum nächsten Level */
    private int experienceToLevel;

    /** Ausgerüstete Waffe (kann null sein) */
    private Item equippedWeapon;

    /** Ausgerüstete Rüstung (kann null sein) */
    private Item equippedArmor;

    /** Basis-Angriffswert ohne Equipment */
    private int baseAttack;

    /** Basis-Verteidigungswert ohne Equipment */
    private int baseDefense;

    /**
     * Konstruktor - erstellt einen neuen Spieler.
     * Startwerte: Level 1, 30 HP, 5 ATK, 2 DEF
     *
     * @param position Startposition auf der Karte
     */
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

    /**
     * Update-Methode - wird nicht verwendet, da der Spieler durch Input gesteuert wird.
     */
    @Override
    public void update() {
        // Spieler wird durch Tastatureingaben gesteuert, nicht durch KI
    }

    // Getter-Methoden

    public Inventory getInventory() {
        return inventory;
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    /**
     * Erhöht die Erfahrung des Spielers.
     * Löst automatisch einen Level-Up aus, wenn genug Erfahrung gesammelt wurde.
     *
     * @param amount Menge der hinzuzufügenden Erfahrung
     */
    public void gainExperience(int amount) {
        experience += amount;
        if (experience >= experienceToLevel) {
            levelUp();
        }
    }

    /**
     * Erhöht das Level des Spielers.
     * Verbessert alle Stats und heilt den Spieler vollständig.
     * Erfahrungsanforderung steigt um 50% pro Level.
     */
    private void levelUp() {
        level++;
        experience -= experienceToLevel;
        experienceToLevel = (int) (experienceToLevel * 1.5);

        // Erhöhe Stats
        maxHp += 5;
        hp = maxHp; // Volle Heilung beim Level-Up
        baseAttack += 1;
        baseDefense += 1;
        updateStats(); // Aktualisiere Gesamt-Stats mit Equipment
    }

    public int getExperienceToLevel() {
        return experienceToLevel;
    }

    // Equipment-Methoden

    /**
     * Rüstet ein Item aus.
     * Tauscht automatisch vorhandenes Equipment des gleichen Typs aus.
     *
     * @param item Das auszurüstende Item
     * @return true wenn erfolgreich, false bei Fehler
     */
    public boolean equipItem(Item item) {
        if (item == null) return false;

        if (item.getType() == Item.ItemType.WEAPON) {
            // Tausche alte Waffe gegen neue
            if (equippedWeapon != null) {
                inventory.addItem(equippedWeapon);
            }
            equippedWeapon = item;
            updateStats();
            return true;
        } else if (item.getType() == Item.ItemType.ARMOR) {
            // Tausche alte Rüstung gegen neue
            if (equippedArmor != null) {
                inventory.addItem(equippedArmor);
            }
            equippedArmor = item;
            updateStats();
            return true;
        }
        return false;
    }

    /**
     * Legt die Waffe ab und legt sie ins Inventar.
     *
     * @return true wenn erfolgreich, false wenn Inventar voll
     */
    public boolean unequipWeapon() {
        if (equippedWeapon != null && !inventory.isFull()) {
            inventory.addItem(equippedWeapon);
            equippedWeapon = null;
            updateStats();
            return true;
        }
        return false;
    }

    /**
     * Legt die Rüstung ab und legt sie ins Inventar.
     *
     * @return true wenn erfolgreich, false wenn Inventar voll
     */
    public boolean unequipArmor() {
        if (equippedArmor != null && !inventory.isFull()) {
            inventory.addItem(equippedArmor);
            equippedArmor = null;
            updateStats();
            return true;
        }
        return false;
    }

    /**
     * Aktualisiert die Gesamt-Stats basierend auf Basis-Werten und Equipment.
     * Wird automatisch aufgerufen beim Ausrüsten/Ablegen von Items.
     */
    private void updateStats() {
        attack = baseAttack;
        defense = baseDefense;

        // Addiere Equipment-Boni
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
