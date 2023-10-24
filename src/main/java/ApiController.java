public class ApiController {
    ApiInteractor useCase;

    public ApiController(ApiInteractor useCase){
        this.useCase = useCase;
    }

    public void handleRequest(RequestModel userInput) {

        useCase.execute(userInput);

    }


}
