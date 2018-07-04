package com.leno.crawler.service;

import com.leno.crawler.entity.Proxy;
import com.leno.crawler.repository.ProxyRepository;
import com.leno.crawler.util.HttpUtils;
import com.leno.crawler.util.ThreadManager;
import org.apache.http.HttpHost;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.sound.sampled.Port;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    @Value("${proxyURL}")
    String proxyURL;
    @Value("${vipUrl}")
    String vipUrl;
    /**@deprecated
     * 随机获得代理地址(弃用)
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
        if (proxy.getIp()==null){//上一次没有成功记录,则搜出全部ip
            List<Proxy> proxies = proxyRepository.findAllByOrOrderByConDate();
            for (Proxy pr : proxies){
                if (pr.getStatus().equals("可用")){
                    return pr;
                }
            }
            return proxies.get(0);//是在没找到凑合着用
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
     *
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
    public void verifyProxy() throws InterruptedException, ParseException, ExecutionException {
        logger.info(">>>>>>>>>>>>>> 开始验证代理状态 <<<<<<<<<<<<<");
        List<Proxy> proxies = proxyRepository.findAll();
//        if (proxies.size()<10){//如果库里的代理数量小于10,则再去抓
//            parseProxyUrl("1");
//        }
        for (Proxy proxy:proxies){
            HttpHost host = new HttpHost(proxy.getIp(),proxy.getPort(),proxy.getType());
            Callable<String> callable = () -> {return HttpUtils.proxyGet("https://www.douban.com",host); };
            String res = ThreadManager.getInstance().submit(callable).get();//开始验证代理地址
            if (!StringUtils.isEmpty(res)){
                proxy.setStatus("可用");
                proxy.setConDate(new Date());
                proxy = proxyRepository.save(proxy);
            }else {
                if (proxy.getTryNum()>=3){
                    proxyRepository.delete(proxy);
                }else {
                    proxy.setStatus("不可用");
                    proxy.setTryNum(proxy.getTryNum()+1);
                    proxy = proxyRepository.save(proxy);
                }
            }
            if (proxy.getStatus().equals("可用")){
                logger.info("发现可用代理:>>>{}<<<,{}",proxy.getStatus(),proxy);
            }
//            logger.info("验证代理状态:>>>{}<<<,{}",proxy.getStatus(),proxy);
        }
    }

    /**
     * 解析代理
     * @param page 要抓第几页的代理
     * @throws ParseException
     */
    public void parseProxyUrl(String page) throws ParseException {
        logger.info(">>>>>>>>>>>>>> 开始抓取代理 <<<<<<<<<<<<<<<<");
        if (page.equals("1")){
            page = "";
        }
        String content = HttpUtils.get(proxyURL + page);
        Document document = Jsoup.parse(content);
        Elements elements = document.getElementById("ip_list").getElementsByTag("tbody");
        if (elements.size()==1){
            Elements trs = elements.get(0).children();
            for (Element tr : trs){
                Proxy proxy = new Proxy();
                if (tr.toString().contains("国家")){//说明不是ip列表
                    continue;
                }else {
                    String secReg = ".*(\\d+\\.\\d+).*";//匹配 title= 0.12秒中的秒数
                    Pattern r = Pattern.compile(secReg);
                    Matcher m = r.matcher(tr.children().get(6).toString());//存在tr节点中的第7个节点
                    if (m.find()){
                        Double sec = Double.valueOf(m.group(1));
                        if (sec>4.0)continue;//如果该代理连接速度大于4秒,则舍弃
                    }
                    if (!tr.child(4).text().equals("高匿")){//如果该代理不是高匿,舍弃
                        continue;
                    }
                    String ipReg = "^(\\d+\\.\\d+\\.\\d+\\.\\d+).*";//匹配代理的ip地址
                    r = Pattern.compile(ipReg);
                    m = r.matcher(tr.text());
                    if (m.find()){
                        String ip = m.group(1).trim();
                        Optional optional = Optional.ofNullable(proxyRepository.findByIp(ip));
                        if (optional.isPresent()){
                            continue;
                        }else {
                            proxy.setIp(ip);
                        }
                    }
                    String portReg = "[\\s](\\d+)[\\s]";//匹配端口号
                    r = Pattern.compile(portReg);
                    m = r.matcher(tr.text());
                    if (m.find()){
                        String port = m.group(1).trim();
                        proxy.setPort(Integer.valueOf(port));
                    }
                    String type = tr.child(5).text();//拿到代理类型
                    proxy.setType(type);

                    SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm");
                    String str = tr.child(9).text().trim();//获取最近验证时间
                    proxy.setConDate(format.parse(str));
                    proxyRepository.save(proxy);
                    logger.info("Saving >>>>>>>>>>>>>>>> {}:{}",proxy.getIp(),proxy.getPort());
                }
            }
        }
    }

    /**
     * 获取vip线路
     */
    public void getVipHost(){
        String res = HttpUtils.get(vipUrl);
        String[] ips = res.split("<br>");
        List<Proxy> proxies =  Stream.of(ips).map(Proxy::new).collect(Collectors.toList());
        proxyRepository.saveAll(proxies);
        logger.info("存入{}条",proxies.size());
    }
    /**
     * 随机停止1到10秒
     * @throws InterruptedException
     */
    private static void friendlyToDouban() throws InterruptedException {
        Thread.sleep((new Random().nextInt(10) + 1)*1000);//sleep for the random second so that avoiding to be listed into blacklist
    }
}
