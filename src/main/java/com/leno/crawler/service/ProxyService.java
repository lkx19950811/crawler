package com.leno.crawler.service;

import com.leno.crawler.entity.Proxy;
import com.leno.crawler.repository.ProxyRepository;
import com.leno.crawler.util.HttpUtils;
import org.apache.http.HttpHost;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author leon
 * @date 2018-06-22 15:40
 * @desc
 */
@Service
public class ProxyService {
    private static final Logger logger = LoggerFactory.getLogger(ProxyService.class);
    @Autowired
    ProxyRepository proxyRepository;

    /**@deprecated
     * 随机获得代理地址
     * @return
     */
    public Proxy getProxyHost(){
        Random random = new Random();
        List<Proxy> proxies = proxyRepository.findAll();
        /*
            随机选用代理地址
         */
        Proxy proxy = proxies.get(random.nextInt(proxies.size()));
        // HttpHost proxy = new HttpHost("你的代理的IP", 8080, "http");
        return proxy;
    }

    /**
     * 选择最近成功的地址
     * @param proxy 上一次使用的地址
     * @return
     */
    public Proxy getBestProxyHost(Proxy proxy){
        if (proxy.getIp()==null){
            proxy = proxyRepository.findAllByOrOrderByConDate().get(0);
        }
        List<Proxy> list = proxyRepository.findAllByIpNotInOrderByConDateDesc(proxy.getIp());
        for (Proxy pr : list){
            if (pr.getStatus().equals("可用")){
                return pr;
            }
        }
        return proxy;//是在没找到可用的代理,先凑合着用,毕竟用自己请求会被封
    }

    /**
     * 代理失败对Proxy进行失败记录
     * @param proxy
     */
    public void failProxy(Proxy proxy){
        if (proxy.getTryNum()>3){
            proxyRepository.delete(proxy);
        }else {
            proxy.setStatus("不可用");
            proxy.setTryNum(proxy.getTryNum() +1);
            proxyRepository.save(proxy);
        }
    }

    /**
     * 验证可用Proxy
     */
    public void verifyProxy() throws InterruptedException {
        logger.info("验证代理状态");
        List<Proxy> proxies = proxyRepository.findAll();
        for (Proxy proxy:proxies){
            friendlyToDouban();
            HttpHost host = new HttpHost(proxy.getIp(),proxy.getPort(),proxy.getType());
            String res = HttpUtils.proxyGet("https://www.baidu.com",host);
            if (!StringUtils.isEmpty(res)){
                proxy.setStatus("可用");
                proxy.setConDate(new Date());
                proxy = proxyRepository.save(proxy);
            }else {
                proxy.setStatus("不可用");
                proxy = proxyRepository.save(proxy);
            }
            logger.info("验证代理状态{},{}",proxy.getStatus(),proxy.getIp());
        }
    }

    /**
     * 抓取代理
     */
    public void parseProxyUrl(String page) throws ParseException {
        logger.info(">>>>>>>>>>>>>> 抓代理  <<<<<<<<<<<<<");
        String content = HttpUtils.get("http://www.xicidaili.com/wt/" + page);
        Document document = Jsoup.parse(content);
        Elements elements = document.getElementById("ip_list").getElementsByTag("tbody");
        if (elements.size()==1){
            Elements trs = elements.get(0).children();
            for (Element tr : trs){
                Proxy proxy = new Proxy();
                if (tr.toString().contains("国家")){//说明不是ip列表
                    continue;
                }else {
                    String secReg = ".*(\\d+\\.\\d+).*";
                    Pattern r = Pattern.compile(secReg);
                    Matcher m = r.matcher(tr.children().get(6).toString());
                    if (m.find()){
                        Double sec = Double.valueOf(m.group(1));
                        if (sec>1.0)continue;
                    }
                    if (!tr.child(4).text().equals("高匿")){
                        continue;
                    }
                    String ipReg = "^(\\d+\\.\\d+\\.\\d+\\.\\d+).*";
                    r = Pattern.compile(ipReg);
                    m = r.matcher(tr.text());
                    if (m.find()){
                        String ip = m.group(1);
                        Optional optional = Optional.ofNullable(proxyRepository.findByIp(ip));
                        if (optional.isPresent()){
                            continue;
                        }else {
                            proxy.setIp(ip);
                        }
                    }
                    String portReg = "[\\s](\\d+)[\\s]";
                    r = Pattern.compile(portReg);
                    m = r.matcher(tr.text());
                    if (m.find()){
                        String port = m.group(1);
                        proxy.setPort(Integer.valueOf(port));
                    }
                    proxy.setType("http");

                    SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd mm:ss");
                    String str = tr.child(9).text();
                    proxy.setConDate(format.parse(str));
                    proxyRepository.save(proxy);
                    logger.info("Saving >>>>>>>>>>>>>>>> {}:{}",proxy.getIp(),proxy.getPort());
                }
            }
        }
    }
    /**
     * 随机停止1到10秒
     * @throws InterruptedException
     */
    private static void friendlyToDouban() throws InterruptedException {
        Thread.sleep((new Random().nextInt(10) + 1)*1000);//sleep for the random second so that avoiding to be listed into blacklist
    }
}
