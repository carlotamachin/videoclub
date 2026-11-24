package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String URL = "jdbc:sqlite:resources/database/videoclub.db";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}