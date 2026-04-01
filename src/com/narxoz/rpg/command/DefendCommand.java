package com.narxoz.rpg.command;

import com.narxoz.rpg.arena.ArenaFighter;

public class DefendCommand implements ActionCommand {
    private final ArenaFighter target;
    private final double dodgeBoost;

    public DefendCommand(ArenaFighter target, double dodgeBoost) {
        this.target = target;
        this.dodgeBoost = dodgeBoost;
    }

    @Override
    public void execute() {
        target.modifyDodgeChance(dodgeBoost);
        System.out.printf("[Defend] %s boosts dodge chance by +%.2f%n",
                target.getName(), dodgeBoost);
    }

    @Override
    public void undo() {
        target.modifyDodgeChance(-dodgeBoost);
        System.out.printf("[Undo] %s loses dodge chance boost%n", target.getName());
    }

    @Override
    public String getDescription() {
        return "Defend (+" + dodgeBoost + " dodge)";
    }
}