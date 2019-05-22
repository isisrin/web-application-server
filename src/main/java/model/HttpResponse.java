package model;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HtmlUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpResponse {
    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
    private static final String TYPE_CSS = "css";
    private static final String TYPE_HTML = "html";
    private static final String CARRIAGE_RETURN = "\r\n";

    private DataOutputStream dos;
    private Map<String, String> header;

    public HttpResponse(OutputStream outPutStream) {
        dos = new DataOutputStream(outPutStream);
        header = new LinkedHashMap<>();
    }

    public void forward(String forwardUrl) throws IOException {
        getResponse(forwardUrl);
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

    private void getResponse(String htmlName) throws IOException {
        byte[] body = HtmlUtils.getHtml(htmlName);
        response200Header(dos, body.length, getType(htmlName));
        responseBody(dos, body);
    }

    public void dynamicResponse(String content) {
        byte[] body = content.getBytes();
        response200Header(dos, body.length, TYPE_HTML);
        responseBody(dos, body);
    }

    private String getType(String htmlName) {
        return (htmlName.contains(TYPE_CSS)) ? TYPE_CSS : TYPE_HTML;
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String type) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK" + CARRIAGE_RETURN);
            dos.writeBytes("Content-Type: text/" + type + ";charset=utf-8" + CARRIAGE_RETURN);
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + CARRIAGE_RETURN);
            dos.writeBytes(CARRIAGE_RETURN);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void addHeader(String headerKey, String headerValue) {
        header.put(headerKey, headerValue);
    }

    @SneakyThrows
    public void sendRedirect(String redirectUrl) {
        dos.writeBytes("HTTP/1.1 302 Found" + CARRIAGE_RETURN);
        dos.writeBytes("Location: http://localhost:8080" + redirectUrl + CARRIAGE_RETURN);
        if(!header.isEmpty()) {
            setResponseHeader();
        }
        dos.writeBytes(CARRIAGE_RETURN);
    }

    @SneakyThrows
    private void setResponseHeader() {
        for (String key : header.keySet()) {
            dos.writeBytes(key + ": " +  header.get(key) + CARRIAGE_RETURN);
        }
    }
}