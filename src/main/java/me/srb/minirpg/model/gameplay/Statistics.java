package me.srb.minirpg.model.gameplay;

import org.bukkit.inventory.ItemStack;

public record Statistics(int attack, int defense, int speed, int hp, ItemStack[] armor, ItemStack weapon) { }
