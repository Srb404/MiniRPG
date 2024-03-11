package me.srb.minirpg.model.statistics;

import org.bukkit.inventory.ItemStack;

public record ItemStats(int id, String name, String description, ItemStack type, BasicStats stats) {
}
