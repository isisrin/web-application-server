package util;

import db.DataBase;
import model.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Map;

public abstract class HtmlUtils {
    private static final String SPLIT_PATTERN = " ";
    public static final int REQUEST_RESOURCES = 1;
    public static final String SERVER_WEBAPP_PATH = "/Users/hyerin/study/web-application-server/webapp";
    public static final String QUERY_PATTERN = "\\?";
    private static final String TBODY_TEMPLATE = "<tr>\n" +
            "<td>userId</td>\n" +
            "<td>userName</td>\n" +
            "<td>userEmail</td>\n" +
            "<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n" +
            "</tr>\n";

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

    public static User joinUser(String queryString) {
        Map<String, String> params = HttpRequestUtils.parseQueryString(queryString);
        return new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
    }

    public static String getCookie(BufferedReader bufferedReader) {
        return bufferedReader.lines().filter(line -> line.contains("Cookie:")).findFirst().get();
    }

    public static byte[] generateHtmlTable() {
        StringBuilder stringBuilder = new StringBuilder("<table border='1'>");
        Collection<User> users = DataBase.findAll();
        users.forEach(user -> stringBuilder.append(toTbody(user)));
        return stringBuilder.append("</table>").toString().getBytes();
    }

    private static String toTbody(User user) {
        return TBODY_TEMPLATE.replace("userId", user.getUserId())
                             .replace("userName", user.getName())
                             .replace("userEmail", user.getEmail());
    }
}
