package com.leno.crawler;

import com.leno.crawler.util.HttpUtils;
import com.leno.crawler.util.MessageUtils;
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
import java.lang.ref.SoftReference;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 描述:
 *
 * @author Leo
 * @create 2018-06-23 下午 2:01
 */
public class HttpTest  {

    @Test
    public void testHttpProxy() throws Exception {
        HttpHost porxy = new HttpHost("111.155.116.232",8123,"https");
        String res = HttpUtils.proxyGetHttps("https://www.douban.com",porxy);
        System.out.println(res);
    }
    @Test
    public void test1() throws IOException {
        HttpClientBuilder build = HttpClients.custom();
        HttpHost proxy = new HttpHost("118.190.95.43", 9001);
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
//        String res = HttpUtils.get("https://movie.douban.com/subject/26416062/?from=showing");
//        System.out.println(res);
        System.out.println(" ॣ ");
    }
    @Test
    public void test3(){
        String res = HttpUtils.get("https://www.baidu.com");
        System.out.println(res);
        SoftReference<Object> softRef = new SoftReference<Object>(new Object());

    }
    @Test
    public void test4(){
        Map map = new HashMap();
        Map newmap = new HashMap();
        map.put("str","1");
        newmap.put("str","2");
        map.put("a",newmap);
        System.out.println(map);
        newmap.put("b","c");
        System.out.println(map);
    }
    @Test
    public void test5(){
        int a = new Random().nextInt(10);
        if (a<6){
            System.out.println("a<6");
        }else if (a>6){
            System.out.println("a>6");
        }else {
            System.out.println("a=6");
        }
    }
    @Test
    public void https(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYYMMDDhhmmss");
        String merId = "875070200000059";
        String orderId = "02546364928236724233";//我们商户的订单
        String txnType = "01";
        String txnSubType = "00";
        String bizType = "000000";
        String txnTime = simpleDateFormat.format(new Date());
        String queryId = "231808071622422400028";//ecommerce那边的定单号 也就是trace number
        TreeMap treeMap = new TreeMap();
        treeMap.put("merId",merId);
        treeMap.put("orderId",orderId);
        treeMap.put("txnType",txnType);
        treeMap.put("txnSubType",txnSubType);
        treeMap.put("bizType",bizType);
        treeMap.put("txnTime",txnTime);
        treeMap.put("queryId",queryId);
        String signature = sign(treeMap,"r7gH5QvxXnEdGHF");
        String signMethod = "SHA";
        treeMap.put("signature",signature);
        treeMap.put("signMethod",signMethod);
        String res = HttpUtils.post("https://sit.sinopayonline.com/UGateWay/backTransReq",treeMap);
        System.out.println(res);
    }
    /**
     * 签名
     * @param map 参数map
     * @param key 秘钥
     * @return 签名结果
     */
    private static String sign(TreeMap map,String key){
        StringBuffer sb = new StringBuffer();
        map.forEach((k,v)-> sb.append(k + "=").append(v).append("&"));
        sb.append(MessageUtils.sha256(key));
        return MessageUtils.sha256(sb.toString());
    }
}
