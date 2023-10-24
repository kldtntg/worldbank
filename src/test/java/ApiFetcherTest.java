import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ApiFetcherTest {

    private final ApiFetcher apiFetcher = new ApiFetcher();
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testSendGetRequestWithMalformedURL() {

        String malformedUrl = "invalid-url";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            apiFetcher.sendRequest(malformedUrl);
        });

        assertEquals(exception.getMessage(), "Malformed URL: " + malformedUrl);
    }

    @Test
    void testSendGetRequestWithIOException() {

        String invalidUrl = "https://bruh-bruh.com/non-existent-endpoint";

        IOException exception = assertThrows(IOException.class, () -> {
            apiFetcher.sendRequest(invalidUrl);
        });

        assertEquals(exception.getMessage(), "Error during HTTP request: " + "bruh-bruh.com");
    }

    @Test
    void testSendGetRequestReturnsWorldGDPFor1Year() throws IOException {
        String OneYearUrl = "https://api.worldbank.org/v2/country/WLD/indicator/NY.GDP.MKTP.CD?format=json&date=2000:2000";
        JsonNode dataByYear = sendRequestandReadDataNode(OneYearUrl);
        assertEquals(dataByYear.size(), 1);
    }

    @Test
    void testSendGetRequestReturnsWorldGDPFor3Year() throws IOException {
        String OneYearUrl = "https://api.worldbank.org/v2/country/WLD/indicator/NY.GDP.MKTP.CD?format=json&date=2000:2002";
        JsonNode dataByYear = sendRequestandReadDataNode(OneYearUrl);
        assertEquals(dataByYear.size(), 3);
    }

    @Test
    void testYearOrderDescendingInResponse() throws IOException {
        String OneYearUrl = "https://api.worldbank.org/v2/country/WLD/indicator/NY.GDP.MKTP.CD?format=json&date=2000:2001";
        JsonNode dataByYear = sendRequestandReadDataNode(OneYearUrl);
        int firstYear = dataByYear.get(0).get("date").asInt();
        int secondYear = dataByYear.get(1).get("date").asInt();
        assertTrue(firstYear > secondYear);
    }

    @Test
    void testThisIsCorrectWayToGetCountryName() throws IOException {
        String getCountryListUrl = "https://api.worldbank.org/v2/country?format=json&per_page=400";
        JsonNode countriesNode = sendRequestandReadDataNode(getCountryListUrl);
        assertTrue(countriesNode.get(0).get("name") != null);

    }

    @Test
    void testThisCorrectlyQueriesGdp() throws IOException {
        String getGdp = "https://api.worldbank.org/v2/country/CAN/indicator/NY.GDP.MKTP.CD?format=json&date=2022";
        JsonNode dataNode = sendRequestandReadDataNode(getGdp);
        assertTrue(dataNode.get(0).get("value") != null);
    }


    private JsonNode sendRequestandReadDataNode(String url) throws IOException {
        String countryList = apiFetcher.sendRequest(url);
        JsonNode responseNode = objectMapper.readTree(countryList);
        JsonNode dataNode = responseNode.get(1);
        return dataNode;
    }

}
