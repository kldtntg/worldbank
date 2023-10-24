import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ApiFetcher implements DataGateway{


    @Override
    public String sendRequest(String apiUrl) throws IOException {
        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;

                    while ((inputLine = reader.readLine()) != null) {
                        response.append(inputLine);
                    }
                }
            } else {
                throw new IOException("HTTP request failed with status code: " + responseCode);
            }
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Malformed URL: " + apiUrl, e);

        } catch (IOException e) {
            throw new IOException("Error during HTTP request: " + e.getMessage(), e);
        }

        return response.toString();
    }
}
