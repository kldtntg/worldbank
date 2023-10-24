import java.time.Year;
import java.util.Scanner;

class UserInputCollectorView {
    public RequestModel collectUserInput() throws UserInputException {

        Scanner scanner = new Scanner(System.in);

        while (true) {

            try {
                int lowestYear;
                int highestYear;
                String fileName;
                int currentYear = Year.now().getValue();

                System.out.println("Enter the command (e.g., gdp --from-year 2020 --to-year 2022 --file output.csv): ");
                String command = scanner.nextLine();

                if (command.matches("gdp --from-year \\d{4} --to-year \\d{4} --file \\{\".*\"}")) {
                    String[] tokens = command.split(" ");
                    lowestYear = Integer.parseInt(tokens[2]);
                    highestYear = Integer.parseInt(tokens[4]);
                    fileName = tokens[6];

                    if (highestYear >= currentYear || lowestYear >= currentYear) {
                        throw new UserInputException("from-year and to-year should not be greater than or equal to the current year.");
                    } else if (lowestYear <= highestYear) {
                        return new RequestModel(lowestYear, highestYear, fileName);
                    } else {
                        throw new UserInputException("The start year should be less than or equal to the end year.");
                    }

                } else {
                    throw new UserInputException("Invalid command format or year (that is not 4-digit). Please use: gdp --from-year <startYear> --to-year <endYear>");
                }

            } catch (UserInputException e) {
                System.err.println(e.getMessage());
                throw e;
            }
        }
    }
}

class UserInputException extends Exception {
    public UserInputException(String message) {
        super(message);
    }
}