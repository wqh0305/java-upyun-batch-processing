package com.upyun.utils;

import com.upyun.RestManager;
import com.upyun.UpException;
import com.upyun.pojo.TotalFile;
import okhttp3.Response;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Pattern;

/***
 * 基于这个类来实现多个批量处理操作
 */
public class FileList {

    public static TotalFile getList(String dirUri, RestManager manager) throws IOException, UpException {
        TotalFile totalFile = new TotalFile();
        int fileNum = 0;
        int dirNum = 0;
        double fileSize = 0;
        ArrayList<String> fileList = new ArrayList<>();
        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.add(dirUri);
        Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");

        do {
            String temp = linkedList.removeFirst();
            String type = manager.getFileInfo(temp).headers().get("x-upyun-file-type");//文件类型 文件 x-upyun-file-type: file  , 目录 x-upyun-file-type: folder
            assert type != null;
            if ("folder".equals(type)) {
                String[] fileArr = body2Arr(manager.readDirIter(temp, null));
                if ("/".equals(temp)) temp = "";
                if (fileArr == null) {
                    continue;
                }
                for (String fileName : fileArr) {
                    String filePath = temp + "/" + fileName;
                    if ( pattern.matcher(fileName).find() || fileName.contains(" ")){
                        filePath = temp + "/" + URLEncoder.encode(fileName, "UTF-8").replace("+", "%20"); //中文转码 替换空格
                    }
                    if ("folder".equals(manager.getFileInfo(filePath).headers().get("x-upyun-file-type"))) {
                        dirNum++;
                        linkedList.add(filePath);
                    } else {
                        fileNum++;
                        fileSize += (Double.parseDouble(Objects.requireNonNull(manager.getFileInfo(filePath).headers().get("x-upyun-file-size"))));
                        fileList.add(filePath);
                    }
                }
            }
        } while (!linkedList.isEmpty());
        totalFile.setFileList(fileList);
        totalFile.setFileNum(fileNum);
        totalFile.setDirNum(dirNum);
        totalFile.setFileSize(fileSize);
        return totalFile;
    }

    private static String[] body2Arr(Response res) throws IOException {
        String str = Objects.requireNonNull(res.body()).string();
        if (str.isEmpty()) {
            return null;
        }
        String[] arr = new String[]{};
        arr = str.split("\n");
        for (int i = 0; i < arr.length; i++) {
            arr[i] = arr[i].substring(0, arr[i].indexOf("\t"));
        }
        return arr;
    }
}
