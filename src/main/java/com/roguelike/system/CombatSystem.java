package com.roguelike.system;

import com.roguelike.entity.Entity;
import com.roguelike.entity.Player;
import java.util.Random;

public class CombatSystem {
    private static final Random random = new Random();

    public static CombatResult attack(Entity attacker, Entity defender, MessageLog log) {
        int damage = attacker.getAttack() - defender.getDefense();

        if (damage > 0) {
            // Add some randomness to damage
            int variance = random.nextInt(3) - 1; // -1, 0, or 1
            damage = Math.max(1, damage + variance);

            defender.takeDamage(damage);

            String message = String.format("%s attacks %s for %d damage!",
                    attacker.getName(), defender.getName(), damage);
            log.addMessage(message, MessageLog.MessageType.COMBAT);

            if (!defender.isAlive()) {
                String deathMessage = String.format("%s is dead!", defender.getName());
                log.addMessage(deathMessage, MessageLog.MessageType.IMPORTANT);

                // Award experience if player killed enemy
                if (attacker instanceof Player && defender.getClass().getSimpleName().equals("Enemy")) {
                    Player player = (Player) attacker;
                    try {
                        int expValue = (int) defender.getClass().getMethod("getExpValue").invoke(defender);
                        player.gainExperience(expValue);
                        log.addMessage(String.format("You gain %d experience!", expValue), MessageLog.MessageType.INFO);
                    } catch (Exception e) {
                        // Reflection failed, skip exp
                    }
                }

                return CombatResult.KILLED;
            }

            return CombatResult.HIT;
        } else {
            String message = String.format("%s attacks %s but does no damage.",
                    attacker.getName(), defender.getName());
            log.addMessage(message, MessageLog.MessageType.COMBAT);
            return CombatResult.NO_DAMAGE;
        }
    }

    public enum CombatResult {
        HIT,
        NO_DAMAGE,
        KILLED
    }
}
