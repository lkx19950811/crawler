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
public class DataUtils {
    private final static Logger logger = LoggerFactory.getLogger(DataUtils.class);

    /**
     * 获取字符集
     * @param str
     * @return
     */
    public static String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {      //判断是不是GB2312
                String s = encode;
                return s;      //是的话，返回“GB2312“，以下代码同理
            }
        } catch (Exception exception) {
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {      //判断是不是ISO-8859-1
                String s1 = encode;
                return s1;
            }
        } catch (Exception exception1) {
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {   //判断是不是UTF-8
                String s2 = encode;
                return s2;
            }
        } catch (Exception exception2) {
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {      //判断是不是GBK
                String s3 = encode;
                return s3;
            }
        } catch (Exception exception3) {
        }
        return "";        //如果都不是，说明输入的内容不属于常见的编码格式。
    }

    /**
     * 字符转码
     * @param str
     * @param charset
     * @return
     */
    public static String transcoding(String str,String charset){
        try {
            return new String(str.getBytes(getEncoding(str)),charset);
        } catch (UnsupportedEncodingException e) {
            logger.info("转码失败");
        }finally {
            return str;
        }
    }

}
