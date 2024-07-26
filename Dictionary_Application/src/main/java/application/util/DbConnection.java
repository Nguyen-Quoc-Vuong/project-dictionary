package application.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/* Singleton pattern, ensuring that only 1 db connection is created and used*/
public class DbConnection {
    private Connection dbLink;
    private static DbConnection instance;

    private DbConnection() {
        System.out.println("Try to connect db");
        createConnection();
    }

    private void createConnection() {
        String url = "jdbc:sqlite:D:/dictionary.db";
        try {
            Class.forName("org.sqlite.JDBC");
            if (dbLink == null || dbLink.isClosed()) {
                this.dbLink = DriverManager.getConnection(url);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static DbConnection getInstance() {
        if (instance == null) {
            instance = new DbConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (dbLink == null || dbLink.isClosed()) {
                createConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dbLink;
    }

    public void closeConnection() {
        if (dbLink != null) {
            try {
                dbLink.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
