package org.gpd.dicenetwork.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class FileManager {

    @Value("${file.directory}")
    private String directory;

    private static final String FILE_NAME_FORMAT = "%s-%s-%d";
    private static final int HASH_LENGTH = 16;
    /**
     * 生成文件名
     *
     * @param timestamp 时间戳
     * @param hash 哈希值
     * @param suffix 尾缀数字
     * @return 文件名
     */
    public String generateFileName(String timestamp, String hash, int suffix) {
        return String.format(FILE_NAME_FORMAT, timestamp, hash.substring(0, HASH_LENGTH), suffix) + ".dat";
    }

    /**
     * 写入文件
     *
     * @param content 文件内容（字节数组）
     * @param timestamp 时间戳
     * @param hash 哈希值
     * @param suffix 尾缀数字
     * @return 文件路径
     * @throws IOException 如果写入文件时发生错误
     */
    public String writeFile(byte[] content, String timestamp, String hash, int suffix) throws IOException {
        String fileName = generateFileName(timestamp, hash, suffix);
        File file = new File(directory, fileName);
        Files.createDirectories(Paths.get(directory));
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content);
        }catch (IOException e){
            e.printStackTrace();
        }
        return fileName;
    }

    /**
     * 读取文件
     *
     * @param filePath 文件路径
     * @return 文件内容（字节数组）
     * @throws IOException 如果读取文件时发生错误
     */
    public byte[] readFile(String filePath) throws IOException {
        return Files.readAllBytes(Paths.get(filePath));
    }
}
