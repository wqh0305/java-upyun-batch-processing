package com.upyun.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/***
 * 同服务下复制或移动文件
 */
public class MFCopy {
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private final static String host = "http://v0.api.upyun.com";

    public static Integer copyFile(String uri, String source, String operator, String secret) throws Exception {
        String date = getRfc1123Time();
        URL url = new URL(host + uri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setUseCaches(false);
        conn.setDoOutput(true);
        conn.setRequestProperty("Authorization", getAuth(operator, secret, "PUT", uri, date));
        conn.setRequestProperty("X-Upyun-Copy-Source", source);
        conn.setRequestProperty("date", date);
        conn.setRequestProperty("Content-Length", "0");
        //创建连接
        conn.connect();
        OutputStream os = conn.getOutputStream();

        int responseCode = conn.getResponseCode(); //返回的状态码

        if (os != null) {
            os.close();
        }

        if (conn != null) {
            conn.disconnect();
        }

        return responseCode;
    }

    public static Integer moveFile(String uri, String source, String operator, String secret) throws Exception {
        String date = getRfc1123Time();
        URL url = new URL(host + uri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setUseCaches(false);
        conn.setDoOutput(true);
        conn.setRequestProperty("Authorization", getAuth(operator, secret, "PUT", uri, date));
        conn.setRequestProperty("X-Upyun-Move-Source", source);
        conn.setRequestProperty("date", date);
        conn.setRequestProperty("Content-Length", "0");
        //创建连接
        conn.connect();
        OutputStream os = conn.getOutputStream();

        int responseCode = conn.getResponseCode(); //返回的状态码

        if (os != null) {
            os.close();
        }

        if (conn != null) {
            conn.disconnect();
        }

        return responseCode;
    }

    /**
     * 获取签名
     *
     * @param operator
     * @param secret
     * @param method
     * @param uri
     * @param date
     * @return 签名
     */
    private static String getAuth(String operator, String secret, String method, String uri, String date) throws Exception {
        //传入的secret为MD5后的值
        return sign(operator, secret, method, uri, date, "", "");
    }

    private static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 is unsupported", e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MessageDigest不支持MD5Util", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    private static byte[] hashHmac(String data, String key)
            throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        mac.init(signingKey);
        return mac.doFinal(data.getBytes());
    }

    public static String sign(String key, String secret, String method, String uri, String date, String policy,
                              String md5) throws Exception {
        String value = method + "&" + uri;
        if (date != "") {
            value = value + "&" + date;
        }
        if (policy != "") {
            value = value + "&" + policy;
        }
        if (md5 != "") {
            value = value + "&" + md5;
        }
        byte[] hmac = hashHmac(value, secret);
        String sign = Base64.getEncoder().encodeToString(hmac);
        return "UPYUN " + key + ":" + sign;
    }

    public static String getRfc1123Time() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(calendar.getTime());
    }
}