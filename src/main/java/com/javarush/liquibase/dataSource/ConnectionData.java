package com.javarush.liquibase.dataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionData {

    public static final String DB_URL = "jdbc:p6spy:mysql://localhost:3306/world";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "root";

    public static Connection getConnection() throws SQLException {

        return DriverManager.getConnection(
                DB_URL,
                DB_USER,
                DB_PASSWORD
        );

    }

}
