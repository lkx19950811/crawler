package com.leno.crawler.schedule;

import com.leno.crawler.entity.Proxy;
import com.leno.crawler.repository.RecordRepository;
import com.leno.crawler.service.CommentService;
import com.leno.crawler.service.MovieService;
import com.leno.crawler.service.ProxyService;
import com.leno.crawler.service.RecordService;
import com.leno.crawler.util.HttpUtils;
import org.apache.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author leon
 * @date 2018-06-21 17:45
 * @desc
 */
@Component
public class CrawlerSchedule {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    RecordRepository recordRepository;
    @Autowired
    ProxyService proxyService;
    @Autowired
    RecordService recordService;
    @Autowired
    MovieService movieService;
    @Autowired
    CommentService commentService;
    @Value("${crawler.seed}")
    private String seed;
    @Value("${proxied}")
    private Boolean proxyied;
    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(100);
    Proxy proxy = new Proxy();
//    @Scheduled(cron = "0/1 * * * * ?")
    @Scheduled(initialDelay = 10000,fixedRate = 1000L * 60)//启动延迟10秒,执行间隔1分钟
    public void CrawlerDouban() throws Exception {
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>    开始爬取,代理设置:{}        <<<<<<<<<<<<<<<<<",proxyied);
        List<String> urls = new ArrayList<>();
        /*
        初始化循环的链接
         */
        recordService.initURLDouban(seed,urls);
        for (String url : urls){
            friendlyToDouban();//先停几秒
            //可更改是否使用代理
            final String page = isProt(url,proxyied);
            if (StringUtils.isEmpty(page))continue;
            Thread recordThread = new Thread(() -> {
                recordService.parseUrl(page);
            });
            Thread movieThread = new Thread(() -> {
                movieService.parseMovie(page,url);
            });
            Thread commentThread = new Thread(() -> {
                commentService.parseComment(page,url);
            });
            fixedThreadPool.execute(recordThread);
            fixedThreadPool.execute(movieThread);
            fixedThreadPool.execute(commentThread);
            recordService.setRecordONE(url);//将爬取过的标记为1
        }
    }

    /**
     * 随机停止1到10秒
     * @throws InterruptedException
     */
    private static void friendlyToDouban() throws InterruptedException {
        Thread.sleep((new Random().nextInt(10) + 1)*1000);//sleep for the random second so that avoiding to be listed into blacklist
    }

    /**
     * proxy 转换 Httphost
     * @param proxy
     * @return
     */
    private static HttpHost proxyToHttphost(Proxy proxy){
        return new HttpHost(proxy.getIp(),proxy.getPort(),proxy.getType());
    }

    /**
     * 是否使用代理,可以在配置文件中配置
     * @param url
     * @param proxied
     * @return
     */
    public String isProt (String url,Boolean proxied) throws InterruptedException {
        if (proxied){
            String content = "";
            boolean requestStatus = false;
            Integer countTry = 0;
            while (!requestStatus){//循环使用代理,失败则切换
                if (proxy.getIp()==null)//当前使用的代理未指定,先获取代理
                proxy =  proxyService.getBestProxyHost(proxy);

                HttpHost proxyHost = proxyToHttphost(proxy);
                try{
                    content = HttpUtils.proxyGet(url,proxyHost);
                }catch (Exception e){
                    logger.error("代理失败,切换代理点");
                    proxyService.failProxy(proxy);//对失败的代理ip进行处理
                }
                if (!StringUtils.isEmpty(content)){//如果拿到了返回,则不继续请求
                    setProxy(proxy);//请求得到了值,则设置好代理
                    requestStatus = true;
                }else {
                    proxy.setStatus("不可用");
                    proxyService.failProxy(proxy);//对失败的代理ip进行处理
                    proxy =  proxyService.getBestProxyHost(proxy);
                    if (++countTry>3){//重复切换代理超过3次,暂时放过此条url
                        requestStatus = true;
                    }
                    Thread.sleep(1000);
                }
            }
            return content;
        }else {
            String content = "";
            try{
                content =  HttpUtils.get(url);
            }catch (Exception e){
               this.proxyied = true;//普通get出问题,打开代理开关
            }
            return content;
        }
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }
}
