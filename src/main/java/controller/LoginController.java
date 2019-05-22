package controller;

import db.DataBase;
import model.HttpRequest;
import model.HttpResponse;
import model.User;

public class LoginController extends AbstractController {

    @Override
    public void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        User savedUser = DataBase.findUserById(httpRequest.getHeader("userId"));
        if(savedUser != null) {
            httpResponse.addHeader("Set-Cookie", "logined=true");
            httpResponse.sendRedirect("/index.html");
            return;
        }
        httpResponse.sendRedirect("/user/login_failed.html");
    }
}
