package me.srb.minirpg.utils;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.srb.squarerpg.SquareRPG;
import me.srb.squarerpg.model.Dungeon;
import me.srb.squarerpg.model.Mob;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class StorageUtil {

    private static ArrayList<Dungeon> dungeons = new ArrayList<>();
    private static ArrayList<Mob> mobs = new ArrayList<>();

    // Dungeon methods

    public static Dungeon findDungeon(int dungeonID) {
        for (Dungeon dungeon : dungeons) {
            if (dungeon.dungeonID() == dungeonID) {
                return dungeon;
            }
        }
        return null;
    }

    public static List<Dungeon> getAllDungeons() {
        return dungeons;
    }

    // Mob methods

    public static Mob findMob(int mobID) {
        for (Mob mob : mobs) {
            if (mob.mobID() == mobID) {
                return mob;
            }
        }
        return null;
    }

    public static List<Mob> getAllMobs() {
        return mobs;
    }

    // Load methods for dungeons and mobs

    public static void loadDungeons() throws IOException {
        Gson gson = new Gson();
        File file = new File(SquareRPG.getPlugin().getDataFolder().getAbsolutePath() + "/dungeons.json");
        if (file.exists()) {
            Reader reader = new FileReader(file);
            Type dungeonListType = new TypeToken<ArrayList<Dungeon>>() {
            }.getType();
            dungeons = gson.fromJson(reader, dungeonListType);
            System.out.println("Dungeons loaded.");
        }
    }

    public static void loadMobs() throws IOException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ItemStack.class, new ItemStackDeserializer())
                .create();
        File file = new File(SquareRPG.getPlugin().getDataFolder().getAbsolutePath() + "/mobs.json");
        if (file.exists()) {
            Reader reader = new FileReader(file);
            Type mobListType = new TypeToken<ArrayList<Mob>>() {
            }.getType();
            mobs = gson.fromJson(reader, mobListType);
            System.out.println("Mobs loaded.");
        }
    }
}
