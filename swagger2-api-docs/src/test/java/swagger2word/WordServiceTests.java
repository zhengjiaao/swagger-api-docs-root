/**
 * @Company: 上海***技术有限公司
 * @Department: 数据中心
 * @Author: 宇宙小神特别萌
 * @Email: ***
 * @Date: 2021-12-16 15:51
 * @Since:
 */
package swagger2word;

import com.alibaba.fastjson.JSON;
import com.deepoove.poi.XWPFTemplate;
import lombok.SneakyThrows;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import swagger.swagger2word.WordService;
import swagger.swagger2word.WordServiceImpl;

import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.Map;

public class WordServiceTests {

    @SneakyThrows
    public RestTemplate restTemplate() {
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(csf)
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);

        //60s
        requestFactory.setConnectTimeout(60 * 1000);
        requestFactory.setReadTimeout(60 * 1000);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }

    final static String API_DOCS_URL = "http://127.0.0.1:8088/v2/api-docs";


    @SneakyThrows
    @Test
    public void test() {
        WordService wordService = new WordServiceImpl(restTemplate());
        Map<String, Object> objectMap = wordService.tableList(API_DOCS_URL);

        String data = JSON.toJSONString(objectMap, true);
        System.out.println(data);

        InputStream resourceAsStream = this.getClass().getResourceAsStream("/templates/word/REST API 说明文档.docx");

        XWPFTemplate template = XWPFTemplate.compile(resourceAsStream).render(objectMap);
        template.writeToFile("./target/test-classes/word-out.docx");
    }
}
