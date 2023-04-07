package org.rloth_demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Calculator {

    private Calculator() {
    }

    public static Integer calculate(String filePath) throws FileParsingException {
        return executeInstructions(readFileToMap(filePath));
    }

    private static Map<String, Integer> readFileToMap(String filePath) throws FileParsingException {

        Map<String, Integer> instructionsMap = new LinkedHashMap<>();

        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine()
                        .toUpperCase()
                        .split(" ");

                if (parts.length == 2) {
                    instructionsMap.put(parts[0], Integer.parseInt(parts[1]));
                }
            }
        } catch (NumberFormatException e) {
            throw new FileParsingException("Error: Invalid number format.");
        } catch (FileNotFoundException e) {
            throw new FileParsingException("Error: File not found.");
        } catch (Exception e) {
            throw new FileParsingException("Error: Something went wrong.");
        }
        return instructionsMap;
    }

    private static Integer executeInstructions(Map<String, Integer> instructionsMap) throws FileParsingException {
        
        Integer number = instructionsMap.get("APPLY");

        if (number == null) {
            throw new FileParsingException("Error: No instructions found.");
        }

        for (var entry : instructionsMap.entrySet()) {

            switch (Instruction.valueOf(entry.getKey())) {
                case ADD:
                    number += entry.getValue();
                    break;
                case MULTIPLY:
                    number *= entry.getValue();
                    break;
                case SUBTRACT:
                    number -= entry.getValue();
                    break;
                case DIVIDE:
                    if (entry.getValue() != 0) {
                        number /= entry.getValue();
                    } else {
                        throw new ArithmeticException("Error: Divide by zero.");
                    }
                    break;
                case APPLY:
                    break;
            }
        }
        return number;
    }
}
