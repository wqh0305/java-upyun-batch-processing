# java-upyun-batch-processing

- 版本：1.8
- 使用upyun sdk 做的一些批量处理

#### Maven 安装

````
<dependency>
    <groupId>com.upyun</groupId>
    <artifactId>java-sdk</artifactId>
    <version>4.2.0</version>
</dependency>
````



#### 方法介绍

- 统计目录下的文件和子目录数量，以及目录大小，支持"/"

  ````java
  toGetFilesList("目录在云存储中的路径",RestManager);
  ````

- 删除目录下的文件(子目录暂时删除不了)

  ```java
  deleteFiles("目录在云存储中的路径",RestManager);
  ```

- 复制目录到指定目录下

  ```java
  copyDir("目标路径","源路径",RestManager); //如 /a 复制到 /b ，复制完成后，/b 下会有 /a 子目录
  ```

- 移动目录到指定目录下

  ```java
  moveDir("目标路径","源路径",RestManager); //同移动目录
  ```