package model;

import lombok.Getter;
import util.HtmlUtils;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Getter
public class HttpRequest {
    private static final String SPLIT_PATTERN = "( |\\?)";
    private static final int PATH_PARAM_INDEX = 3;
    private static final String CONTENT_LENGTH = "Content-Length";
    private static final String HEADER_PARSING_PATTERN = ": ";

    private Map<String, String> header = new HashMap<>();
    private String method;
    private String path;

    public HttpRequest(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String firstLine = HtmlUtils.getFirstLine(bufferedReader);
        parseMethodPath(firstLine);
        parseHeader(bufferedReader);
    }

    public String getHeader(String headerKey) {
        return header.get(headerKey);
    }

    private void parseMethodPath(String firstLine) {
        String[] header = firstLine.split(SPLIT_PATTERN);
        this.method = header[0];
        this.path = header[1];
        if(header.length == PATH_PARAM_INDEX) {
            this.header.putAll(HttpRequestUtils.parseQueryString(header[2]));
        }
    }

    private void parseHeader(BufferedReader bufferedReader) throws IOException {
        String line = "start";
        while(!"".equals(line)) {
            line = bufferedReader.readLine();
            if(line == null) {
                return;
            }
            String[] splitLine = line.split(HEADER_PARSING_PATTERN);
            if (splitLine[0].isEmpty()) {
                continue;
            }
            header.put(splitLine[0], splitLine[1]);
        }

        if(header.get(CONTENT_LENGTH) != null) {
            this.header.putAll(HttpRequestUtils.parseQueryString(IOUtils.readData(bufferedReader, Integer.parseInt(header.get("Content-Length")))));
        }
    }
}
