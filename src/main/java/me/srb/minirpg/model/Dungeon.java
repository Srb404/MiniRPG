package me.srb.minirpg.model;

import me.srb.minirpg.model.npc.DungeonFloor;

public record Dungeon(int id, String name, DungeonFloor[] dungeonFloors) {
}
