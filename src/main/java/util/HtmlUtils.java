package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public abstract class HtmlUtils {
    private static final String SPLIT_PATTERN = " ";
    public static final int REQUEST_RESOURCES = 1;
    public static final String SERVER_WEBAPP_PATH = "/Users/hyerin/study/web-application-server/webapp";

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
}
