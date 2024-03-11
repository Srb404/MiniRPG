package me.srb.minirpg.database;

import me.srb.minirpg.database.enums.CityDataFields;
import me.srb.minirpg.database.enums.PlayerDataFields;
import me.srb.minirpg.database.enums.Tables;

import java.sql.*;

import static me.srb.minirpg.database.SQLQueries.*;

public class PlayerManager {

    private final Connection connection;

    public PlayerManager(Connection connection) {
        this.connection = connection;
    }

    public String getPlayerStat(String uuid, PlayerDataFields playerDataField) throws SQLException {
        return getPlayerStat(uuid, playerDataField.toString(), Tables.PlayerData.toString());
    }

    public String getPlayerStat(String uuid, CityDataFields cityDataFields) throws SQLException {
        return getPlayerStat(uuid, cityDataFields.toString(), Tables.CityData.toString());
    }

    private String getPlayerStat(String uuid, String data, String table) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(String.format(SELECT_PLAYER_STAT, data, table))) {
            statement.setString(1, uuid);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) return resultSet.getString(data);
            else return null;
        }
    }

    public void updatePlayerStat(String uuid, PlayerDataFields playerDataField, String newValue) throws SQLException {
        updatePlayerStat(uuid, playerDataField.toString(), newValue, Tables.PlayerData.toString());
    }

    public void updatePlayerStat(String uuid, CityDataFields cityDataField, String newValue) throws SQLException {
        updatePlayerStat(uuid, cityDataField.toString(), newValue, Tables.CityData.toString());
    }

    private void updatePlayerStat(String uuid, String field, String newValue, String table) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(String.format(UPDATE_PLAYER_STAT, table, field))) {
            statement.setString(1, newValue);
            statement.setString(2, uuid);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Nie udało się zaaktualizować rekordu w bazie.");
                throw new SQLException();
            }
        }
    }

    public void createPlayer(String uuid) throws SQLException {
        if (!playerExists(uuid)) {
            createRecord(uuid, INSERT_PLAYER_DATA);
            createRecord(uuid, INSERT_CITY_DATA);
        }
    }

    private void createRecord(String uuid, String query) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, uuid);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Nie udało się utworzyć rekordu w tabeli.");
                throw new SQLException();
            }
        }
    }

    public void deletePlayer(String uuid) throws SQLException {
        if (playerExists(uuid)) {
            deleteRecord(uuid, Tables.CityData.toString());
            deleteRecord(uuid, Tables.PlayerData.toString());
        } else {
            System.out.println("Gracz o podanym UUID nie istnieje w bazie danych.");
        }
    }

    private void deleteRecord(String uuid, String table) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(String.format(DELETE_RECORD, table))) {
            statement.setString(1, uuid);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Nie udało się usunąć rekordu z tabeli.");
                throw new SQLException();
            }
        }
    }

    private boolean playerExists(String uuid) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CHECK_PLAYER_EXISTENCE)) {
            statement.setString(1, uuid);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        }
        return false;
    }
}
