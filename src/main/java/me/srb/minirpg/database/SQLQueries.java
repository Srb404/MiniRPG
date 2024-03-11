package me.srb.minirpg.database;

public class SQLQueries {

    public static final String CREATE_PLAYER_DATA_TABLE = """
                CREATE TABLE IF NOT EXISTS PlayerData (
                    UUID char(64) NOT NULL PRIMARY KEY,
                    class char(32) NOT NULL DEFAULT 'Przybłęda',
                    level int NOT NULL DEFAULT 1,
                    exp int NOT NULL DEFAULT 0,
                    money int NOT NULL DEFAULT 0,
                    energy int NOT NULL DEFAULT 100
                )""";

    public static final String CREATE_CITY_DATA_TABLE = """
                CREATE TABLE IF NOT EXISTS CityData (
                    ID int NOT NULL PRIMARY KEY AUTO_INCREMENT,
                    UUID varchar(64),
                    FOREIGN KEY (UUID) REFERENCES playerdata(UUID),
                    homeLvl int NOT NULL DEFAULT 1,
                    reputation int NOT NULL DEFAULT 0
                )""";

    public static final String SELECT_PLAYER_STAT = "SELECT %s FROM %s WHERE UUID = ?";
    public static final String UPDATE_PLAYER_STAT = "UPDATE %s SET %s = ? WHERE UUID = ?";
    public static final String INSERT_PLAYER_DATA = "INSERT INTO PlayerData(uuid) VALUES (?)";
    public static final String INSERT_CITY_DATA = "INSERT INTO CityData(uuid) VALUES (?)";
    public static final String DELETE_RECORD = "DELETE FROM %s WHERE UUID = ?";
    public static final String CHECK_PLAYER_EXISTENCE = "SELECT COUNT(*) FROM PlayerData WHERE UUID = ?";
}
