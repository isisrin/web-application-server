package controller;

import model.HttpRequest;
import model.HttpResponse;

public interface Controller {
    static void service(HttpRequest httpRequest, HttpResponse httpResponse){}
}
