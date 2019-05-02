package util;

import model.User;

import java.io.*;
import java.nio.file.Files;
import java.util.Map;

public class HtmlUtils {
    private static final String SPLIT_PATTERN = " ";
    public static final int REQUEST_RESOURCES = 1;
    public static final String SERVER_WEBAPP_PATH = "/Users/hyerin/study/web-application-server/webapp";
    public static final String QUERY_PATTERN = "\\?";

    public static byte[] getHtml(InputStream inputStream) throws IOException {
        return Files.readAllBytes(new File(SERVER_WEBAPP_PATH + getLocation(inputStream)).toPath());
    }

    private static String getLocation(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        return getFileLocation(bufferedReader);
    }

    static String getFileLocation(BufferedReader bufferedReader) throws IOException{
        String[] header = getFirstLine(bufferedReader).split(SPLIT_PATTERN);
        if(header[0].contains("POST")) {
            getObject(getBody(bufferedReader));
            return "/index.html";
        }
        String request_resource = header[REQUEST_RESOURCES];
        if(request_resource.contains("?")) {
           getObject(getQueryString(request_resource));
           return "/index.html";
        }
        return request_resource;
    }

    private static String getFirstLine(BufferedReader bufferedReader) {
        return bufferedReader.lines().filter(line -> line != null).findFirst().get();
    }

    static String getBody(BufferedReader bufferedReader) throws IOException {
        String contentLength = getLength(bufferedReader);
        String line = contentLength;
        while(!"".equals(line)) {
            line = bufferedReader.readLine();
        }
        return IOUtils.readData(bufferedReader, Integer.parseInt(contentLength.split(SPLIT_PATTERN)[REQUEST_RESOURCES]));
    }

    private static String getLength(BufferedReader bufferedReader) {
        return bufferedReader.lines().filter(line -> line.contains("Length")).findFirst().get();
    }

    static String getQueryString(String firstLine) {
        return firstLine.split(QUERY_PATTERN)[REQUEST_RESOURCES];
    }

    static User getObject(String queryString) {
        Map<String, String> params = HttpRequestUtils.parseQueryString(queryString);
        User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
        System.out.println("유저  "+ user.toString());
        return user;
    }
}
