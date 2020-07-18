package com.example.sptiledemo.common;

import java.util.List;

public class TestTex {
    private static String outXlsPath = "D:\\BaiduNetdiskDownload\\15-16年手机数据\\3W的手机OK.txt"; // 生成路径
    public static void main(String[] args) {
        List<String> read = TxtReader.read(outXlsPath, "UTF-8", null);
        System.out.println(read.get(0));
    }
}
