package com.roguelike.entity;

import com.roguelike.engine.Position;
import java.awt.Color;

public abstract class Entity {
    protected Position position;
    protected char character;
    protected Color color;
    protected String name;
    protected int maxHp;
    protected int hp;
    protected int attack;
    protected int defense;
    protected boolean blocking;
    protected boolean alive;

    public Entity(Position position, char character, Color color, String name,
                  int maxHp, int attack, int defense, boolean blocking) {
        this.position = position;
        this.character = character;
        this.color = color;
        this.name = name;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.attack = attack;
        this.defense = defense;
        this.blocking = blocking;
        this.alive = true;
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

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public boolean isBlocking() {
        return blocking;
    }

    public boolean isAlive() {
        return alive;
    }

    public void takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) {
            hp = 0;
            die();
        }
    }

    public void heal(int amount) {
        hp = Math.min(hp + amount, maxHp);
    }

    protected void die() {
        alive = false;
        blocking = false;
        character = '%';
        color = new Color(191, 0, 0);
        name = "remains of " + name;
    }

    public abstract void update();
}
