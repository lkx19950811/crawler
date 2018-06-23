import com.leno.crawler.service.ProxyService;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述:
 *
 * @author Leo
 * @create 2018-06-24 上午 12:40
 */
public class ParseProxyTest {
    @Test
    public void testParse(){
        ProxyService proxyService = new ProxyService();
        proxyService.parseProxyUrl();
    }
    @Test
    public void testPattern(){
        String str = "110.73.11.80 8123 广西防城港 高匿 HTTP 361天 18-06-24 02:03";
        String reg = "[\\s](\\d+)[\\s]";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(reg);
        // 现在创建 matcher 对象
        Matcher m = r.matcher(str);
        if (m.find()){
            String ip = m.group(1);
            System.out.println(ip);
        }
    }
    @Test
    public void test1(){
        String str = "<td class=\"country\"> \n" +
                " <div title=\"0.156秒\" class=\"bar\"> \n" +
                "  <div class=\"bar_inner fast\" style=\"width:85%\"> \n" +
                "  </div> \n" +
                " </div> </td>";
        String secReg = ".*(\\d+\\.\\d+).*";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(secReg);
        // 现在创建 matcher 对象
        Matcher m = r.matcher(str);
        if (m.find()){
            String ip = m.group(1);
            System.out.println(ip);
        }
    }
}
