package org.gpd.dicenetwork.utils;

import java.util.Random;

public class StringUtils {

    /**
     * 生成四位长度的随机码
     * @param
     * @return
     */
    public static String generateRandomCode() {
        // 生成一个包含所有可能字符的字符串
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        // 生成一个四位数的字符数组
        char[] code = new char[4];

        Random random = new Random();
        // 循环四次，每次从字符串中随机选择一个字符
        for (int i = 0; i < 4; i++) {
            int index = random.nextInt(characters.length());
            code[i] = characters.charAt(index);
        }

        // 将字符数组转换为字符串
        return new String(code);
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
