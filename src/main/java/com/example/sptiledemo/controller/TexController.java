package com.example.sptiledemo.controller;

import com.example.sptiledemo.bean.RunningWater;
import com.example.sptiledemo.common.TxtReader;
import com.example.sptiledemo.mapper.RunningWaterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.ldap.PagedResultsControl;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

@RestController
public class TexController {
    @Autowired
    private RunningWaterMapper runningWaterMapper;

    private static String outXlsPath = "D:\\BaiduNetdiskDownload\\15-16年手机数据\\11"; // 生成路径

    public static void main(String[] args) {
        List<String> list = TxtReader.find(outXlsPath, null,null);
        System.out.println("读取到的文件数量："+list.size());
        for (String s:list){
            List<String> read = TxtReader.read(s, "UTF-8", null);
            for (String r:read){
                if (r.contains("1030680954")||r.contains("15703413642")||r.contains("18234494592")||r.contains("18835067496")
                        ||r.contains("312054439")
                        ||r.contains("1003986591")
                        ||r.contains("445775343")
                        ||r.contains("1849551538")
//                        ||r.contains("wangying")
                        ||r.contains("wuxuyang")
                ){
                    System.out.println(r);
//                    RunningWater runningWater=new RunningWater();
//                    runningWater.setCode(r);
//                    runningWaterMapper.save(runningWater);
                }
            }
        }
        System.out.println("扫描结束");
    }

    @GetMapping("ms")
    public String readTxt() {
        List<String> list = TxtReader.find(outXlsPath, null,null);
        System.out.println("读取到的文件数量："+list.size());
        for (String s:list){
            List<String> read = TxtReader.read(s, "UTF-8", null);
            for (String r:read){
                if (r.contains("1030680954")||r.contains("15703413642")||r.contains("18234494592")||r.contains("18835067496")
                        ||r.contains("312054439")
                        ||r.contains("1003986591")
                        ||r.contains("445775343")
                        ||r.contains("1849551538")
//                        ||r.contains("wangying")
                        ||r.contains("wuxuyang")
                        ){
                    System.out.println(r);
//                    RunningWater runningWater=new RunningWater();
//                    runningWater.setCode(r);
//                    runningWaterMapper.save(runningWater);
                }
            }
        }
            return "ok";
    }
}
