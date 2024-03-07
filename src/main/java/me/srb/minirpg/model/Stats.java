package me.srb.minirpg.model;

import org.bukkit.inventory.ItemStack;

public record Stats(int attack, int defense, int speed, int hp, ItemStack[] armor, ItemStack weapon) { }
