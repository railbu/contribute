package com.iyingdi.utils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.util.Calendar;

/**
 * Created by leim on 2015/11/24.
 */
public class EncrypAES {

    private static final String AES = "AES";

    /**
     * 加密key需16位，除去 - 和两位月份，还有13位随机字符串，即CRYPT_KEY
     */
    private static final String CRYPT_KEY = "o[Ui%iY*g+ifa";

    /**
     * 加密
     */
    public static byte[] encrypt(byte[] src, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(AES);
        SecretKeySpec securekey = new SecretKeySpec(key.getBytes(), AES);
        cipher.init(Cipher.ENCRYPT_MODE, securekey);//设置密钥和加密形式
        return cipher.doFinal(src);
    }

    /**
     * 解密
     */
    public static byte[] decrypt(byte[] src, String key)  throws Exception  {
        Cipher cipher = Cipher.getInstance(AES);
        SecretKeySpec securekey = new SecretKeySpec(key.getBytes(), AES);//设置加密Key
        cipher.init(Cipher.DECRYPT_MODE, securekey);//设置密钥和解密形式
        return cipher.doFinal(src);
    }

    /**
     * 二行制转十六进制字符串
     */
    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            }else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }

    public static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0) {
            throw new IllegalArgumentException("长度不是偶数");
        }
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    /**
     * 解密
     */
    public final static String decrypt(String data) {
        try {
            Calendar calendar = Calendar.getInstance();
            int month = calendar.get(Calendar.MONTH);
            String key = CRYPT_KEY +  "-" + (month < 10 ? "0" : "") + month;
            return new String(decrypt(hex2byte(data.getBytes()),
                    key));
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 加密
     */
    public final static String encrypt(String data) {
        try {
            Calendar calendar = Calendar.getInstance();
            int month = calendar.get(Calendar.MONTH);
            String key = CRYPT_KEY +  "-" + (month < 10 ? "0" : "") + month;
            return byte2hex(encrypt(data.getBytes(), key));
        } catch (Exception e) {
        }
        return null;
    }


//    public static void main(String[] args) {
//        String ID = "1234";
//        String idEncrypt = encrypt(ID);
//        System.out.println(idEncrypt);
//        String idDecrypt = decrypt(idEncrypt);
//        System.out.println(idDecrypt);
//        System.out.println(decrypt("EF73C07DBE1E2B597D05061A5FA9CA3F"));
//
//    }

//    public static void main(String[] args) {
//        System.out.println(decrypt("E5E4137DC6FE3D25718DB932012A1D4B"));
//    }




}
