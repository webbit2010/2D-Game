package com.roguelike.item;

import java.util.ArrayList;
import java.util.List;

/**
 * Inventar-System für Items.
 * Verwaltet eine begrenzte Anzahl von Items mit festgelegter Kapazität.
 */
public class Inventory {
    /** Liste der Items im Inventar */
    private List<Item> items;

    /** Maximale Anzahl Items */
    private int capacity;

    /**
     * Erstellt ein neues Inventar mit fester Kapazität.
     *
     * @param capacity Maximale Anzahl von Items
     */
    public Inventory(int capacity) {
        this.capacity = capacity;
        this.items = new ArrayList<>();
    }

    /**
     * Fügt ein Item zum Inventar hinzu.
     *
     * @param item Das hinzuzufügende Item
     * @return true wenn erfolgreich, false wenn Inventar voll
     */
    public boolean addItem(Item item) {
        if (items.size() < capacity) {
            items.add(item);
            return true;
        }
        return false;
    }

    /**
     * Entfernt ein Item aus dem Inventar.
     *
     * @param item Das zu entfernende Item
     * @return true wenn erfolgreich, false wenn Item nicht gefunden
     */
    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    /**
     * Holt ein Item an einem bestimmten Index.
     *
     * @param index Index des Items (0-basiert)
     * @return Das Item oder null wenn Index ungültig
     */
    public Item getItem(int index) {
        if (index >= 0 && index < items.size()) {
            return items.get(index);
        }
        return null;
    }

    /**
     * Gibt eine Kopie der Item-Liste zurück.
     *
     * @return Liste aller Items im Inventar
     */
    public List<Item> getItems() {
        return new ArrayList<>(items);
    }

    /**
     * @return Aktuelle Anzahl Items im Inventar
     */
    public int getSize() {
        return items.size();
    }

    /**
     * @return Maximale Kapazität des Inventars
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Prüft ob das Inventar voll ist.
     *
     * @return true wenn voll, false sonst
     */
    public boolean isFull() {
        return items.size() >= capacity;
    }
}
