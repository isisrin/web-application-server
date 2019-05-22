package controller;

import lombok.SneakyThrows;
import model.HttpMethod;
import model.HttpRequest;
import model.HttpResponse;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractController implements Controller {
    private static final CreateUserController createUserController = new CreateUserController();
    private static final ListUserController listUserController = new ListUserController();
    private static final LoginController loginController = new LoginController();

    private static final Map<String, AbstractController> urlMatcher = new HashMap<>();

    public void doPost(HttpRequest httpRequest, HttpResponse httpResponse){}
    public void doGet(HttpRequest httpRequest, HttpResponse httpResponse){}

    // TODO : 어노테이션으로 추출할 수 있는지 확인
    static {
        urlMatcher.put("/user/create", createUserController);
        urlMatcher.put("/user/list", listUserController);
        urlMatcher.put("/user/login", loginController);
    }

    @SneakyThrows
    public static void service(HttpRequest httpRequest, HttpResponse httpResponse) {
        AbstractController matchedController = getController(httpRequest);
        if(matchedController == null) {
            httpResponse.forward(httpRequest.getPath());
            return;
        }
        if(httpRequest.getMethod().equals(HttpMethod.POST)) {
            matchedController.doPost(httpRequest, httpResponse);
            return;
        }
        if(httpRequest.getMethod().equals(HttpMethod.GET)) {
            matchedController.doGet(httpRequest, httpResponse);
            return;
        }
    }

    private static AbstractController getController(HttpRequest httpRequest) {
        for (String key : urlMatcher.keySet()) {
            if(key.equals(httpRequest.getPath()))  {
                return urlMatcher.get(key);
            }
        }
        return null;
    }
}
