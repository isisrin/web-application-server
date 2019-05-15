package model;

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
        getResponse(htmlName, false);
    }

    private void getResponse(String htmlName, boolean isCss) throws IOException {
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
//            dos.writeBytes("Content-Type: text/html" + "여기가  css면 String return css하면되겠네" + ";charset=utf-8\r\n");
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

    public void addHeader(String headerKey, String headerValue) {
        header.put(headerKey, headerValue);
    }

    public void sendRedirect(String redirectUrl) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: http://localhost:8080" + redirectUrl +"\r\n");
            if(!header.isEmpty()) {
                dos.writeBytes("Set-Cookie" + ": " +  header.get("Set-Cookie") + "\r\n");
            }
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        
    }
}