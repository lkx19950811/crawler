package com.leno.crawler;

import com.leno.crawler.util.HttpUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.*;

/**
 * 描述:
 *
 * @author Leo
 * @create 2018-06-23 下午 2:01
 */
public class HttpTest  {

    @Test
    public void testHttpProxy() throws Exception {
        HttpHost porxy = new HttpHost("114.231.68.97",18118,"https");
        String res = HttpUtils.proxyGet("https://www.douban.com",porxy);
        System.out.println(res);
    }
    @Test
    public void test1() throws IOException {
        HttpClientBuilder build = HttpClients.custom();
        HttpHost proxy = new HttpHost("115.223.240.136", 9000);
        CloseableHttpClient client = build.setProxy(proxy).build();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(20000).setConnectTimeout(20000).build();//设置请求和传输超时时间
        HttpGet request = new HttpGet("https://blog.csdn.net/ryelqy/article/details/75331453");
        request.setConfig(requestConfig);
        CloseableHttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));
        response.close();
        client.close();
    }
    @Test
    public void test2(){
        String res = HttpUtils.get("https://movie.douban.com/subject/26756258/");
        System.out.println(res);
    }
    @Test
    public void test3(){
        String res = HttpUtils.get("http://ip.baizhongsou.com/?u=lkx19950811&p=6e3e5706735627a2&sl=1000");
        System.out.println(res);
    }
}
