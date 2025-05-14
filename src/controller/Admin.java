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
            System.out.println("2. View All Registered User");
            System.out.println("3. Add item to menu");
            System.out.println("4. Remove item from menu");
            System.out.println("5. Update item price in menu");
            System.out.println("6. View total sales");
            System.out.println("7. Exit");

            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // Clearing Buffer Created by nextInt()

            switch (choice) {
                case 1:
                    viewMenu();
                    break;
                case 2:
                    viewAllRegisteredUsers();
                    break;
                case 3:
                    addItemToMenu();
                    break;
                case 4:
                    removeItemFromMenu();
                    break;
                case 5:
                    updateItemPrice();
                    break;
                case 6:
                    viewTotalSales();
                    break;
                case 7:
                    System.out.println("Exiting admin menu.");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    //View Menu
    public void viewMenu(){
        String sql = "SELECT name, price FROM food_items"; // SQL query to fetch menu items
        List<MenuItem> menu = new LinkedList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) { //Returns ResultSet

            while (rs.next()) {
                String name = rs.getString("name");
                double price = rs.getDouble("price");

                // Create MenuItem object and add it to the LinkedList
                menu.add(new MenuItem(name, price));
            }

            DSAUtils.sortMenu(menu); //Sorting Menu Based on price using bubble sort
            System.out.println("---- Menu (Sorted by Price) ----");
            for (MenuItem item : menu) {
                System.out.println(item.name + " - ₹" + item.price);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching menu from database: " + e.getMessage());
        }
    }

    //View All RegisteredUser
    private void viewAllRegisteredUsers() {
        String sql = "SELECT id, username, email FROM users";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("List of Registered Users:");
            System.out.println("---------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String email = rs.getString("email");

                if(username.equals("Admin")) continue;

                System.out.println("ID: " + id);
                System.out.println("Name: " + username);
                System.out.println("Email: " + email);
                System.out.println("---------------------------------------------");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    //Add Items in Menu
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
            int rows = stmt.executeUpdate(); //Return Int
            if (rows > 0) {
                System.out.println("Item added successfully.");
            } else {
                System.out.println("Failed to add item.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Remove Items from Menu
    private void removeItemFromMenu() {
        System.out.println("---- Existing Menu ----");
        viewMenu();
        System.out.print("Enter item name to remove: ");
        String name = sc.nextLine();

        String sql = "DELETE FROM food_items WHERE name = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            int rows = stmt.executeUpdate(); //Return Int

            if (rows > 0) {
                System.out.println("Item removed successfully.");
            } else {
                System.out.println("No such item found in the menu.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Update Item Price in Menu
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
            int rows = stmt.executeUpdate(); //Return Int

            if (rows > 0) {
                System.out.println("Item price updated successfully.");
            } else {
                System.out.println("No such item found in the menu.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Calculating and giving Total Sale as Output
    private void viewTotalSales() {
        String sql = "SELECT SUM(total) AS total_sales FROM orders";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) { //Returns ResultSet

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

