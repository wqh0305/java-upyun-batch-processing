package com.upyun;

import com.upyun.pojo.TotalFile;
import com.upyun.utils.FileList;
import com.upyun.utils.MFCopy;
import okhttp3.Response;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class AllMethodsDemo {
    public static void main(String[] args) throws Exception {
        RestManager manager = new RestManager("服务名", "操作员", "操作员密码");
        String dirUri = "/"; //目录路径
        String uri = "/";  //目标路径    /<save_as_dir>
        String source = "/"; //源文件  /<source_to_dir>

//        toGetFilesList(dirUri,manager); //统计目录下的文件和目录
//        deleteFiles(dirUri,manager); //删除目录下的文件
//        copyDir(uri,source,manager); //复制文件夹
//        moveDir(uri,source,manager); //移动文件夹
    }

    /***
     * 输出所有文件并统计文件和目录数量，总大小
     * @param dirUri 文件夹路径
     */
    private static void toGetFilesList(String dirUri, RestManager manager) throws IOException, UpException {
        TotalFile totalFile = FileList.getList(dirUri, manager);
        List<String> fileList = totalFile.getFileList();
        for (String filePath : fileList) {
            System.out.println(filePath);
        }
        System.out.println("文件数量：" + totalFile.getFileNum());
        System.out.println("目录数量：" + totalFile.getDirNum());
        BigDecimal bigDecimal = new BigDecimal(totalFile.getFileSize());
        System.out.println("总大小(单位 byte)：" + bigDecimal);
    }

    /***
     * 批量删除目录下所有文件，包括子目录下文件
     * @param dirUri 文件夹路径
     */
    private static void deleteFiles(String dirUri, RestManager manager) throws IOException, UpException {
        TotalFile totalFile = FileList.getList(dirUri, manager);
        List<String> fileList = totalFile.getFileList();
        for (String filePath : fileList) {
            Response response = manager.deleteFile(filePath, null);
            System.out.println(filePath);
            System.out.println(Objects.requireNonNull(response.body()).string());
        }
    }

    /***
     * 文件复制
     * @param uri  目标路径
     * @param source 源文件路径
     * @param manager RestManager
     * @throws Exception
     */
    private static void copyDir(String uri, String source, RestManager manager) throws Exception {
        TotalFile totalFile = FileList.getList(source, manager);
        List list = totalFile.getFileList();
        String bucketName = manager.bucketName;
        for (int i = 0; i < list.size(); i++) {
            String sourceFileUri = "/" + bucketName + list.get(i);
            String toFileUri = "/" + bucketName + uri + list.get(i);
            Integer integer = MFCopy.copyFile(toFileUri, sourceFileUri, manager.userName, manager.password);
            System.out.println("Source：" + sourceFileUri + "  CopyTo：" + toFileUri + "   Code：" + integer);
        }
    }

    /***
     * 文件移动
     * @param uri  目标路径
     * @param source 源文件路径
     * @param manager RestManager
     * @throws Exception
     */
    private static void moveDir(String uri, String source, RestManager manager) throws Exception {
        TotalFile totalFile = FileList.getList(source, manager);
        List list = totalFile.getFileList();
        String bucketName = manager.bucketName;
        for (int i = 0; i < list.size(); i++) {
            String sourceFileUri = "/" + bucketName + list.get(i);
            String toFileUri = "/" + bucketName + uri + list.get(i);
            Integer integer = MFCopy.moveFile(toFileUri, sourceFileUri, manager.userName, manager.password);
            System.out.println("Source：" + sourceFileUri + "  MoveTo：" + toFileUri + "   Code：" + integer);
        }
    }
}

