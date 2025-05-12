package controller;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import model.DBConnection;
import utils.DSAUtils;
import utils.MenuItem;

public class Admin {
    Scanner sc = new Scanner(System.in);


    public void adminMenu() {
        while (true) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. View Menu");
            System.out.println("2. Add item to menu");
            System.out.println("3. Remove item from menu");
            System.out.println("4. Update item price in menu");
            System.out.println("5. View total sales");
            System.out.println("6. Exit");

            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    viewMenu();
                    break;
                case 2:
                    addItemToMenu();
                    break;
                case 3:
                    removeItemFromMenu();
                    break;
                case 4:
                    updateItemPrice();
                    break;
                case 5:
                    viewTotalSales();
                    break;
                case 6:
                    System.out.println("Exiting admin menu.");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    public void viewMenu(){
        String sql = "SELECT name, price FROM food_items"; // SQL query to fetch menu items
        List<MenuItem> menu = new LinkedList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String name = rs.getString("name");
                double price = rs.getDouble("price");

                // Create MenuItem object and add it to the LinkedList
                menu.add(new MenuItem(name, price));
            }

            DSAUtils.sortMenu(menu);
            System.out.println("---- Menu (Sorted by Price) ----");
            for (MenuItem item : menu) {
                System.out.println(item.name + " - ₹" + item.price);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching menu from database: " + e.getMessage());
        }
    }

    private void addItemToMenu() {
        System.out.println("---- Existing Menu ----");
        viewMenu();
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

    private void removeItemFromMenu() {
        System.out.println("---- Existing Menu ----");
        viewMenu();
        System.out.print("Enter item name to remove: ");
        String name = sc.nextLine();

        String sql = "DELETE FROM food_items WHERE name = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Item removed successfully.");
            } else {
                System.out.println("No such item found in the menu.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateItemPrice() {
        System.out.println("---- Existing Menu ----");
        viewMenu();
        System.out.print("Enter item name to update price: ");
        String name = sc.nextLine();
        System.out.print("Enter new price: ");
        double newPrice = sc.nextDouble();
        sc.nextLine(); // Clear buffer after reading double

        String sql = "UPDATE food_items SET price = ? WHERE name = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, newPrice);
            stmt.setString(2, name);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Item price updated successfully.");
            } else {
                System.out.println("No such item found in the menu.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void viewTotalSales() {
        String sql = "SELECT SUM(total) AS total_sales FROM orders";
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

