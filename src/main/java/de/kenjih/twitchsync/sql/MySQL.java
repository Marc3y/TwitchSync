package de.kenjih.twitchsync.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    private String host = "157.90.26.182";
    private String port = "3306";
    private String database = "TwitchSync";
    private String username = "mysql_database";
    private String password = "u8i9d3wqhd7892879eh278ueh9";

    private static Connection connection;

    public boolean isConnected(){ return !(connection == null); }

    public void connect(){
        if(!isConnected()){
            try {
                connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?useSSL=false", this.username, this.password);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void disconnect() {
        if (isConnected())
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public static Connection getConnection() {
        return connection;
    }

}
