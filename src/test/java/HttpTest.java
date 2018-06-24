import com.leno.crawler.util.HttpUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;

/**
 * 描述:
 *
 * @author Leo
 * @create 2018-06-23 下午 2:01
 */
public class HttpTest  {

    @Test
    public void testHttpProxy() throws Exception {
        HttpHost porxy = new HttpHost("125.109.197.229",27987,"http");
        String res = HttpUtils.proxyGet("https://blog.csdn.net/ryelqy/article/details/75331453",porxy);
        System.out.println(res);
    }
    @Test
    public void test1() throws IOException {
        HttpClientBuilder build = HttpClients.custom();
        HttpHost proxy = new HttpHost("125.109.197.229", 27987);
        CloseableHttpClient client = build.setProxy(proxy).build();
        HttpGet request = new HttpGet("https://blog.csdn.net/ryelqy/article/details/75331453");
        CloseableHttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));
    }
}
