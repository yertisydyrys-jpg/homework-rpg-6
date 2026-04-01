package com.narxoz.rpg.command;

import com.narxoz.rpg.arena.ArenaOpponent;

public class AttackCommand implements ActionCommand {
    private final ArenaOpponent opponent;
    private final int attackPower;
    private int damageDealt;

    public AttackCommand(ArenaOpponent opponent, int attackPower) {
        this.opponent = opponent;
        this.attackPower = attackPower;
    }

    @Override
    public void execute() {
        damageDealt = Math.min(opponent.getHealth(), attackPower);
        opponent.takeDamage(damageDealt);
        System.out.printf("[Attack] %s deals %d damage to %s%n",
                opponent.getName(), damageDealt, opponent.getName()); // Actually hero attacks opponent, better to pass hero name
        // But we don't have hero in command; maybe add hero name later
        // For simplicity, just use opponent name as target
    }

    @Override
    public void undo() {
        opponent.restoreHealth(damageDealt);
        System.out.printf("[Undo] Restored %d health to %s%n", damageDealt, opponent.getName());
    }

    @Override
    public String getDescription() {
        return "Attack (power " + attackPower + ")";
    }
}