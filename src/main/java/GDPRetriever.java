import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;

public class GDPRetriever implements ApiInteractor {

    final int NUMBER_OF_COUNTRIES = 400;
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, String> countrycodeToCountry = new HashMap<>();
    List<String> countryCodes;
    Map<String, Map<String, Double>> countrycodeToYearToPercentage = new HashMap<>();
    List<Pair> countrycodeToHighestYearPercentage = new ArrayList<>();
    int lowestYear;
    int highestYear;
    double[] worldGdpThroughoutYears;
    DataGateway dataGateway;
    Presenter presenter;


    public GDPRetriever(DataGateway dataGatewayArg){
        dataGateway = dataGatewayArg;
    }


    @Override
    public void execute(RequestModel requestModel) {

        try {
            lowestYear = requestModel.getLowestYear();
            highestYear = requestModel.getHighestYear();

            countryCodes = getCountryCodes();

            worldGdpThroughoutYears = getWorldGdpThroughoutYears();

            getAllCountriesGdpThroughoutYears();

            TableBuilder tableBuilder = new TableBuilder(lowestYear, highestYear, countrycodeToHighestYearPercentage,
                    countrycodeToCountry, countrycodeToYearToPercentage);

            Table table = tableBuilder.buildTable();


            ResponseModel response = new ResponseModel(table);
            Presenter presenter = createPresenter(requestModel.getFileName());
            presenter.present(response);


        } catch (Exception e) {
            String errorMessage = "An error occurred while processing your request. Please try again later.";
            presenter.showError(errorMessage);
        }
    }

    public Presenter createPresenter(String fileName){
        if (fileName == null){
            return new GdpConsolePresenter();
        }
        else{
            return new GdpFilePresenter();
        }
    }

    private double[] getWorldGdpThroughoutYears() throws Exception {
        String response = dataGateway.sendRequest(
                "https://api.worldbank.org/v2/country/WLD/indicator/NY.GDP.MKTP.CD?format=json&date="
                + lowestYear + ":" + highestYear);

        JsonNode dataByYear = extractDataNodeFromResponse(response);
        double[] worldGdpThroughoutYears = new double[highestYear - lowestYear + 1];
        int i = 0;
        if (dataByYear != null){
            for (JsonNode item : dataByYear) {
                worldGdpThroughoutYears[i] = item.get("value").asDouble();
                i++;
            }
        }
        return worldGdpThroughoutYears;

    }

    private JsonNode extractDataNodeFromResponse(String response) throws JsonProcessingException {
        JsonNode responseNode = objectMapper.readTree(response);
        return responseNode.get(1);
    }

    private void getAllCountriesGdpThroughoutYears() throws Exception {
        for (String countryCode : countryCodes){
            getCountryGdpThroughoutYears(countryCode);
        }
    }

    private void getCountryGdpThroughoutYears(String countryCode) throws Exception {
        String response = dataGateway.sendRequest("https://api.worldbank.org/v2/country/"
                + countryCode + "/indicator/NY.GDP.MKTP.CD?format=json&date="
                + lowestYear + ":" + highestYear);

        JsonNode dataByYear = extractDataNodeFromResponse(response);

        if (dataByYear != null){
            for (JsonNode item : dataByYear) {
                double itemGdpValue = item.get("value").asDouble();
                String itemYear = item.get("date").asText();

                int worldGdpIndex = highestYear - Integer.parseInt(itemYear);

                double itemGdpVsWorldGdp = itemGdpValue / worldGdpThroughoutYears[worldGdpIndex];

                if (countrycodeToYearToPercentage.get(countryCode) == null){
                    Map<String, Double> yearToPercentage = new HashMap<>();
                    yearToPercentage.put(itemYear, itemGdpVsWorldGdp);
                    countrycodeToYearToPercentage.put(countryCode, yearToPercentage);
                }
                else{
                    countrycodeToYearToPercentage.get(countryCode).put(itemYear, itemGdpVsWorldGdp);
                }

                if (Integer.parseInt(itemYear) == highestYear){
                countrycodeToHighestYearPercentage.add(new Pair(countryCode, itemGdpVsWorldGdp));
                }

            }
        }

    }

    private List<String> getCountryCodes() throws Exception {
        String getCountryListUrl = "https://api.worldbank.org/v2/country?format=json&per_page=" + NUMBER_OF_COUNTRIES;
        String countryList = dataGateway.sendRequest(getCountryListUrl);

        return extractCountryCodes(countryList);
    }

    private List<String> extractCountryCodes(String countryListResponse) throws Exception {
        JsonNode countriesNode = extractDataNodeFromResponse(countryListResponse);

        List<String> countryCodes = new ArrayList<>();
        for (JsonNode countryNode : countriesNode) {
            String countryCode = countryNode.get("id").asText();
            boolean isRegion = Objects.equals(countryNode.get("region").get("id").asText(), "NA");
            if (!isRegion){
                countryCodes.add(countryCode);
            }

            String countryName = (countryNode.get("name").asText());
            countrycodeToCountry.put(countryCode,countryName);
        }
        return countryCodes;
    }



}


class Pair implements Comparable<Pair> {
    String countryCode;
    Double latestYearPercentage;

    public Pair(String countryCode, Double latestYearPercentage){
        this.countryCode = countryCode;
        this.latestYearPercentage = latestYearPercentage;
    };

    @Override
    public int compareTo(Pair other) {
        return Double.compare(other.latestYearPercentage, this.latestYearPercentage);
    }
}







