package com.upyun.utils;

import com.upyun.UpYunUtils;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/***
 * 同服务下复制或移动文件
 */
public class MFCopy {
    private final static String host = "http://v0.api.upyun.com";

    public static Integer copyFile(String uri, String source, String operator, String secret) throws Exception {
        String date = getRfc1123Time();
        URL url = new URL(host + uri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setUseCaches(false);
        conn.setDoOutput(true);
        conn.setRequestProperty("Authorization", UpYunUtils.sign("PUT", date, uri, operator, secret,""));
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
        conn.setRequestProperty("Authorization", UpYunUtils.sign("PUT",date,uri,operator,secret,""));
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

    public static String getRfc1123Time() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(calendar.getTime());
    }
}