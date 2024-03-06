package me.srb.minirpg.listeners.events;

import me.srb.squarerpg.database.Database;
import me.srb.squarerpg.database.SpigotSideStats;
import me.srb.squarerpg.model.stats.PlayerBattleStats;
import me.srb.squarerpg.model.stats.PlayerDefaultStats;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

public class PlayerQuit {
    private final PlayerQuitEvent playerQuitEvent;
    private final Player player;
    private final Database database;
    private PlayerDefaultStats playerDefaultStats;
    private PlayerBattleStats playerBattleStats;

    public PlayerQuit(PlayerQuitEvent playerQuitEvent, Database database) {
        this.playerQuitEvent = playerQuitEvent;
        this.player = playerQuitEvent.getPlayer();
        this.database = database;
    }

    public void updateDatabase() {
        try {
            UUID playerUUID = player.getUniqueId();

            playerDefaultStats = SpigotSideStats.defaultStatsMap.get(playerUUID);
            playerDefaultStats.setLastOnline(new Date());
            SpigotSideStats.defaultStatsMap.remove(playerUUID);
            database.updatePlayerDefaultStats(playerDefaultStats);

            playerBattleStats = SpigotSideStats.battleStatsMap.get(playerUUID);
            SpigotSideStats.battleStatsMap.remove(playerUUID);
            database.updatePlayerBattleStats(playerBattleStats);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
