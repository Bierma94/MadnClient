package de.bierma.model;

/**
 * View
 *
 * @author Jannes Bierma
 * @version 1.0 - 17.12.2024
 */
public class View {

    private static View instance;

    private View() {
    }

    public static View getInstance() {
        if (instance == null) {
            instance = new View();
        }
        return instance;
    }

    public void print(String message) {
        System.out.println(message);
    }

}
