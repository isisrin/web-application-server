package model;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.junit.Assert.*;

public class HttpRequestTest {

    private String testDirectory = "./src/test/resources/";

    @Test
    public void request_GET() throws Exception {
        InputStream inputStream = new FileInputStream(new File(testDirectory + "http_GET.txt"));
        HttpRequest request = new HttpRequest(inputStream);

        assertEquals("GET", request.getMethod());
        assertEquals("/user/create", request.getPath());
        assertEquals("keep-alive", request.getHeader("Connection"));
        assertEquals("test", request.getHeader("userId"));
    }

    @Test
    public void request_POST() throws Exception {
        InputStream inputStream = new FileInputStream(new File(testDirectory + "http_POST.txt"));
        HttpRequest request = new HttpRequest(inputStream);

        assertEquals("POST", request.getMethod());
        assertEquals("/user/create", request.getPath());
        assertEquals("keep-alive", request.getHeader("Connection"));
        assertEquals("test", request.getHeader("userId"));
    }
}