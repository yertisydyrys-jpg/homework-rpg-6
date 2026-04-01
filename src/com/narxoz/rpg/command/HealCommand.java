package com.narxoz.rpg.command;

import com.narxoz.rpg.arena.ArenaFighter;

public class HealCommand implements ActionCommand {
    private final ArenaFighter target;
    private final int healAmount;
    private int actualHeal;

    public HealCommand(ArenaFighter target, int healAmount) {
        this.target = target;
        this.healAmount = healAmount;
    }

    @Override
    public void execute() {
        if (target.getHealPotions() > 0) {
            int missingHealth = target.getMaxHealth() - target.getHealth();
            actualHeal = Math.min(healAmount, missingHealth);
            target.heal(actualHeal);
            System.out.printf("[Heal] %s healed for %d HP (potions left: %d)%n",
                    target.getName(), actualHeal, target.getHealPotions());
        } else {
            actualHeal = 0;
            System.out.printf("[Heal] %s has no potions left, no healing%n", target.getName());
        }
    }

    @Override
    public void undo() {
        if (actualHeal > 0) {
            target.takeDamage(actualHeal);
            System.out.printf("[Undo] Removed %d HP from %s%n", actualHeal, target.getName());
        }
    }

    @Override
    public String getDescription() {
        return "Heal (amount " + healAmount + ")";
    }
}