import java.util.*;

public class GdpConsolePresenter implements Presenter{


    @Override
    public void present(ResponseModel interactorResponse){
        Table table = interactorResponse.getTable();

        printHeaderRow(table);
        printCountryRows(table);

    }

    private void printHeaderRow(Table table){
        Row headerRow = table.getRows().get(0);
        System.out.print("Country,");
        printNumbersThroughoutYears(headerRow.getList());
    }

    private void printCountryRows(Table table){
        for (int i = 1; i < table.getRows().size(); i++){
            Row countryRow = table.getRows().get(i);
            String countryName = wrapCountryNameInQuotes(countryRow.getFirstString());
            System.out.print(countryName + ",");
            List<Double> percentagesThroughoutYears = countryRow.getList();
            printNumbersThroughoutYears(percentagesThroughoutYears);

        }
    }
    private String wrapCountryNameInQuotes(String string){
        return "\"" + string + "\"";
    }

    <T> void printNumbersThroughoutYears(List<T> numbersThroughoutYears){
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
                System.out.println(reformattedNumber);

            }
            else {
                System.out.print(reformattedNumber + ",");

            }
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

