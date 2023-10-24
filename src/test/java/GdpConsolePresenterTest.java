import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class GdpConsolePresenterTest {

    private GdpConsolePresenter presenter;
    Table table = new Table();


    @BeforeEach
    public void setUp() {
        presenter = new GdpConsolePresenter();

        Row headerRow = new HeaderRow("Country", new ArrayList<>(List.of("2000", "2001", "2002")));
        Row countryRow1 = new CountryRow("United, States", new ArrayList<>(List.of(0.35211, 0.4124, 0.7231)));
        Row countryRow2 = new CountryRow("Canada", new ArrayList<>(List.of(0.2524, 0.53936, 0.344255)));

        table.addRow(headerRow);
        table.addRow(countryRow1);
        table.addRow(countryRow2);


    }

    @Test
    public void testPresenterPrintsRowsCorrectly() {
        String expectedOutput = "Country,2000,2001,2002\n" + "\"United, States\",35.21,41.24,72.31\n" + "\"Canada\",25.24,53.94,34.43\n";
        String actualOutput = captureOutput(() -> presenter.present(new ResponseModel(table)));


        expectedOutput = expectedOutput.replace("\r\n", "\n");
        actualOutput = actualOutput.replace("\r\n", "\n");

        assertEquals(expectedOutput, actualOutput);

    }


    private String captureOutput(Runnable runnable) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        runnable.run();
        System.setOut(System.out);
        return outputStream.toString();
    }
}
