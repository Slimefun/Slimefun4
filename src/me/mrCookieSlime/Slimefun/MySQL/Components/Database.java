package me.mrCookieSlime.Slimefun.MySQL.Components;

import me.mrCookieSlime.Slimefun.MySQL.MySQLMain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {
    private Connection connection;
    private boolean keepAlive;
    private String mysql_database;
    public Database(String mysql_database, boolean KeepAlive)
    {
        this.mysql_database = mysql_database;
        this.keepAlive = KeepAlive;
        connection = Initialize();
    }
    private Connection Initialize() {
        try {
            /*if (connection != null && !connection.isClosed()) {
                return connection;
            }*/

            Class.forName("com.mysql.jdbc.Driver");

            String connectUrl = "jdbc:mysql://" + MySQLMain.instance.titan_mysql_host + ":" + MySQLMain.instance.titan_mysql_port + "/" + mysql_database + "?autoReconnect=true&useSSL=false";

            Connection connection = DriverManager.getConnection(connectUrl, MySQLMain.instance.titan_mysql_username, MySQLMain.instance.titan_mysql_password);

            return connection;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public Connection getConnection() {
        return connection;
    }

    public void ping()
    {
        if (keepAlive) {
            try (PreparedStatement ps = connection.prepareStatement("/* ping */ SELECT 1")) {
                ps.executeQuery();
            } catch (SQLException ex) {
                disconnect();
                connection = Initialize();
            }
        }
    }
    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
