import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GdpFilePresenterTest {
    @Test
    void testThatGdpResponseIsPrintedToExampleFiel() {
        //TODO
        // 1. Create fake very simple response model (2 years, 2 countries)
        // 2. Call present with fake model
        // 3. Read example.txt to check for expected very simple result (2 years, 2 countries)

        Table table = new Table();
        Row headerRow = new HeaderRow("Country", new ArrayList<>(List.of("2000", "2001")));
        Row countryRow1 = new CountryRow("United, States", new ArrayList<>(List.of(0.001, 0.002)));
        Row countryRow2 = new CountryRow("Canada", new ArrayList<>(List.of(0.003, 0.004)));

        table.addRow(headerRow);
        table.addRow(countryRow1);
        table.addRow(countryRow2);
        ResponseModel response = new ResponseModel(table);

        Presenter presenter = new GdpFilePresenter();
        presenter.present(response);

        String expectedOutput = "Country,2000,2001" + System.lineSeparator() + "\"United, States\",0.1,0.2" + System.lineSeparator()
                + "\"Canada\",0.3,0.4" + System.lineSeparator();

        String filePath = "example.txt";
        StringBuilder actual = new StringBuilder();


        try {
            // Create a FileReader and wrap it in a BufferedReader for efficient reading
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;

            // Read lines from the file until the end is reached (null is returned)
            while ((line = bufferedReader.readLine()) != null) {
                actual.append(line + System.lineSeparator());
            }

            // Close the BufferedReader and FileReader when done
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(expectedOutput, actual.toString());

    }


}
