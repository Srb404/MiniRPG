package me.srb.minirpg.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnector {

    private Connection connection;

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String url = "jdbc:mysql://localhost/minirpg";
            String login = "root";
            String password = "";

            this.connection = DriverManager.getConnection(url, login, password);
            System.out.println("Połączono z bazą danych.");

            Statement statement = connection.createStatement();
            statement.execute(SQLQueries.CREATE_CITY_DATA_TABLE);
            statement.execute(SQLQueries.CREATE_PLAYER_DATA_TABLE);
            System.out.println("Przygotowano wymagane tabele.");
        }
        return connection;
    }
}
