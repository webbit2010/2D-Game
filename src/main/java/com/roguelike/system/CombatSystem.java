package com.roguelike.system;

import com.roguelike.entity.Entity;
import com.roguelike.entity.Player;
import java.util.Random;

/**
 * Kampfsystem für Angriffe zwischen Entities.
 * Berechnet Schaden basierend auf ATK vs DEF mit Zufallsvarianz.
 */
public class CombatSystem {
    /** Zufallsgenerator für Schadensvarianz */
    private static final Random random = new Random();

    /**
     * Führt einen Angriff durch.
     * Berechnet Schaden als (ATK - DEF) mit Zufallsvarianz von -1 bis +1.
     * Vergibt Erfahrung wenn Spieler einen Gegner tötet.
     *
     * @param attacker Der Angreifer
     * @param defender Der Verteidiger
     * @param log MessageLog für Kampfnachrichten
     * @return Ergebnis des Angriffs (HIT, NO_DAMAGE oder KILLED)
     */
    public static CombatResult attack(Entity attacker, Entity defender, MessageLog log) {
        int damage = attacker.getAttack() - defender.getDefense();

        if (damage > 0) {
            // Füge Zufallsvarianz hinzu für interessanteres Gameplay
            int variance = random.nextInt(3) - 1; // -1, 0, oder 1
            damage = Math.max(1, damage + variance); // Mindestens 1 Schaden

            defender.takeDamage(damage);

            String message = String.format("%s attacks %s for %d damage!",
                    attacker.getName(), defender.getName(), damage);
            log.addMessage(message, MessageLog.MessageType.COMBAT);

            if (!defender.isAlive()) {
                String deathMessage = String.format("%s is dead!", defender.getName());
                log.addMessage(deathMessage, MessageLog.MessageType.IMPORTANT);

                // Vergebe Erfahrung wenn Spieler einen Gegner getötet hat
                if (attacker instanceof Player && defender.getClass().getSimpleName().equals("Enemy")) {
                    Player player = (Player) attacker;
                    try {
                        // Nutze Reflection um EXP-Wert vom Gegner zu holen
                        int expValue = (int) defender.getClass().getMethod("getExpValue").invoke(defender);
                        player.gainExperience(expValue);
                        log.addMessage(String.format("You gain %d experience!", expValue), MessageLog.MessageType.INFO);
                    } catch (Exception e) {
                        // Reflection fehlgeschlagen, überspringe EXP
                    }
                }

                return CombatResult.KILLED;
            }

            return CombatResult.HIT;
        } else {
            // Kein Schaden - Verteidigung war zu hoch
            String message = String.format("%s attacks %s but does no damage.",
                    attacker.getName(), defender.getName());
            log.addMessage(message, MessageLog.MessageType.COMBAT);
            return CombatResult.NO_DAMAGE;
        }
    }

    /**
     * Mögliche Ergebnisse eines Angriffs.
     */
    public enum CombatResult {
        /** Angriff war erfolgreich, Ziel wurde getroffen */
        HIT,

        /** Angriff war erfolglos, kein Schaden verursacht */
        NO_DAMAGE,

        /** Angriff tötete das Ziel */
        KILLED
    }
}
