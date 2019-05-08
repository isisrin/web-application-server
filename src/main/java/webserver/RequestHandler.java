package webserver;

import db.DataBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HtmlUtils;
import util.HttpRequestUtils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final String INDEX_HTML = "/index.html";

    private Socket connection;

    RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            DataOutputStream dos = new DataOutputStream(out);
            response(in, dos);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response(InputStream inputStream, DataOutputStream dos) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String firstLine = HtmlUtils.getFirstLine(bufferedReader);

        if(firstLine.contains("POST")) {
            if(firstLine.contains("/user/login")) {
                String cookie = HtmlUtils.getCookie(bufferedReader);
                Map<String, String> cookies = HttpRequestUtils.parseCookies(cookie);
                if(Boolean.parseBoolean(cookies.get("Cookie: logined"))) {
                    responseDynamicHtml(dos, HtmlUtils.generateHtmlTable());
                    return;
                }
                response302Header(dos, "/user/form.html", false);
                return;
            }
            if(firstLine.contains("/user/create")) {
                DataBase.addUser(HtmlUtils.joinUser(HtmlUtils.getBody(bufferedReader)));
                response302Header(dos, INDEX_HTML, true);
                return;
            }
        }

        if(firstLine.contains("GET")) {
            if(firstLine.contains("?")) {
                DataBase.addUser(HtmlUtils.joinUser(HtmlUtils.getQueryString(firstLine)));
                getResponse(dos, INDEX_HTML);
                return;
            }
            if(firstLine.contains("css")) {
                getResponse(dos, HtmlUtils.getFileLocation(firstLine), true);
            }
            getResponse(dos, HtmlUtils.getFileLocation(firstLine));
        }
    }

    private void responseDynamicHtml(DataOutputStream dos, byte[] bytes) {
        response200Header(dos, bytes.length);
        responseBody(dos, bytes);
    }

    private void getResponse(DataOutputStream dos, String htmlName) throws IOException {
        getResponse(dos, htmlName, false);
    }

    private void getResponse(DataOutputStream dos, String htmlName, boolean isCss) throws IOException {
        byte[] body = HtmlUtils.getHtml(htmlName);
        if(isCss) {
            responseCss200Header(dos, body.length);
            responseBody(dos, body);
            return;
        }
        response200Header(dos, body.length);
        responseBody(dos, body);
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseCss200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/css;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String location, boolean isLogin) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: http://localhost:8080" + location +"\r\n");
            dos.writeBytes("Set-Cookie: logined=" + isLogin + "\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            //flush() - 버퍼를 출력하고 비운다.
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


}
