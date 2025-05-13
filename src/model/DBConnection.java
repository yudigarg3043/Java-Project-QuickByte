package model;

import java.sql.*;

public class DBConnection {
<<<<<<< HEAD
    private static final String URL = "jdbc:mysql://localhost:3306/javaproject2";
    private static final String USER = "root";
    private static final String PASS = "Sunny@41";
=======
    private static final String URL = "jdbc:mysql://localhost:3306/javaproject2"; //URL : jdbc:mysql://localhost:3306/{Database_Name}
    private static final String USER = "root"; //UserName of MySQL
    private static final String PASS = "Mahashakti@123"; // Password of your MySQL
>>>>>>> 2e391e4e35f029c02bd50a8e0c4150078980e6cb

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
