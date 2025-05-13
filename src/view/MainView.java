package view;

import controller.AuthController;
import controller.OrderController;
import model.User;

import java.util.Scanner;

public class MainView {
    public void start() {
        Scanner sc = new Scanner(System.in);
        AuthController auth = new AuthController();
        User user = null;

        while (true) {
            System.out.println("\n1. Register\n2. Login\nChoose: ");
            int choice = sc.nextInt();

            try {
                if (choice == 1) user = auth.register();
                else if (choice == 2) user = auth.login();

                if (user != null) {
                    OrderController orderController = new OrderController();
                    while (true) {
                        System.out.println("\n1. View Menu\n2. Remove item from cart\n3. View Order\n4. Checkout\n5. Past Orders\n6. Logout");
                        int ch = sc.nextInt();

                        switch (ch) {
                            case 1 -> orderController.viewMenu();
                            case 2 -> orderController.removeFromCart();
                            case 3 -> orderController.viewOrder();
                            case 4 -> orderController.checkout(user);
                            case 5 -> orderController.pastOrders(user);
                            case 6 -> {
                                user = null;
                                System.out.println("Logged out.");
                                break;
                            }
                        }
                        if (user == null) break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
