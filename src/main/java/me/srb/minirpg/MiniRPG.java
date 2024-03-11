package me.srb.minirpg;

import lombok.Getter;
import me.srb.minirpg.database.DatabaseConnector;
import me.srb.minirpg.database.PlayerManager;
import me.srb.minirpg.database.enums.CityDataFields;
import me.srb.minirpg.database.enums.PlayerDataFields;
import me.srb.minirpg.model.location.Dungeon;
import me.srb.minirpg.model.location.DungeonFloor;
import me.srb.minirpg.model.npc.NPC;
import me.srb.minirpg.utils.StorageUtil;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public final class MiniRPG extends JavaPlugin {

    @Getter
    public static MiniRPG plugin;
    @Override
    public void onEnable() {
        plugin = this;
        databaseTest();
        processNPCData();
        System.out.println("Uruchomiono MiniRPG.");
    }

    public static void databaseTest() {
        try {
            DatabaseConnector databaseConnector = new DatabaseConnector();
            Connection connection = databaseConnector.getConnection();
            PlayerManager playerManager = new PlayerManager(connection);

            playerManager.createPlayer("999");
            playerManager.deletePlayer("999");
            playerManager.createPlayer("999");
            playerManager.updatePlayerStat("999", PlayerDataFields.LEVEL, "10");
            playerManager.updatePlayerStat("999", CityDataFields.HOMELVL, "6");

            String level = playerManager.getPlayerStat("999", PlayerDataFields.LEVEL);
            String home = playerManager.getPlayerStat("999", CityDataFields.HOMELVL);
            System.out.println("Poziom gracza: " + level);
            System.out.println("Home level gracza: " + home);

            playerManager.deletePlayer("999");
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void processNPCData() {
        try {
            StorageUtil.loadNPCs();
            StorageUtil.loadDungeons();

            int npcID = 1;
            NPC npc = StorageUtil.findNPC(npcID);
            if (npc != null) System.out.println("Znaleziono NPC o ID " + npcID + ": " + npc.name());
            else System.out.println("Nie znaleziono NPC o ID " + npcID);

            int dungeonID = 1;
            Dungeon dungeon = StorageUtil.findDungeon(dungeonID);
            if (dungeon != null) {
                System.out.println("Znaleziono Dungeon o ID " + dungeonID + ": " + dungeon.name());
                for (DungeonFloor floor : dungeon.dungeonFloors()) {
                    System.out.println("\tEnemies:");
                    for (int enemyID : floor.enemyNPCs()) System.out.println("\t\t" + StorageUtil.findNPC(enemyID));
                    System.out.println("\tBoss:");
                    System.out.println("\t\t" + StorageUtil.findNPC(floor.bossNPC()));
                }
            } else System.out.println("Nie znaleziono Dungeon o ID " + dungeonID);
        } catch (IOException e) {
            System.err.println("Wystąpił błąd podczas wczytywania danych: " + e.getMessage());
        }
    }
}
