package jp.util;

import java.security.MessageDigest;

abstract public class Util {
    static public String md5(final String s) {
        try {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            final byte[] array = md.digest(s.getBytes("CP1252"));
            final StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public interface Listener {
        void callback(final String s);
    }
}
