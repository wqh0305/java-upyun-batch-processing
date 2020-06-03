package com.upyun;

import com.upyun.pojo.TotalFile;
import com.upyun.utils.FileList;

import java.io.IOException;
import java.util.*;

public class AllMethodsDemo {
    public static void main(String[] args) throws IOException, UpException {
        RestManager manager = new RestManager("服务名", "操作员", "操作员密码");
        String dirUri = "/"; //目录路径

    }

    /***
     * 输出所有文件并统计文件和目录数量
     * @param dirUri 文件夹路径
     */
    private static void toGetFilesList(String dirUri, RestManager manager) throws IOException, UpException {
        TotalFile totalFile = FileList.getList(dirUri, manager);
        List list = totalFile.getFileList();
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
        System.out.println("文件数量：" + totalFile.getFileNum());
        System.out.println("目录数量：" + totalFile.getDirNum());
    }

    /***
     * 批量删除目录下所有文件，包括子目录下文件
     * @param dirUri 文件夹路径
     */
    private static void deleteFiles(String dirUri, RestManager manager) throws IOException, UpException {
        TotalFile totalFile = FileList.getList(dirUri, manager);
        List list = totalFile.getFileList();
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
            manager.deleteFile(list.get(i).toString(), null);
        }
    }
}

