package model;

import java.sql.*;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/javaproject2"; //URL : jdbc:mysql://localhost:3306/{Database_Name}
    private static final String USER = "root"; //UserName of MySQL
    private static final String PASS = "Mahashakti@123"; // Password of your MySQL

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
