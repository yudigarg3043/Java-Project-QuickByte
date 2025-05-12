package utils;

import java.util.List;

public class DSAUtils {
    //Sorting Menu based on Price
    public static void sortMenu(List<MenuItem> items) {
        // Simple bubble sort
        for (int i = 0; i < items.size() - 1; i++) {
            for (int j = 0; j < items.size() - i - 1; j++) {
                if (items.get(j).price > items.get(j + 1).price) {
                    MenuItem temp = items.get(j);
                    items.set(j, items.get(j + 1));
                    items.set(j + 1, temp);
                }
            }
        }
    }
}
