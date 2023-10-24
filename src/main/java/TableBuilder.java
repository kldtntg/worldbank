import java.util.*;

public class TableBuilder{
    int lowestYear;
    int highestYear;
    Map<String, String> countrycodeToCountry;
    Map<String, Map<String, Double>> countrycodeToYearToPercentage;
    List<Pair> countrycodeToHighestYearPercentage;


    public TableBuilder(int lowestYear, int highestYear,
                        List<Pair> countrycodeToHighestYearPercentage,
                        Map<String, String> countrycodeToCountry,
                        Map<String, Map<String, Double>> countrycodeToYearToPercentage){

        this.lowestYear = lowestYear;
        this.highestYear = highestYear;
        this.countrycodeToHighestYearPercentage = countrycodeToHighestYearPercentage;
        this.countrycodeToCountry = countrycodeToCountry;
        this.countrycodeToYearToPercentage = countrycodeToYearToPercentage;
    }

    public Table buildTable(){

        Table table = new Table();
        table.addRow(createHeaderRow());

        Collections.sort(countrycodeToHighestYearPercentage);

        for (Pair pair: countrycodeToHighestYearPercentage){

            String countryCode = pair.countryCode;

            String countryName = countrycodeToCountry.get(countryCode);
            Map<String, Double> yearToPercentage = countrycodeToYearToPercentage.get(countryCode);

            table.addRow(createCountryRow(countryName, yearToPercentage));
        }

        return table;
    }


    private Row createHeaderRow(){

        List<String> years = new ArrayList<>();
        for (int year = lowestYear; year <= highestYear; year++){
            years.add(year + "");
        }

        return new HeaderRow("Country", years);
    }


    private Row createCountryRow(String countryName, Map<String, Double> yearToPercentage ){

        List<Double> percentagesThroughoutYears = new ArrayList<>();

        for (int year = lowestYear; year <= highestYear; year++){
            percentagesThroughoutYears.add(yearToPercentage.get(String.valueOf(year)));
        }

        return new CountryRow(countryName, percentagesThroughoutYears );

    }


}

class Table {
    private List<Row> rows;

    public Table(){
        this.rows = new ArrayList<>();
    }

    public void addRow(Row row) {
        rows.add(row);
    }

    public List<Row> getRows() {
        return rows;
    }
}

abstract class Row<T>{
    public abstract String getFirstString();

    public abstract List<T> getList();
}

class HeaderRow extends Row{
    private final String countryHeader;
    private final List<String> yearsHeader;

    public HeaderRow(String countryHeader, List<String> yearsHeader){
        this.countryHeader = countryHeader;
        this.yearsHeader = yearsHeader;
    }

    @Override
    public String getFirstString() {
        return countryHeader;
    }

    @Override
    public List<String> getList() {
        return yearsHeader;
    }
}
class CountryRow extends Row {
    private final String countryName;
    private final List<Double> percentagesThroughoutYears;

    public CountryRow(String countryName, List<Double> percentagesThroughoutYears){
        this.countryName = countryName;
        this.percentagesThroughoutYears = percentagesThroughoutYears;
    }

    @Override
    public String getFirstString() {
        return countryName;
    }

    @Override
    public List<Double> getList() {
        return percentagesThroughoutYears;
    }
}