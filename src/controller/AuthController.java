package controller;

import model.DBConnection;
import model.User;

import java.sql.*;
import java.util.Scanner;

public class AuthController {
    private Scanner sc = new Scanner(System.in);

    public User register() throws SQLException {
        System.out.println("---- Registering User ----");
        System.out.print("Enter username: ");
        String username = sc.next();
        System.out.print("Enter email: ");
        String email = sc.next();
        System.out.print("Enter password: ");
        String password = sc.next();

        Connection conn = DBConnection.getConnection();
        String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);
        stmt.setString(2, email);
        stmt.setString(3, password);
        stmt.executeUpdate();
        conn.close();
        return login(); // auto login after registration
    }

//    public User login() throws SQLException {
//        System.out.print("Enter username: ");
//        String username = sc.next();
//        System.out.print("Enter password: ");
//        String password = sc.next();
//
//        Connection conn = DBConnection.getConnection();
//        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
//        PreparedStatement stmt = conn.prepareStatement(sql);
//        stmt.setString(1, username);
//        stmt.setString(2, password);
//        ResultSet rs = stmt.executeQuery();
//
//        if (rs.next()) {
//            User user = new User(username, rs.getString("email"), password);
//            user.id = rs.getInt("id");
//            conn.close();
//            return user;
//        } else {
//            System.out.println("Invalid credentials.");
//            conn.close();
//            return null;
//        }
//    }

    public User login() throws SQLException {
        System.out.println("---- Logging In ----");
        System.out.print("Enter username: ");
        String username = sc.next();
        System.out.print("Enter password: ");
        String password = sc.next();

        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            User user = new User(username, rs.getString("email"), password);
            user.id = rs.getInt("id");
            conn.close();

            // Check if admin
            if (username.equals("Admin")) {
                System.out.println("Welcome, Admin!");
                Admin admin = new Admin();
                admin.adminMenu(); // show admin options
                return null; // or return admin object if needed
            }

            return user;
        } else {
            System.out.println("Invalid credentials.");
            conn.close();
            return null;
        }
    }

}
