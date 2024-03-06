package me.srb.minirpg.listeners.events;

import me.srb.squarerpg.database.Database;
import me.srb.squarerpg.database.SpigotSideStats;
import me.srb.squarerpg.model.stats.PlayerBattleStats;
import me.srb.squarerpg.model.stats.PlayerDefaultStats;
import me.srb.squarerpg.scoreboard.DefaultScr;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

public class PlayerJoin {
    private final PlayerJoinEvent playerJoinEvent;
    private final Player player;
    private final Database database;
    private final PlayerDefaultStats playerDefaultStats;

    public PlayerJoin(PlayerJoinEvent playerJoinEvent, Database database) {
        this.playerJoinEvent = playerJoinEvent;
        this.player = playerJoinEvent.getPlayer();
        this.database = database;
        this.playerDefaultStats = updateDatabaseAndMap();
    }

    public void setJoinMessage(String joinMessage) {
        playerJoinEvent.setJoinMessage(joinMessage);
    }

    private PlayerDefaultStats updateDatabaseAndMap() {
        UUID uuid = player.getUniqueId();
        try {
            PlayerDefaultStats playerDefaultStats = database.getPlayerStats(uuid.toString());
            PlayerBattleStats playerBattleStats = database.getPlayerBattleStats(uuid.toString());

            int timeOffline = (int) ((new Date().getTime() - playerDefaultStats.getLastOnline().getTime()) / 1000);
            playerDefaultStats.updateEnergy(timeOffline);
            database.updatePlayerDefaultStats(playerDefaultStats);
            SpigotSideStats.defaultStatsMap.put(uuid, playerDefaultStats);
            SpigotSideStats.battleStatsMap.put(uuid, playerBattleStats);
            return playerDefaultStats;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setDefaultScoreboard() {
        if (playerDefaultStats == null) return;
        DefaultScr defaultScr = new DefaultScr(player, playerDefaultStats);
        defaultScr.set();
    }

    public void setPrefix() {
        player.setDisplayName(playerDefaultStats.getTitle(playerDefaultStats.getLevel()) + " " + player.getDisplayName());
    }
}
