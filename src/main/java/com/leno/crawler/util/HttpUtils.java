package com.leno.crawler.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author leon
 * @date 2018-06-21 17:33
 * @desc http工具类
 */
public class HttpUtils {
    static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * 设置代理get请求
     *
     * @param url
     * @param proxy
     * @return
     * @throws Exception
     */
    public static String proxyGet(String url, HttpHost proxy) {
        //设置代理IP、端口、协议
//        HttpHost proxy = new HttpHost("你的代理的IP", 8080, "http");
        RequestConfig defaultRequestConfig = RequestConfig.custom().setProxy(proxy).setConnectTimeout(10000).setSocketTimeout(10000).build();
        //实例化CloseableHttpClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
        HttpGet httpGet = setHeader(url);
        String response = "";
        try {
            ResponseHandler<String> responseHandler = getResponseHandler();
            response = httpClient.execute(httpGet, responseHandler);
        } catch (Exception e) {
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
            }
            return DataUtils.transcoding(response, "UTF-8");
        }
    }

    /**
     * 普通get http
     *
     * @param url 请求地址
     * @return String 类型的html或者数据
     * @throws IOException
     */
    public static String get(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();//创建默认httpclient
        String response = "";
        HttpGet httpGet = setHeader(url);
        try {
            ResponseHandler<String> responseHandler = getResponseHandler();//设置responseHandler
            response = httpClient.execute(httpGet, responseHandler);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return DataUtils.transcoding(response, "UTF-8");
    }
    /**
     * 设置代理get请求
     *
     * @param url
     * @param proxy
     * @return
     * @throws Exception
     */
    public static String proxyGetHttps(String url, HttpHost proxy) {
        //设置代理IP、端口、协议
//        HttpHost proxy = new HttpHost("你的代理的IP", 8080, "http");
        RequestConfig defaultRequestConfig = RequestConfig.custom().setProxy(proxy).build();
        //实例化CloseableHttpClient对象
        CloseableHttpClient httpClient = createSSLClientDefault();
        HttpGet httpGet = setHeader(url);
        String response = "";
        try {
            httpGet.setConfig(defaultRequestConfig);
            ResponseHandler<String> responseHandler = getResponseHandler();
            response = httpClient.execute(httpGet, responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return DataUtils.transcoding(response, "utf-8");
        }
    }
    /**
     * 自定义 返回以及错误捕捉
     *
     * @return 自定义responseHandler
     */
    private static ResponseHandler<String> getResponseHandler() {
        ResponseHandler<String> responseHandler = httpResponse -> {
            int status = httpResponse.getStatusLine().getStatusCode();
            logger.info("------------status:" + status);
            if (status >= 200 && status < 300) {            //抛弃异常状态请求
                HttpEntity entity = httpResponse.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else if (status == 300 || status == 301 || status == 302 || status == 304 || status == 400 ||
                    status == 401 || status == 403 || status == 404 || new String(status + "").startsWith("5")) { //refer to link http://blog.csdn.net/u012043391/article/details/51069441
                return null;
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        };
        return responseHandler;
    }

    /**
     * 设置模拟请求头
     *
     * @param url
     * @return 返回httpget
     */
    private static HttpGet setHeader(String url) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Accept", "text/html");
        httpGet.addHeader("Accept-Charset", "utf-8");
        httpGet.addHeader("Accept-Encoding", "gzip");
        httpGet.addHeader("Accept-Language", "zh-CN,zh");
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.160 Safari/537.22");
        return httpGet;
    }

    /**
     * 创建https连接
     * @return
     */
    public static CloseableHttpClient createSSLClientDefault() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                //信任所有
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();
    }

    public static void main(String[] args) throws ClientProtocolException, IOException {
        CloseableHttpClient hp = createSSLClientDefault();
        HttpGet hg = new HttpGet("https://news.cnblogs.com/");
        CloseableHttpResponse response = hp.execute(hg);
        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity, "utf-8");
        System.out.println(content);
        hp.close();

    }


}
