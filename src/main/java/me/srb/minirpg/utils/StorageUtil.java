package me.srb.minirpg.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import me.srb.minirpg.MiniRPG;
import me.srb.minirpg.model.location.Dungeon;
import me.srb.minirpg.model.npc.NPC;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class StorageUtil {

    @Getter
    private static ArrayList<NPC> NPCs = new ArrayList<>();

    @Getter
    private static ArrayList<Dungeon> dungeons = new ArrayList<>();

    public static NPC findNPC(int npcID) {
        for (NPC npc : NPCs) if (npc.id() == npcID) return npc;
        return null;
    }

    public static Dungeon findDungeon(int dungeonID) {
        for (Dungeon dungeon : dungeons) if (dungeon.id() == dungeonID) return dungeon;
        return null;
    }

    public static void loadNPCs() throws IOException {
        Gson gson = new Gson();
        File file = new File(MiniRPG.getPlugin().getDataFolder().getAbsolutePath() + "/npc.json");
        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                Type npcListType = new TypeToken<ArrayList<NPC>>() {}.getType();
                NPCs = gson.fromJson(reader, npcListType);
                System.out.println("NPCs loaded.");
            }
        }
    }

    public static void loadDungeons() throws IOException {
        Gson gson = new Gson();
        File file = new File(MiniRPG.getPlugin().getDataFolder().getAbsolutePath() + "/dungeons.json");
        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                Type dungeonListType = new TypeToken<ArrayList<Dungeon>>() {}.getType();
                dungeons = gson.fromJson(reader, dungeonListType);
                System.out.println("Dungeons loaded.");
            }
        }
    }
}
