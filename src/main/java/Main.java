public class Main {
    public static void main(String[] args) {


        UserInputCollectorView userInputCollectorView = new UserInputCollectorView();
        DataGateway dataGateway = new ApiFetcher();
//        Presenter presenter = new GdpConsolePresenter();
        ApiInteractor GDPRetriever = new GDPRetriever(dataGateway);
        ApiController controller = new ApiController(GDPRetriever);

        try {
            RequestModel userInput = userInputCollectorView.collectUserInput();
            controller.handleRequest(userInput);

        } catch (UserInputException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}


