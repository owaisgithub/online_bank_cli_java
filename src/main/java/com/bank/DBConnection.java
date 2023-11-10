package com.bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Random;

public class DBConnection {
    public Connection connection() {
        try {
            // #1 Database Details
            String connectionString = "jdbc:mysql://localhost/bank";
            String user = "root";
            String dbpassword = "rootpassword";

            // #2 Load JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // #3 Open Connection
            Connection conn = DriverManager.getConnection(connectionString, user, dbpassword);

            return conn;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
