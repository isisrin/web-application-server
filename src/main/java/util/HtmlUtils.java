package util;

import model.User;

import java.io.*;
import java.nio.file.Files;
import java.util.Map;

public class HtmlUtils {
    private static final String SPLIT_PATTERN = " ";
    public static final int REQUEST_RESOURCES = 1;
    public static final String SERVER_WEBAPP_PATH = "/Users/hyerin/study/web-application-server/webapp/";

    public static byte[] getHtml(InputStream inputStream) throws IOException {
        return Files.readAllBytes(new File(SERVER_WEBAPP_PATH + getLocation(inputStream)).toPath());
    }

    private static String getLocation(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        return getFileLocation(getFirstLine(bufferedReader));
    }

    private static String getFirstLine(BufferedReader bufferedReader) {
        return bufferedReader.lines().filter(line -> line != null).findFirst().get();
    }

    static String getFileLocation(String firstLine) {
        String request_resource = firstLine.split(SPLIT_PATTERN)[REQUEST_RESOURCES];
        if(request_resource.contains("?")) {
           getObject(getQueryString(request_resource));
           return "/index.html";
        }
        return request_resource;
    }

    static String getQueryString(String firstLine) {
        return firstLine.split("\\?")[1];
    }

    static User getObject(String queryString) {
        Map<String, String> params = HttpRequestUtils.parseQueryString(queryString);
        User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
        return user;
    }
}
