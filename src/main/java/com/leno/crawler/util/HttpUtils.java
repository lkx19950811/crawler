package com.leno.crawler.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author leon
 * @date 2018-06-21 17:33
 * @desc http工具类
 */
public class HttpUtils {
    static Logger logger = LoggerFactory.getLogger(HttpUtils.class);
    /**
     * 设置代理get请求
     * @param url
     * @param proxy
     * @return
     * @throws Exception
     */
    public static String proxyGet(String url,HttpHost proxy) throws Exception {
        //设置代理IP、端口、协议
//        HttpHost proxy = new HttpHost("你的代理的IP", 8080, "http");
        RequestConfig defaultRequestConfig = RequestConfig.custom().setProxy(proxy).build();
        //实例化CloseableHttpClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
        HttpGet httpGet = setHeader(url);
        try {
            ResponseHandler<String> responseHandler = getResponseHandler();
            return httpClient.execute(httpGet,responseHandler);
        }finally {
            httpClient.close();
        }
    }

    /**
     * 普通get
     * @param url 请求地址
     * @return String 类型的html或者数据
     * @throws IOException
     */
    public static String get(String url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();//创建默认httpclient
        HttpGet httpGet = setHeader(url);
        try {
            ResponseHandler<String> responseHandler = getResponseHandler();//设置responseHandler
            return httpClient.execute(httpGet,responseHandler);
        }finally {
            httpClient.close();
        }
    }

    /**
     * 自定义 返回以及错误捕捉
     * @return 自定义responseHandler
     */
    public static ResponseHandler<String> getResponseHandler(){
        ResponseHandler<String> responseHandler = httpResponse -> {
            int status = httpResponse.getStatusLine().getStatusCode();
            logger.info("------------status:" + status);
            if (status >= 200 && status < 300) {            //抛弃异常状态请求
                HttpEntity entity = httpResponse.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else if (status == 300 || status ==301 || status == 302 ||status == 304 || status == 400 ||
                    status == 401 || status == 403 || status == 404 || new String(status + "").startsWith("5")){ //refer to link http://blog.csdn.net/u012043391/article/details/51069441
                return null;
            }else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        };
        return responseHandler;
    }

    /**
     * 设置模拟请求头
     * @param url
     * @return 返回httpget
     */
    public static HttpGet setHeader(String url){
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Accept", "text/html");
        httpGet.addHeader("Accept-Charset", "utf-8");
        httpGet.addHeader("Accept-Encoding", "gzip");
        httpGet.addHeader("Accept-Language", "zh-CN,zh");
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.160 Safari/537.22");
        return httpGet;
    }
}
