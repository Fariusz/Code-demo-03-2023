package org.rloth_demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculatorTest {

    @Test
    void shouldThrowArithmeticExceptionWhenDividingByZero() throws Exception {
        File tempFile = File.createTempFile("divide_by_zero", null);
        tempFile.deleteOnExit();

        // Write test data to the temporary file
        FileWriter writer = new FileWriter(tempFile);
        writer.write("multiply 2\n");
        writer.write("subtract 4\n");
        writer.write("divide 0\n"); // divide by zero operation
        writer.write("apply 5\n");
        writer.close();

        Throwable exception = assertThrows(ArithmeticException.class, () -> {
            Calculator.calculate(tempFile.getAbsolutePath());
        });

        assertEquals("Error: Divide by zero.", exception.getMessage());
    }

    // Test for case-insensitive instruction keys
    @Test
    void shouldHandleCaseInsensitiveInstructionKeys() throws Exception {
        File tempFile = File.createTempFile("case_insensitive", "txt");
        tempFile.deleteOnExit();

        // Write instructions to file with mixed-case keys
        FileWriter writer = new FileWriter(tempFile);
        writer.write("add 2\n");
        writer.write("MULTIPLY 3\n");
        writer.write("aPpLy 10\n");
        writer.close();

        int expected = 36;
        int actual = Calculator.calculate(tempFile.getAbsolutePath());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldReturnExpectedResultForValidFileInput(@TempDir File tempDir) throws IOException, FileParsingException {
        // Create a text file in the temporary directory with some instructions
        File testFile = new File(tempDir, "test.txt");
        FileWriter writer = new FileWriter(testFile);
        writer.write("multiply 3\n");
        writer.write("add 2\n");
        writer.write("apply 10\n");
        writer.close();

        // Call the calculate method with the path to the test file
        Integer result = Calculator.calculate(testFile.getPath());

        // Assert that the returned Integer is the expected value
        assertEquals(Integer.valueOf(32), result);
    }

    @Test
    void shouldThrowFileParsingExceptionForInvalidNumberFormat() throws IOException {
        // create a temporary file with invalid number formats
        File tempFile = File.createTempFile("temp", "txt");
        tempFile.deleteOnExit();
        FileWriter writer = new FileWriter(tempFile);
        writer.write("apply five\nadd 5.1\nsubtract X\nmultiply 2,3");
        writer.close();

        // define the expected exception and call the calculate method with the temp file path
        Exception exception = assertThrows(FileParsingException.class, () ->
                Calculator.calculate(tempFile.getAbsolutePath()));

        // assert that the exception message contains the expected text
        String expectedMessage = "Error: Invalid number format.";
        String actualMessage = exception.getMessage();
        assert (actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldThrowFileParsingExceptionForInvalidFilePath() {
        // Call the calculate method with an invalid/non-existent file path and assert that a FileParsingException is thrown with the correct message
        FileParsingException exception = assertThrows(FileParsingException.class, () ->
                Calculator.calculate("invalid_path.txt"));
        assertEquals("Error: File not found.", exception.getMessage());
    }

    @Test
    void shouldThrowFileParsingExceptionForEmptyFileInput() throws Exception {
        File tempFile = File.createTempFile("empty", "txt");
        tempFile.deleteOnExit();

        FileParsingException exception = assertThrows(FileParsingException.class, () ->
                Calculator.calculate(tempFile.getAbsolutePath()));
        assertEquals("Error: No instructions found.", exception.getMessage());
    }
}


