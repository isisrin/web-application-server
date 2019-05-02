package util;

import model.User;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.*;

public class HtmlUtilsTest {

    private InputStream inputStream;
    private static final String getSampleHeader = "GET /index.html HTTP/1.1\n" +
            "Host: localhost:8080\n" +
            "Connection: keep-alive\n" +
            "Upgrade-Insecure-Requests: 1\n" +
            "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36\n" +
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3\n" +
            "Accept-Encoding: gzip, deflate, br\n" +
            "Accept-Language: ko,en;q=0.9,en-US;q=0.8\n" +
            "Cookie: SL_G_WPT_TO=ko; SL_GWPT_Show_Hide_tmp=undefined; SL_wptGlobTipTmp=undefined\n";

    private static final String postSampleHeader = "Host: localhost:8080\n" +
            "Connection: keep-alive\n" +
            "Content-Length: 84\n" +
            "Cache-Control: max-age=0\n" +
            "Origin: http://localhost:8080\n" +
            "Upgrade-Insecure-Requests: 1\n" +
            "Content-Type: application/x-www-form-urlencoded\n" +
            "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36\n" +
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3\n" +
            "Referer: http://localhost:8080/user/form.html\n" +
            "Accept-Encoding: gzip, deflate, br\n" +
            "Accept-Language: ko,en;q=0.9,en-US;q=0.8\n" +
            "Cookie: SL_G_WPT_TO=ko; SL_GWPT_Show_Hide_tmp=undefined; SL_wptGlobTipTmp=undefined\n" +
            "\n" +
            "userId=test&password=test1&name=%EB%B0%95%ED%98%9C%EB%A6%B0&email=isisrin%40nate.com";

    @Test
    public void 헤더읽기_html() throws IOException {
        inputStream = new ByteArrayInputStream(getSampleHeader.getBytes());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        String result = HtmlUtils.getFileLocation(bufferedReader);
        Assertions.assertThat(result).isEqualTo("/index.html");
    }

    @Test
    public void 헤더읽기_requestParam() {
        String result = HtmlUtils.getQueryString("GET /user/create?userId=test&password=test1&name=박혜린&email=isisrin%40nate.com");
        Assertions.assertThat(result).isEqualTo("userId=test&password=test1&name=박혜린&email=isisrin%40nate.com");

    }

    @Test
    public void 유저_가져오기() {
        User result = HtmlUtils.getObject("userId=test&password=test1&name=박혜린&email=isisrin%40nate.com");
        Assertions.assertThat(result.toString()).isEqualTo(new User("test", "test1", "박혜린", "isisrin%40nate.com").toString());
    }

    @Test
    public void 파일읽기() throws IOException {
        inputStream = new ByteArrayInputStream(getSampleHeader.getBytes());
        byte[] result = HtmlUtils.getHtml(inputStream);
        System.out.println(new String(result));
        Assertions.assertThat(result.length).isEqualTo(10500);
    }

    @Test
    public void 겟바디_테스트() throws IOException {
        inputStream = new ByteArrayInputStream(postSampleHeader.getBytes());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        String result = HtmlUtils.getBody(bufferedReader);
        System.out.println(result);
        Assertions.assertThat(result).isEqualTo("userId=test&password=test1&name=%EB%B0%95%ED%98%9C%EB%A6%B0&email=isisrin%40nate.com");
    }

}