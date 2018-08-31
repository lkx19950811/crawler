package com.leno.crawler.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author leon
 * @date 2018-08-03 11:57
 * @desc 摘要算法合集
 */
public class MessageUtils {
    /**
     * 使用指定哈希算法计算摘要信息
     * @param content 内容
     * @param algorithm 哈希算法 如 md5,sha-256,sha-512
     * @return 内容摘要
     */
    public static String getMessageDigest(String content,String algorithm){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(content.getBytes("utf-8"));
            return bytesToHexString(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * sha
     * @param str
     * @return
     */
    public static String SHA(String str,String type) {
        if (str == null) {
            return null;
        }
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance(type);
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer strBuff = new StringBuffer();
        for (int i = 0; i <byteArray.length; i++) {
            if (Integer.toHexString(0xFF &byteArray[i]).length() == 1)
                strBuff.append("0").append(Integer.toHexString(0xFF &byteArray[i]));
            else
                strBuff.append(Integer.toHexString(0xFF &byteArray[i]));
        }
        return strBuff.toString();
    }

    /**
     * 传入文本内容，返回 SHA-512 串
     *
     * @param strText
     * @return
     */
    public static String sha512(final String strText) {
        return SHA(strText, "SHA-512");
    }
    /**
     * 传入文本内容，返回 SHA-256 串
     *
     * @param strText
     * @return
     */
    public static String sha256(final String strText) {
        return SHA(strText, "SHA-256");
    }

    /**
     * 将字节数组转换成16进制字符串
     * @param bytes 即将转换的数据
     * @return 16进制字符串
     */
    private static String bytesToHexString(byte[] bytes){
        StringBuffer sb = new StringBuffer(bytes.length);
        String temp = null;
        for (int i = 0;i< bytes.length;i++){
            temp = Integer.toHexString(0xFF & bytes[i]);
            if (temp.length() <2){
                sb.append(0);
            }
            sb.append(temp);
        }
        return sb.toString();
    }

}
