package controller;

import db.DataBase;
import model.HttpRequest;
import model.HttpResponse;
import model.User;

public class CreateUserController extends AbstractController {

    @Override
    public void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        DataBase.addUser(new User(httpRequest.getHeader("userId"), 
                                    httpRequest.getHeader("password"),
                                    httpRequest.getHeader("name"),
                                    httpRequest.getHeader("email")));
        httpResponse.sendRedirect("/index.html");
    }
}
