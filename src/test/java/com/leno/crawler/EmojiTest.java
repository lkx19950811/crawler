package com.leno.crawler;

import com.alibaba.fastjson.util.Base64;
import com.github.binarywang.java.emoji.EmojiConverter;
import com.leno.crawler.util.DataUtils;
import com.mysql.jdbc.util.Base64Decoder;
import com.thoughtworks.xstream.core.util.Base64Encoder;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

/**
 * @author lee Cather
 * @date 2018-08-31 13:42
 * desc :
 */
public class EmojiTest {
    private static EmojiConverter emojiConverter = EmojiConverter.getInstance();
    @Test
    public void emojiStrTest() throws UnsupportedEncodingException {
        String emojiStr = "\uD83D\uDE01\uD83D\uDE1A\uD83D\uDE1A\uD83D\uDE3A\uD83D\uDE3A\uD83D\uDE3A\uD83D\uDC68\u200D\uD83D\uDC68\u200D\uD83D\uDC67\u200D\uD83D\uDC66卧槽尼尔吗";
        String result = DataUtils.emojiEncode(emojiStr);
        String base64 = new Base64Encoder().encode(emojiStr.getBytes("UTF8"));
        System.out.println(result);
        System.out.println(emojiStr);
        System.out.println(base64);
        System.out.println(new String(new Base64Encoder().decode(base64),"UTF8"));
    }
}
