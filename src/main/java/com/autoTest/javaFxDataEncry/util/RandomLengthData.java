package com.autoTest.javaFxDataEncry.util;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * 描述:
 *
 * @since 2019年7月16日 下午3:46:47
 * @author
 */
public class RandomLengthData {
    public static void main(String[] args) {

        System.out.println(getRandomString(10, 10));
        // System.out.println(getOneRandomChar());
    }

    /**
     * 随机汉字构建(随机长度)
     *
     * @param param
     *            长度在0-param 之间的汉字
     * @param length
     *            长度为length长的汉字
     * @return [length,(length+param)]区间的汉字
     */
    public static String getRandomString(int param, int length) {

        Random random = new Random();

        // 长度为[length,length+param]
        int rn = random.nextInt(param) + length;

        StringBuilder ret = new StringBuilder();

        for (int i = 0; i < rn; i++) {
            ret.append(getOneRandomChar());
        }

        return ret.toString();
    }

    /**
     * 随机汉字生成
     */
    public static char getOneRandomChar() {

        String str = "";
        int hightPos;
        int lowPos;

        Random random = new Random();

        // 两个数字与GBK编码的知识相关，此处不做详解，直接百度GBK编码即可
        hightPos = (176 + random.nextInt(39));
        lowPos = (161 + random.nextInt(93));

        byte[] b = new byte[2];
        b[0] = (Integer.valueOf(hightPos)).byteValue();
        b[1] = (Integer.valueOf(lowPos)).byteValue();

        try {

            // 将字节转化为汉字
            str = new String(b, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return str.charAt(0);
    }

}

