package com.leno.crawler.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * @author leon
 * @date 2018-06-21 17:33
 * @desc http工具类
 */
public class HttpUtils {
    public static String get(String url) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet(url);
            //抛弃异常状态请求
            ResponseHandler<String> responseHandler = httpResponse -> {
                int status = httpResponse.getStatusLine().getStatusCode();
                System.out.println("------------status:" + status);
                if (status >= 200 && status < 300) {
                    HttpEntity entity = httpResponse.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else if (status == 300 || status ==301 || status == 302 ||status == 304 || status == 400 ||
                        status == 401 || status == 403 || status == 404 || new String(status + "").startsWith("5")){ //refer to link http://blog.csdn.net/u012043391/article/details/51069441
                    return null;
                }else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };
            return httpClient.execute(httpGet,responseHandler);
        }finally {
            httpClient.close();
        }
    }
}
