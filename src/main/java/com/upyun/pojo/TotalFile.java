package com.upyun.pojo;

import java.util.List;

public class TotalFile {
    private List<String> fileList;
    private int fileNum;
    private int dirNum;

    public TotalFile() {
    }

    public List<String> getFileList() {
        return fileList;
    }

    public void setFileList(List<String> fileList) {
        this.fileList = fileList;
    }

    public int getFileNum() {
        return fileNum;
    }

    public void setFileNum(int fileNum) {
        this.fileNum = fileNum;
    }

    public int getDirNum() {
        return dirNum;
    }

    public void setDirNum(int dirNum) {
        this.dirNum = dirNum;
    }
}
