package de.bierma.model;

import java.util.Scanner;

/**
 * Input
 *
 * @author Jannes Bierma
 * @version 1.0 - 17.12.2024
 */
public class Input {

    private static Input instance;
    private Scanner scanner;

    private Input() {
        scanner = new Scanner(System.in);
    }

    public static Input getInstance() {
        if (instance == null) {
            instance = new Input();
        }
        return instance;
    }

    public String readLine() {
        return scanner.nextLine();
    }

}
