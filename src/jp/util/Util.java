package jp.util;

import java.security.MessageDigest;

abstract public class Util {
    static public String md5(String s) {
        String hash = "";

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(s.getBytes("CP1252"));
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i)
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            hash = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hash;
    }
}
