package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HtmlUtils;

import java.io.*;
import java.net.Socket;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final String INDEX_HTML = "/index.html";

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
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

    public void response(InputStream inputStream, DataOutputStream dos) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        String firstLine = HtmlUtils.getFirstLine(bufferedReader);

        if(firstLine.contains("POST")) {
            HtmlUtils.getObject(HtmlUtils.getBody(bufferedReader));
            response302Header(dos, INDEX_HTML);
        }

        if(firstLine.contains("GET")) {
            if(firstLine.contains("?")) {
                HtmlUtils.getObject(HtmlUtils.getQueryString(firstLine));
                getResponse(dos, INDEX_HTML);
                return;
            }

            getResponse(dos, HtmlUtils.getFileLocation(firstLine));
        }
    }

    private void getResponse(DataOutputStream dos, String htmlName) throws IOException {
        byte[] body = HtmlUtils.getHtml(htmlName);
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

    private void response302Header(DataOutputStream dos, String location) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: http://localhost:8080" + location);
            dos.writeBytes("\r\n");
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
