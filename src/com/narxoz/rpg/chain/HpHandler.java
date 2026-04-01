package com.narxoz.rpg.chain;

import com.narxoz.rpg.arena.ArenaFighter;

public class HpHandler extends DefenseHandler {

    @Override
    public void handle(int incomingDamage, ArenaFighter target) {
        System.out.printf("[HP] Final damage %d applied to %s%n", incomingDamage, target.getName());
        target.takeDamage(incomingDamage);
        // Terminal handler: do NOT call passToNext
    }
}