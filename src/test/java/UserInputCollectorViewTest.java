import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class UserInputCollectorViewTest {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    private final UserInputCollectorView userInputCollectorView = new UserInputCollectorView();


    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    private void testSimulatedInputThrowingExceptions(String simulatedInput){
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        assertThrows(UserInputException.class, () -> {
            userInputCollectorView.collectUserInput();
        });
    }

    private void testSimulatedInputNotThrowingExceptions(String simulatedInput){
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        try {
            userInputCollectorView.collectUserInput();
        } catch (UserInputException e) {
            fail("No exception should be thrown for valid input.");
        }
    }

    @Test
    void testValidFileNameIsGiven(){
        String simulatedInput = "gdp --from-year 2020 --to-year 2022 --file output.csv\n";
        testSimulatedInputNotThrowingExceptions(simulatedInput);
    }

    @Test
    void testValidInputsNotThrowingExceptionsMultipleYears() {

        String simulatedInput = "gdp --from-year 2020 --to-year 2022\n";
        testSimulatedInputNotThrowingExceptions(simulatedInput);
    }

    @Test
    void testValidInputsNotThrowingExceptionsOneYear() {

        String simulatedInput = "gdp --from-year 2018 --to-year 2018\n";
        testSimulatedInputNotThrowingExceptions(simulatedInput);
    }


    @Test
    void testCollectUserInputWithMissingEndYear() {
        String simulatedInput = "gdp --from-year 2020 --to-year\n";
        testSimulatedInputThrowingExceptions(simulatedInput);
    }

    @Test
    void testCollectUserInputWithMissingStartYear() {

        String simulatedInput = "gdp --from-year --to-year 2022 ";
        testSimulatedInputThrowingExceptions(simulatedInput);
    }

    @Test
    void testCollectUserInputWithStartYearGreaterThanEndYear() {

        String simulatedInput = "gdp --from-year 2021 --to-year 2020 ";
        testSimulatedInputThrowingExceptions(simulatedInput);

    }

    @Test
    void testCollectUserInputWithNon4DigitStartYear() {

        String simulatedInput = "gdp --from-year 789 --to-year 2020 ";
        testSimulatedInputThrowingExceptions(simulatedInput);

    }

    @Test
    void testCollectUserInputWithNon4DigitEndYear() {

        String simulatedInput = "gdp --from-year 1999 --to-year 20202 ";
        testSimulatedInputThrowingExceptions(simulatedInput);

    }

    @Test
    void testCollectUserInputWithEndYearTooBig() {

        String simulatedInput = "gdp --from-year 2021 --to-year 2024\n";
        testSimulatedInputThrowingExceptions(simulatedInput);

    }


    @Test
    void testEmptyInput() {

        String simulatedInput = "\n";
        testSimulatedInputThrowingExceptions(simulatedInput);

    }

    @Test
    void testCollectUserInputNonNumericYear() {

        String simulatedInput = "gdp --from-year two thousand --to-year 2022\n";
        testSimulatedInputThrowingExceptions(simulatedInput);

    }

    @Test
    void testInputWithExtraSpace() {

        String simulatedInput = "gdp --from-year  2021 --to-year 2022\n";
        testSimulatedInputThrowingExceptions(simulatedInput);

    }

    @Test
    void testInputWithCapitalizedLetter() {

        String simulatedInput = "GDP --from-year 2021 --to-year 2022\n";
        testSimulatedInputThrowingExceptions(simulatedInput);

    }

}

