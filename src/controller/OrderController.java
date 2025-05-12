package controller;

import model.DBConnection;
import model.User;
import utils.MenuItem;
import utils.DSAUtils;

import java.sql.*;
import java.util.*;

public class OrderController {
    private List<MenuItem> menu = new LinkedList<>();
    private HashMap<String, Integer> cart = new HashMap<>();
    private Stack<String> orderStack = new Stack<>();
    private LinkedList<String> pastOrderList = new LinkedList<>();
    private Scanner sc = new Scanner(System.in);

    public OrderController() {
        String sql = "SELECT name, price FROM food_items"; // SQL query to fetch menu items
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String name = rs.getString("name");
                double price = rs.getDouble("price");

                // Create MenuItem object and add it to the LinkedList
                menu.add(new MenuItem(name, price));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching menu from database: " + e.getMessage());
        }
    }

    public void viewMenu() {
        DSAUtils.sortMenu(menu);
        while (true) {
            System.out.println("---- Menu (Sorted by Price) ----");
            for (MenuItem item : menu) {
                System.out.println(item.name + " - ₹" + item.price);
            }

            System.out.print("Add item to cart? (name of the item or 'no' to exit): ");
            String choice = sc.nextLine();
            if (choice.equalsIgnoreCase("no")) {
                break;
            }

            boolean itemFound = false;
            for (MenuItem item : menu) {
                if (item.name.equalsIgnoreCase(choice)) {
                    System.out.print("Enter quantity: ");
                    int qty;
                    try {
                        qty = sc.nextInt();
                        sc.nextLine();
                        if (qty <= 0) {
                            System.out.println("Quantity must be at least 1.");
                            break;
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid quantity. Please enter a number.");
                        sc.nextLine(); // clear invalid input
                        break;
                    }

                    cart.put(item.name, cart.getOrDefault(item.name, 0) + qty);

                    // Push item to orderStack `qty` times
                    for (int i = 0; i < qty; i++) {
                        orderStack.push(item.name);
                    }

                    System.out.println(qty + " x " + item.name + " added to cart.");
                    itemFound = true;
                    break;
                }
            }

            if (!itemFound) {
                System.out.println("Incorrect item name, re-enter item name.");
            }
        }
    }

    public void viewOrder() {
        double totalAmount = 0;  // To store the total bill amount

        System.out.println("---- Current Order ----");

        // Iterate through the cart to display items and calculate the total
        for (Map.Entry<String, Integer> entry : cart.entrySet()) { // {pasta : 3}
            String itemName = entry.getKey();
            int quantity = entry.getValue();

            // Get the price of the item from the menu
            double itemPrice = getPrice(itemName);  // Helper function to get price of item from menu

            // Calculate the cost for this item (quantity * price)
            double itemTotal = itemPrice * quantity;
            totalAmount += itemTotal;  // Add to the total bill

            // Display item name, quantity, and price
            System.out.println(itemName + " x " + quantity + " = ₹" + itemTotal);
        }

        // Display total amount of the bill
        System.out.println("\nTotal Amount: ₹" + totalAmount);
    }

    public void checkout(User user) throws SQLException {
        if (cart.isEmpty()) {
            System.out.println("No items to checkout.");
            return;
        }
        System.out.print("Enter your address: ");
        String address = sc.nextLine();

        // Build string in the format item-qty
        StringBuilder items = new StringBuilder();
        int totalAmount = 0;
        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            items.append(entry.getKey()).append("-").append(entry.getValue()).append(", ");
        }
        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            String itemName = entry.getKey();
            int quantity = entry.getValue();

            // Get the price of the item from the menu
            double itemPrice = getPrice(itemName);  // Helper function to get price of item from menu

            // Calculate the cost for this item (quantity * price)
            double itemTotal = itemPrice * quantity;
            totalAmount += itemTotal;  // Add to the total bill

        }

        // Remove trailing comma and space
        if (items.length() > 0) {
            items.setLength(items.length() - 2);
        }

        pastOrderList.add("Items: " + items.toString() + " Address: " + address);

        // Print the bill
        printBill(user, address);

        // Save to DB
        Connection conn = DBConnection.getConnection();
        String sql = "INSERT INTO orders (user_id, items, address, total) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, user.id);
        stmt.setString(2, items.toString());
        stmt.setString(3, address);
        stmt.setInt(4, totalAmount);
        stmt.executeUpdate();
        conn.close();

        cart.clear();
        orderStack.clear();
        System.out.println("Your order will be shortly delivered at " + address);
    }

    public void printBill(User user, String address) {
        String line = "╔" + "═".repeat(60) + "╗";
        String mid = "╟" + "─".repeat(60) + "╢";
        String bottom = "╚" + "═".repeat(60) + "╝";

        System.out.println(line);
        System.out.printf("║ %-58s ║\n", "          QuickByte - INVOICE");
        System.out.println(mid);
        System.out.printf("║ %-58s ║\n", "User: " + user.username + " (ID: " + user.id + ")");
        System.out.printf("║ %-58s ║\n", "Address: " + address);
        System.out.println(mid);
        System.out.printf("║ %-25s %-10s %-10s %-10s ║\n", "Item", "Qty", "Price", "Total");
        System.out.println("╟" + "─".repeat(60) + "╢");

        double grandTotal = 0;
        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            String item = entry.getKey();
            int qty = entry.getValue();
            double price = getPrice(item); // Assume a method to get price of item
            double total = qty * price;
            grandTotal += total;

            System.out.printf("║ %-25s %-10d %-10.2f %-10.2f ║\n", item, qty, price, total);
        }

        System.out.println(mid);
        System.out.printf("║ %-46s %-10.2f  ║\n", "Grand Total:", grandTotal);
        System.out.println(bottom);
        System.out.println("        Thank you for shopping with QuickByte!");
    }

    public double getPrice(String item) {
        for (MenuItem menuItem : menu) {
            if (menuItem.name.equalsIgnoreCase(item)) {
                return menuItem.price;
            }
        }
        return -1; // Item not found
    }


    public void pastOrders(User user) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM orders WHERE user_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, user.id);
        ResultSet rs = stmt.executeQuery();

        System.out.println("---- Past Orders ----");
        while (rs.next()) {
            System.out.println("Items: " + rs.getString("items") + "\nDelivered at: " + rs.getString("address") + " for the total of: " + rs.getInt("total") + " at: " + rs.getString("order_time"));
        }
        conn.close();
    }
}
