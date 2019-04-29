package util;

import java.io.*;
import java.nio.file.Files;

public class HtmlUtils {
    private static final String SPLIT_PATTERN = " ";
    public static final int HTML_INDEX = 1;
    public static final String SERVER_WEBAPP_PATH = "/Users/hyerin/study/web-application-server/webapp/";

    public static byte[] getHtml(InputStream inputStream) throws IOException {
        return Files.readAllBytes(new File(SERVER_WEBAPP_PATH + getLocation(inputStream)).toPath());
    }

    private static String getLocation(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        return getFileLocation(getFirstLine(bufferedReader));
    }

    static String getFileLocation(String firstLine) {
        return firstLine.split(SPLIT_PATTERN)[HTML_INDEX];
    }

    private static String getFirstLine(BufferedReader bufferedReader) {
        return bufferedReader.lines().filter(line -> line != null).findFirst().get();
    }
}
