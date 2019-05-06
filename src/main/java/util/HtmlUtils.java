package util;

import model.User;

import java.io.*;
import java.nio.file.Files;
import java.util.Map;

public abstract class HtmlUtils {
    private static final String SPLIT_PATTERN = " ";
    public static final int REQUEST_RESOURCES = 1;
    public static final String SERVER_WEBAPP_PATH = "/Users/hyerin/study/web-application-server/webapp";
    public static final String QUERY_PATTERN = "\\?";

    public static byte[] getHtml(String pageLocation) throws IOException {
        return Files.readAllBytes(new File(SERVER_WEBAPP_PATH + pageLocation).toPath());
    }

    public static String getFileLocation(String firstLine) {
        String[] header = firstLine.split(SPLIT_PATTERN);
        return header[REQUEST_RESOURCES];
    }

    public static String getFirstLine(BufferedReader bufferedReader) {
        return bufferedReader.lines().filter(line -> line != null).findFirst().get();
    }

    public static String getBody(BufferedReader bufferedReader) throws IOException {
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

    public static String getQueryString(String firstLine) {
        return firstLine.split(QUERY_PATTERN)[REQUEST_RESOURCES];
    }

    public static User getObject(String queryString) {
        Map<String, String> params = HttpRequestUtils.parseQueryString(queryString);
        User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
        System.out.println("유저  "+ user.toString());
        return user;
    }

}
