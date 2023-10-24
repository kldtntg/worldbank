import java.util.*;
import java.io.FileWriter;
import java.io.IOException;

public class GdpFilePresenter implements Presenter{
    StringBuilder content = new StringBuilder();


    @Override
    public void present(ResponseModel interactorResponse){
        Table table = interactorResponse.getTable();

        printHeaderRow(table);
        printCountryRows(table);

    }

    private void printHeaderRow(Table table){
        Row headerRow = table.getRows().get(0);
        content.append("Country,");
        printNumbersThroughoutYearsToFile(headerRow.getList());
    }

    private void printCountryRows(Table table){
        for (int i = 1; i < table.getRows().size(); i++){
            Row countryRow = table.getRows().get(i);
            String countryName = wrapCountryNameInQuotes(countryRow.getFirstString());
            content.append(countryName + ",");
            List<Double> percentagesThroughoutYears = countryRow.getList();
            printNumbersThroughoutYearsToFile(percentagesThroughoutYears);
        }
    }
    private String wrapCountryNameInQuotes(String string){
        return "\"" + string + "\"";
    }

    private <T> void printNumbersThroughoutYearsToFile(List<T> numbersThroughoutYears){




        try {
            for (int i = 0; i < numbersThroughoutYears.size(); i++){

                T number = numbersThroughoutYears.get(i);
                String reformattedNumber;
                if (number instanceof Number){
                    reformattedNumber = reformatNumber((Number) number);
                }
                else {
                    reformattedNumber = (String) number;
                }

                if (i == numbersThroughoutYears.size() - 1){
                    content.append(reformattedNumber);

                }
                else {
                    content.append(reformattedNumber + ",");

                }

            }
            content.append(System.lineSeparator());


            String filePath = "example.txt";

            // Create a FileWriter instance with the file path
            FileWriter writer = new FileWriter(filePath);

            // Write the content to the file
            writer.write(content.toString());

            // Close the FileWriter to ensure that the changes are saved
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String reformatNumber(Number number){
        return "" + Math.round(number.doubleValue() * 10000.0) / 100.0 ;
    }

    @Override
    public void showError(String errorMessage){
        System.err.println(errorMessage);
    }

}

