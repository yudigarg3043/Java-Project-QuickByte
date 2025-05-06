package controller;

import java.sql.*;
import java.util.Scanner;
import model.DBConnection;

public class Admin {
    Scanner sc = new Scanner(System.in);

    public void adminMenu() {
        while (true) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. Add item to menu");
            System.out.println("2. View total sales");
            System.out.println("3. Exit");

            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addItemToMenu();
                    break;
                case 2:
                    viewTotalSales();
                    break;
                case 3:
                    System.out.println("Exiting admin menu.");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void addItemToMenu() {
        System.out.print("Enter item name: ");
        String name = sc.nextLine();
        System.out.print("Enter item price: ");
        double price = sc.nextDouble();

        String sql = "INSERT INTO food_items (name, price) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setDouble(2, price);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Item added successfully.");
            } else {
                System.out.println("Failed to add item.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void viewTotalSales() {
        String sql = "SELECT SUM(total_amount) AS total_sales FROM orders";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                double totalSales = rs.getDouble("total_sales");
                System.out.println("Total Sales: ₹" + totalSales);
            } else {
                System.out.println("No sales data found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

