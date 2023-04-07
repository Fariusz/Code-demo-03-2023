package org.rloth_demo;

import java.util.Scanner;

import static java.lang.System.exit;
import static org.rloth_demo.Calculator.calculate;

public class Main {

    public static void main(String[] args) {
        try {
            System.out.println(calculate(getFilePathFromUserInput()));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            exit(1);
        }
    }

    private static String getFilePathFromUserInput() {
        System.out.print("Provide file path: ");
        return new Scanner(System.in).nextLine();
    }
}

