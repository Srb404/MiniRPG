package me.srb.minirpg.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Database {
    // *** SQL CONNECTION *** //
    private Connection connection;

    private Connection getConnection() throws SQLException {
        if (connection == null) {
            String url = "jdbc:mysql://localhost/statistics";
            String login = "root";
            String password = "";

            Connection connection = DriverManager.getConnection(url, login, password);
            this.connection = connection;
            System.out.println("Połączono z bazą danych.");
        }
        return connection;
    }

    public void initializeDatabase() throws SQLException {
        Statement statement = getConnection().createStatement();
        String sqlPlayerStats = "CREATE TABLE IF NOT EXISTS player_stats (uuid varchar(36) primary key, " +
                "balance double, join_date DATETIME, last_online DATETIME, " +
                "energy int, level int, houseLvl int, exp int)";
        statement.execute(sqlPlayerStats);

        String sqlPlayerBattleStats = "CREATE TABLE IF NOT EXISTS player_battle_stats (uuid varchar(36) primary key, " +
                "maxHP int, maxMana int, maxStamina int, specialAttacks TEXT)";
        statement.execute(sqlPlayerBattleStats);

        statement.close();
    }
    // *** END *** //

    private static final String SELECT_PLAYER_STATS = "SELECT * FROM player_stats WHERE uuid = ?";
    private static final String SELECT_PLAYER_BATTLE_STATS = "SELECT * FROM player_battle_stats WHERE uuid = ?";
    private static final String INSERT_PLAYER_STATS = "INSERT INTO player_stats(uuid, balance, join_date, last_online, energy, level, houseLvl, exp) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_PLAYER_BATTLE_STATS = "INSERT INTO player_battle_stats(uuid, maxHP, maxMana, maxStamina, specialAttacks) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_PLAYER_STATS = "UPDATE player_stats SET balance = ?, last_online = ?, energy = ?, level = ?, houseLvl = ?, exp = ? WHERE uuid = ?";
    private static final String UPDATE_PLAYER_BATTLE_STATS = "UPDATE player_battle_stats SET maxHP = ?, maxMana = ?, maxStamina = ?, specialAttacks = ? WHERE uuid = ?";

    // *** SQL PLAYER MANAGEMENT *** //
    public PlayerDefaultStats getPlayerStats(String uuid) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(SELECT_PLAYER_STATS)) {
            statement.setString(1, uuid);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                PlayerDefaultStats newPlayerDefaultStats = new PlayerDefaultStats(uuid, 0, new java.util.Date(), new java.util.Date(), 200, 1, 1, 0);
                createPlayerStats(newPlayerDefaultStats);
                return getPlayerStats(uuid);
            }
            return mapResultSetToPlayerStats(resultSet);
        }
    }

    public PlayerBattleStats getPlayerBattleStats(String uuid) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(SELECT_PLAYER_BATTLE_STATS)) {
            statement.setString(1, uuid);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                PlayerBattleStats newPlayerBattleStats = new PlayerBattleStats(uuid, 50, 100, 100, new ArrayList<>());
                createPlayerBattleStats(newPlayerBattleStats);
                return getPlayerBattleStats(uuid);
            }
            return mapResultSetToPlayerBattleStats(resultSet);
        }
    }

    private PlayerDefaultStats mapResultSetToPlayerStats(ResultSet resultSet) throws SQLException {
        String uuid = resultSet.getString("uuid");
        int balance = resultSet.getInt("balance");
        Timestamp joinDate = resultSet.getTimestamp("join_date");
        Timestamp lastOnline = resultSet.getTimestamp("last_online");
        int energy = resultSet.getInt("energy");
        int level = resultSet.getInt("level");
        int houseLvl = resultSet.getInt("houseLvl");
        int exp = resultSet.getInt("exp");

        return new PlayerDefaultStats(uuid, balance, joinDate, lastOnline, energy, level, houseLvl, exp);
    }

    private PlayerBattleStats mapResultSetToPlayerBattleStats(ResultSet resultSet) throws SQLException {
        String uuid = resultSet.getString("uuid");
        int maxHP = resultSet.getInt("maxHP");
        int maxMana = resultSet.getInt("maxMana");
        int maxStamina = resultSet.getInt("maxStamina");
        List<String> specialAttacks = Arrays.asList(resultSet.getString("specialAttacks").split(","));
        return new PlayerBattleStats(uuid, maxHP, maxMana, maxStamina, specialAttacks);
    }

    /* one time use */
    public void createPlayerStats(PlayerDefaultStats playerDefaultStats) throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement(INSERT_PLAYER_STATS);
        statement.setString(1, playerDefaultStats.getPlayerUUID().toString());
        statement.setInt(2, playerDefaultStats.getBalance());
        statement.setTimestamp(3, new Timestamp(playerDefaultStats.getJoinDate().getTime()));
        statement.setTimestamp(4, new Timestamp(playerDefaultStats.getLastOnline().getTime()));
        statement.setInt(5, playerDefaultStats.getCurrentEnergy());
        statement.setInt(6, playerDefaultStats.getLevel());
        statement.setInt(7, playerDefaultStats.getHouseLvl());
        statement.setInt(8, playerDefaultStats.getExp());

        statement.executeUpdate();
        statement.close();
    }

    private void createPlayerBattleStats(PlayerBattleStats playerBattleStats) throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement(INSERT_PLAYER_BATTLE_STATS);
        statement.setString(1, playerBattleStats.getPlayerUUID().toString());
        statement.setInt(2, playerBattleStats.getMaxHP());
        statement.setInt(3, playerBattleStats.getMaxMana());
        statement.setInt(4, playerBattleStats.getMaxStamina());
        statement.setString(5, playerBattleStats.getSpecialAttacks().toString());

        statement.executeUpdate();
        statement.close();
    }

    public void updatePlayerDefaultStats(PlayerDefaultStats playerDefaultStats) throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement(UPDATE_PLAYER_STATS);
        statement.setDouble(1, playerDefaultStats.getBalance());
        statement.setTimestamp(2, new Timestamp(playerDefaultStats.getLastOnline().getTime()));
        statement.setInt(3, playerDefaultStats.getCurrentEnergy());
        statement.setInt(4, playerDefaultStats.getLevel());
        statement.setInt(5, playerDefaultStats.getHouseLvl());
        statement.setInt(6, playerDefaultStats.getExp());
        statement.setString(7, playerDefaultStats.getPlayerUUID().toString());

        statement.executeUpdate();
        statement.close();
    }

    public void updatePlayerBattleStats(PlayerBattleStats playerStats) throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement(UPDATE_PLAYER_BATTLE_STATS);
        statement.setInt(1, playerStats.getMaxHP());
        statement.setInt(2, playerStats.getMaxMana());
        statement.setInt(3, playerStats.getMaxStamina());
        statement.setString(4, String.join(",", playerStats.getSpecialAttacks()));
        statement.setString(5, playerStats.getPlayerUUID().toString());

        statement.executeUpdate();
        statement.close();
    }
    // *** END *** //
}
