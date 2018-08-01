package com.leno.crawler.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * 描述:
 * 数据处理
 *
 * @author Leo
 * @create 2018-06-23 下午 2:30
 */
class DataUtils {
    private final static Logger logger = LoggerFactory.getLogger(DataUtils.class);

    /**
     * 获取字符集
     * @param str 要获取字符集的字符串
     * @return 返回字符集类型
     */
    private static String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {      //判断是不是GB2312
                return encode;      //是的话，返回“GB2312“，以下代码同理
            }
        } catch (Exception ignored) {
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {      //判断是不是ISO-8859-1
                return encode;
            }
        } catch (Exception ignored) {
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {   //判断是不是UTF-8
                return encode;
            }
        } catch (Exception ignored) {
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {      //判断是不是GBK
                return encode;
            }
        } catch (Exception ignored) {
        }
        return "";        //如果都不是，说明输入的内容不属于常见的编码格式。
    }

    /**
     * 字符转码
     * @param str 需要转码的字符串
     * @param charset 转码的字符集类型
     * @return 转换完毕的字符串
     */
    static String transcoding(String str, String charset){
        try {
            String encoding = getEncoding(str);
            if (encoding.equalsIgnoreCase("gb2312")){
                charset = encoding;
            }
            return new String(str.getBytes(encoding),charset);
        } catch (UnsupportedEncodingException e) {
            logger.info("转码失败");
        }
        return str;
    }

}
