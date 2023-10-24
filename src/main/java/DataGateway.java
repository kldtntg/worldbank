import java.io.IOException;

interface DataGateway {
    String sendRequest(String apiUrl) throws IOException;
}