package me.hades.yqword.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by hades on 2018/6/9.
 * MD5加密工具类
 * @author hades
 * @version 1.0
 * @see MD5Util
 */

public class MD5Util {


    private static final String SLAT = "yqwordApp";

    public static String encoder(String psw) {
        // 加盐
        psw += SLAT;

        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(psw.getBytes());

            StringBuffer sb = new StringBuffer();
            for (byte b : bytes) {
                int i = b & 0xff;// 获取低8位内容
                String hexString = Integer.toHexString(i);

                if (hexString.length() == 1) {
                    hexString = "0" + hexString;
                }

                sb.append(hexString);
            }

            String md5 = sb.toString();
            return md5;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }
}