package me.srb.minirpg.model.npc;

import me.srb.minirpg.model.Stats;
import org.bukkit.entity.EntityType;

public record NPC(int id, EntityType type, String name, Stats stats) { }
