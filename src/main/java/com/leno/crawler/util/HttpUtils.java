package com.leno.crawler.util;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author leon
 * @date 2018-06-21 17:33
 * @desc http工具类
 */
@Configuration
public class HttpUtils {
    private final static String cookie = "ll=\"118172\"; bid=1lF6m_SDs4M; __utmc=30149280; __utmc=223695111; __yadk_uid=8m9REAOy9bhnKCtAETjNJGWcE9sNiLjh; _vwo_uuid_v2=DD45C53267166E72ADC99933B5541F949|f6c4884a330a9c87e41b55c97b0d9d86; viewed=\"27073387\"; gr_user_id=c482e9c6-bcb0-4f4e-8663-c17311abfbf6; __utmv=30149280.17214; douban-fav-remind=1; ps=y; __utmz=30149280.1535952481.50.33.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; __utmz=223695111.1535958788.30.18.utmcsr=douban.com|utmccn=(referral)|utmcmd=referral|utmcct=/; _ga=GA1.2.1388735101.1527214209; _gid=GA1.2.100509127.1535959101; push_noty_num=0; push_doumail_num=0; ap_v=1,6.0; _pk_ref.100001.4cf6=%5B%22%22%2C%22%22%2C1535966213%2C%22https%3A%2F%2Fwww.douban.com%2F%22%5D; _pk_ses.100001.4cf6=*; __utma=30149280.1388735101.1527214209.1535958787.1535966216.52; __utma=223695111.1131618684.1527214209.1535958788.1535966216.31; __utmb=223695111.0.10.1535966216; __utmt=1; __utmb=30149280.2.9.1535966373293; dbcl2=\"172146532:oYRQmj9t3vw\"; ck=PRH7; _pk_id.100001.4cf6=cd7b5a58b38e962f.1527214209.34.1535966406.1535959779.";
    private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);
    private static CloseableHttpClient prrlhttpClient = null;
    private final static Object syncLock = new Object();
    /**
     * 设置代理get请求
     *
     * @param url 请求地址
     * @param proxy 代理对象
     * @return 请求结果
     */
    public static String proxyGet(String url, HttpHost proxy) {
        //设置代理IP、端口、协议
//        HttpHost proxy = new HttpHost("你的代理的IP", 8080, "http");
        RequestConfig defaultRequestConfig = RequestConfig.custom().
                setProxy(proxy).
                setConnectTimeout(20000).
                setSocketTimeout(20000).
                setConnectionRequestTimeout(20000)
                .setAuthenticationEnabled(false).build();
        //实例化CloseableHttpClient对象
        CloseableHttpClient httpClient = getHttpClient(url);
        HttpGet httpGet = setHeader(url);
        httpGet.setConfig(defaultRequestConfig);
        String response = "";
        try {
            ResponseHandler<String> responseHandler = getResponseHandler();
            response = httpClient.execute(httpGet, responseHandler);
        } catch (Exception ignored) {
        } finally {
            try {
                httpGet.releaseConnection();
                httpClient.close();
//                logger.info("关闭连接,代理{}",proxy);
            } catch (IOException e) {
                logger.error("!关闭链接失败,代理{}",proxy);
            }
            return DataUtils.transcoding(response, "UTF-8");
        }
    }

    /**
     * 普通get http
     *
     * @param url 请求地址
     * @return String 类型的html或者数据
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
     * post方法
     * @param url
     * @param params
     * @return
     */
    public static String post(String url, Map<String,String> params){
        //设置代理IP、端口、协议
        //实例化CloseableHttpClient对象
        CloseableHttpClient httpClient = getHttpClient(url);
        HttpPost httpPost = new HttpPost(url);
        String response = "";
        try {
            setParams(httpPost,params);
            ResponseHandler<String> responseHandler = getResponseHandler();
            response = httpClient.execute(httpPost, responseHandler);
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
     * 设置代理get请求
     *
     * @param url 请求地址
     * @param proxy 代理对象
     * @return 请求结果
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
        return httpResponse -> {
            int status = httpResponse.getStatusLine().getStatusCode();
            logger.info("------------status:" + status);
            if (status >= 200 && status < 300) {            //抛弃异常状态请求
                HttpEntity entity = httpResponse.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else if (status == 300 || status == 301 || status == 302 || status == 304 || status == 400 ||
                    status == 401 || status == 403 || status == 404 || (status + "").startsWith("5")) { //refer to link http://blog.csdn.net/u012043391/article/details/51069441
                if (status==404)return "404";//404页面不存在,添加标志
                return null;//其他则是请求错误,被策略组拒绝,稍后继续请求
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        };
    }

    /**
     * 设置模拟请求头
     *
     * @param url 请求地址
     * @return 返回httpget
     */
    private static HttpGet setHeader(String url) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Accept", "text/html");
        httpGet.addHeader("Accept-Charset", "utf-8");
        httpGet.addHeader("Accept-Encoding", "gzip");
        httpGet.addHeader("Accept-Language", "zh-CN,zh");
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.160 Safari/537.22");
        httpGet.addHeader("Cookie",cookie);
        return httpGet;
    }

    /**
     * 创建https连接
     * @return 返回httpclient对象
     */
    public static CloseableHttpClient createSSLClientDefault() {
        try {
            //信任所有
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (TrustStrategy) (chain, authType) -> true).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException | KeyStoreException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();
    }
    /**
     * 获取HttpClient对象(使用连接池,不然会造成堵塞)
     *
     * @return 返回httpclient对象
     * @author SHANHY
     * @create 2015年12月18日
     */
    public static CloseableHttpClient getHttpClient(String url) {
        String hostname = url.split("/")[2];
        int port = 80;
        if (url.indexOf("https")>0){
            port = 443;
        }
        if (hostname.contains(":")) {
            String[] arr = hostname.split(":");
            hostname = arr[0];
            port = Integer.parseInt(arr[1]);
        }
        if (prrlhttpClient == null) {
            synchronized (syncLock) {
                if (prrlhttpClient == null) {
                    prrlhttpClient = createHttpClient(200, 40, 100, hostname, port);
                }
            }
        }
        return prrlhttpClient;
    }

    /**
     * 创建HttpClient对象
     *
     * @return 返回httpclient对象
     * @author SHANHY
     * @create 2015年12月18日
     */
    public static CloseableHttpClient createHttpClient(int maxTotal,
                                                       int maxPerRoute, int maxRoute, String hostname, int port) {
        ConnectionSocketFactory plainsf = PlainConnectionSocketFactory
                .getSocketFactory();
        LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory
                .getSocketFactory();
        Registry<ConnectionSocketFactory> registry = RegistryBuilder
                .<ConnectionSocketFactory> create().register("http", plainsf)
                .register("https", sslsf).build();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(
                registry);
        // 将最大连接数增加
        cm.setMaxTotal(maxTotal);
        // 将每个路由基础的连接增加
        cm.setDefaultMaxPerRoute(maxPerRoute);
        HttpHost httpHost = new HttpHost(hostname, port);
        // 将目标主机的最大连接数增加
        cm.setMaxPerRoute(new HttpRoute(httpHost), maxRoute);

        // 请求重试处理
        HttpRequestRetryHandler httpRequestRetryHandler = (exception, executionCount, context) -> {
            if (executionCount >= 3) {// 如果已经重试了3次，就放弃
                return false;
            }
            if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                return true;
            }
            if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                return false;
            }
            if (exception instanceof InterruptedIOException) {// 超时
                return false;
            }
            if (exception instanceof UnknownHostException) {// 目标服务器不可达
                return false;
            }
            if (exception instanceof SSLException) {// SSL握手异常
                return false;
            }
            HttpClientContext clientContext = HttpClientContext
                    .adapt(context);
            HttpRequest request = clientContext.getRequest();
            // 如果请求是幂等的，就再次尝试
            return !(request instanceof HttpEntityEnclosingRequest);
        };
        return HttpClients.custom()
                .setConnectionManager(cm)
                .setRetryHandler(httpRequestRetryHandler).build();
    }

    public static void main(String[] args) throws IOException {
        CloseableHttpClient hp = createSSLClientDefault();
        HttpGet hg = new HttpGet("https://news.cnblogs.com/");
        CloseableHttpResponse response = hp.execute(hg);
        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity, "utf-8");
        System.out.println(content);
        hp.close();

    }
    /**
     * 为post设置参数
     * @param post httppost对象
     * @param params 请求参数
     */
    public static void setParams(HttpPost post,Map<String,String> params){
        if (params != null) {//添加参数
            List<NameValuePair> nvps = new ArrayList<>();
            for (Map.Entry<String, String> entity : params.entrySet()) {
                nvps.add(new BasicNameValuePair(entity.getKey(), entity.getValue()));
            }
            try {
                post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
}
