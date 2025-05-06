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
                        System.out.println("\n1. View Menu\n2. View Order\n3. Checkout\n4. Past Orders\n5. Logout");
                        int ch = sc.nextInt();

                        switch (ch) {
                            case 1 -> orderController.viewMenu();
                            case 2 -> orderController.viewOrder();
                            case 3 -> orderController.checkout(user);
                            case 4 -> orderController.pastOrders(user);
                            case 5 -> {
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
