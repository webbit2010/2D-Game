package com.roguelike.item;

import com.roguelike.engine.Position;
import java.awt.Color;

public class Item {
    private Position position;
    private char character;
    private Color color;
    private String name;
    private ItemType type;
    private int value;

    public Item(Position position, char character, Color color, String name, ItemType type, int value) {
        this.position = position;
        this.character = character;
        this.color = color;
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public char getCharacter() {
        return character;
    }

    public Color getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public ItemType getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    // Factory methods
    public static Item createHealthPotion(Position position) {
        return new Item(position, '!', new Color(127, 0, 255), "Health Potion", ItemType.HEALING, 20);
    }

    public static Item createSword(Position position) {
        return new Item(position, '/', new Color(0, 191, 255), "Sword", ItemType.WEAPON, 3);
    }

    public static Item createShield(Position position) {
        return new Item(position, '[', new Color(139, 69, 19), "Shield", ItemType.ARMOR, 2);
    }

    public enum ItemType {
        HEALING,
        WEAPON,
        ARMOR
    }
}
