package me.srb.minirpg.model.npc;

import me.srb.minirpg.model.statistics.BasicStats;
import org.bukkit.entity.EntityType;

public record NPC(int id, EntityType type, String name, BasicStats basicStats) { }
