package com.narxoz.rpg.tournament;

import com.narxoz.rpg.arena.ArenaFighter;
import com.narxoz.rpg.arena.ArenaOpponent;
import com.narxoz.rpg.arena.TournamentResult;
import com.narxoz.rpg.chain.*;
import com.narxoz.rpg.command.*;

import java.util.ArrayList;
import java.util.List;

public class TournamentEngine {
    private final ArenaFighter hero;
    private final ArenaOpponent opponent;
    private long randomSeed = System.currentTimeMillis();

    public TournamentEngine(ArenaFighter hero, ArenaOpponent opponent) {
        this.hero = hero;
        this.opponent = opponent;
    }

    public TournamentEngine setRandomSeed(long seed) {
        this.randomSeed = seed;
        return this;
    }

    public TournamentResult runTournament() {
        TournamentResult result = new TournamentResult();
        List<String> log = new ArrayList<>();

        // Build defense chain
        DefenseHandler chain = new DodgeHandler(hero.getDodgeChance(), randomSeed);
        chain.setNext(new BlockHandler(hero.getBlockRating() / 100.0))
                .setNext(new ArmorHandler(hero.getArmorValue()))
                .setNext(new HpHandler());

        ActionQueue actionQueue = new ActionQueue();

        int rounds = 0;
        final int MAX_ROUNDS = 50;
        boolean heroTurn = true; // Hero always goes first in a round? Actually each round hero acts, then opponent.

        while (hero.isAlive() && opponent.isAlive() && rounds < MAX_ROUNDS) {
            rounds++;
            log.add("=== Round " + rounds + " ===");

            // Hero's turn: build queue (simple strategy)
            actionQueue.enqueue(new AttackCommand(opponent, hero.getAttackPower()));
            if (hero.getHealth() < hero.getMaxHealth() / 2 && hero.getHealPotions() > 0) {
                actionQueue.enqueue(new HealCommand(hero, 20));
            }
            // Optionally defend occasionally
            if (Math.random() < 0.3) { // random, but we could use seed; for simplicity use Math.random
                actionQueue.enqueue(new DefendCommand(hero, 0.1));
            }

            log.add("Hero actions queued: " + actionQueue.getCommandDescriptions());
            // Execute all queued actions
            actionQueue.executeAll(); // prints to console but we might want to capture output
            // For simplicity, we'll just let them print; log will capture round summary.

            // Check if opponent died after hero's actions
            if (!opponent.isAlive()) {
                log.add("Opponent defeated!");
                break;
            }

            // Opponent's turn: counterattack through defense chain
            int opponentDamage = opponent.getAttackPower();
            log.add("Opponent attacks with power " + opponentDamage);
            // We need to capture chain output? It prints to console; we can redirect or just add generic message.
            // For log, we can add a line that damage is processed via chain.
            chain.handle(opponentDamage, hero);
            log.add("Hero health after defense: " + hero.getHealth());

            // Check if hero died
            if (!hero.isAlive()) {
                log.add("Hero defeated!");
                break;
            }
        }

        // Determine winner
        String winner;
        if (hero.isAlive()) {
            winner = hero.getName();
        } else {
            winner = opponent.getName();
        }
        result.setWinner(winner);
        result.setRounds(rounds);
        for (String line : log) {
            result.addLine(line);
        }

        return result;
    }
}