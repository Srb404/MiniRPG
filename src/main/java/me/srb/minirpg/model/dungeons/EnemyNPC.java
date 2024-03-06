package me.srb.minirpg.model.dungeons;

import me.srb.minirpg.model.gameplay.Statistics;
import org.bukkit.entity.EntityType;

public record EnemyNPC(int id, EntityType type, String name, Statistics stats) { }
