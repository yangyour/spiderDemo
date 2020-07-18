/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.sptiledemo.common;


import com.example.sptiledemo.api.TxtReaderI;
import com.example.sptiledemo.exp.DataRegexException;
import com.example.sptiledemo.impl.TextStingReaderImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Administrator
 */
public class TxtReader {
    private static TxtReaderI reader = null;
    private static TxtReaderI getReader(){
        if(reader == null){
            reader = new TextStingReaderImpl();
        }
        return reader;
    }
    
    public static List<String> read(String fileName, String coding, String regx){
        try {
            return getReader().readStr(fileName, coding,regx);
        } catch (DataRegexException ex) {
            Logger.getLogger(TxtReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    static List<String> list=new ArrayList<>();
    public static List<String> find(String path,String reg,List<String> ls){
        Pattern pat=Pattern.compile("txt");
        File file=new File(path);
        File[] arr=file.listFiles();
        for(int i=0;i<arr.length;i++){
            //判断是否是文件夹，如果是的话，再调用一下find方法
            if(arr[i].isDirectory()){
                find(arr[i].getAbsolutePath(),reg,null);
            }
            Matcher mat=pat.matcher(arr[i].getAbsolutePath());
            //根据正则表达式，寻找匹配的文件
            if(arr[i].getAbsolutePath().contains("txt")){
//                System.out.println(arr[i].getAbsolutePath());
                //这个getAbsolutePath()方法返回一个String的文件绝对路径
                list.add(arr[i].getAbsolutePath());
            }
        }
        return list;
    }

}
