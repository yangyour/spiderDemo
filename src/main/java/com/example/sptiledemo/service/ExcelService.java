package com.example.sptiledemo.service;

import com.example.sptiledemo.bean.Contract;
import com.example.sptiledemo.bean.RunningWater;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC;

public class ExcelService {
    private static String desXlsPath = "D:\\输出文件\\荣大商务\\底稿\\荣大商务底稿2017-2020合同台账.xls"; // 原文件路径
    private static String timeXlsPath = "D:\\learn\\数据处理文件\\原始文件\\时间.XLS"; // 原文件路径
    private static String time1XlsPath = "C:\\Users\\31205\\Desktop\\数据\\荣大商务\\全包打包\\荣大商务2017-2019全包打包合同台账.XLS"; // 原文件路径
    private static String outXlsPath = "D:\\learn\\数据处理文件\\数据输出文件\\ceshi.xlsx"; // 生成路径
    private static String osXlsPath = "D:\\learn\\数据处理文件\\数据输出文件\\终止对比.xlsx"; // 生成路径
    private static String seXlsPath = "D:\\learn\\数据处理文件\\原始文件\\非科创版项目终止审核2017-2020(1).xlsx"; // 生成路径

    //对数据源Excel进行处理
    public static List<Contract> getExcel() {
        Map<String, Object> map = new HashMap<String, Object>();
        // 添加所需读取的文件
        List<Contract> readerList = new ArrayList<>();
        try {
            HSSFWorkbook HSSFWorkbook = new HSSFWorkbook(new FileInputStream(desXlsPath));
            //所有年份的4个sheet
            HSSFSheet sheet = HSSFWorkbook.getSheetAt(0);
            //解析出excel中所需的数据
            List<Contract> readers = getSheet(sheet);
            readerList.addAll(readers);
            //检测数据量是否正确
            HSSFWorkbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readerList;
    }


    //读取每一个sheet的数据放入List中
    private static List<Contract> getSheet(HSSFSheet sheet) {
        List<Contract> readers = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("0");
        if (null != sheet) {
            for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                HSSFRow row2 = sheet.getRow(i);
                if (null != row2.getCell(0) && !row2.getCell(0).toString().equals("")) {
                    Contract contract = new Contract();

                    if (null != row2.getCell(0) && !row2.getCell(0).toString().equals("")) {
                        String code = row2.getCell(0).toString();
                        contract.setCode(code);
                    }
                    if (null != row2.getCell(2) && !row2.getCell(2).toString().equals("")) {
                        if (row2.getCell(2).getCellType() == CELL_TYPE_NUMERIC) {
                            Date d = (Date) row2.getCell(2).getDateCellValue();
                            DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");//HH:mm:ss
                            String format = df2.format(d);
                            contract.setqTime(format);
                        } else {
                            String d = row2.getCell(2).toString();
                            contract.setqTime(d);
                        }
                    }
                    if (null != row2.getCell(3) && !row2.getCell(3).toString().equals("")) {
                        String name = row2.getCell(3).toString();
                        contract.setName(name);
                    }
                    if (null != row2.getCell(4) && !row2.getCell(4).toString().equals("")) {
                        String kCode = row2.getCell(4).toString();
                        contract.setkCode(kCode);
                    }
                    if (null != row2.getCell(5) && !row2.getCell(5).toString().equals("")) {
                        String conName = row2.getCell(5).toString();
                        contract.setConName(conName);
                    }

                    if (null != row2.getCell(6) && !row2.getCell(6).toString().equals("")) {
                        String status = row2.getCell(6).toString();
                        contract.setStatus(status);
                    }
                    if (null != row2.getCell(7) && !row2.getCell(7).toString().equals("")) {
                        if (row2.getCell(7).getCellType() == CELL_TYPE_NUMERIC) {
                            Date d = (Date) row2.getCell(7).getDateCellValue();
                            DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");//HH:mm:ss
                            String format = df2.format(d);
                            contract.setTime(format);
                        } else {
                            String d = row2.getCell(7).toString();
                            contract.setTime(d);
                        }
                    }
                    if (null != row2.getCell(8) && !row2.getCell(8).toString().equals("")) {
                        String type = row2.getCell(8).toString();
                        contract.setType(type);
                    }
                    if (null != row2.getCell(10) && !row2.getCell(10).toString().equals("")) {
                        String printing = row2.getCell(10).toString();
                        contract.setPrinting(printing);
                    }
                    if (null != row2.getCell(11) && !row2.getCell(11).toString().equals("")) {
                        String manuscript = row2.getCell(11).toString();
                        contract.setManuscript(manuscript);
                    }
                    if (null != row2.getCell(12) && !row2.getCell(12).toString().equals("")) {
                        String feasibility = row2.getCell(12).toString();
                        contract.setFeasibility(feasibility);
                    }
                    if (null != row2.getCell(13) && !row2.getCell(13).toString().equals("")) {
                        String letterApproved = row2.getCell(13).toString();
                        contract.setLetterApproved(letterApproved);
                    }
                    if (null != row2.getCell(14) && !row2.getCell(14).toString().equals("")) {
                        String finance = row2.getCell(14).toString();
                        contract.setFinance(finance);
                    }
                    if (null != row2.getCell(15) && !row2.getCell(15).toString().equals("")) {
                        String els = row2.getCell(15).toString();
                        contract.setEls(els);
                    }
                    if (null != row2.getCell(16) && !row2.getCell(16).toString().equals("")) {
                        String yxz = row2.getCell(16).toString();
                        contract.setYxz(yxz);
                    }
                    if (null != row2.getCell(17) && !row2.getCell(17).toString().equals("")) {
                        String rYear = row2.getCell(17).toString();
                        contract.setrYear(rYear);
                    }
                    if (null != row2.getCell(18) && !row2.getCell(18).toString().equals("")) {
                        String xYear = row2.getCell(18).toString();
                        contract.setxYear(xYear);
                    }
                    if (null != row2.getCell(19) && !row2.getCell(19).toString().equals("")) {
                        if (row2.getCell(19).getCellType() == CELL_TYPE_NUMERIC) {
                            Date d = (Date) row2.getCell(19).getDateCellValue();
                            DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");//HH:mm:ss
                            String format = df2.format(d);
                            contract.setNhTime(format);
                        } else {
                            String d = row2.getCell(19).toString();
                            contract.setNhTime(d);
                        }
                    }
                    if (null != row2.getCell(20) && !row2.getCell(20).toString().equals("")) {
                        if (row2.getCell(20).getCellType() == CELL_TYPE_NUMERIC) {
                            Date d = (Date) row2.getCell(20).getDateCellValue();
                            DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");//HH:mm:ss
                            String format = df2.format(d);
                            contract.setSbTime(format);
                        } else {
                            String d = row2.getCell(20).toString();
                            contract.setSbTime(d);
                        }
                    }
                    if (null != row2.getCell(21) && !row2.getCell(21).toString().equals("")) {
                        if (row2.getCell(21).getCellType() == CELL_TYPE_NUMERIC) {
                            Date d = (Date) row2.getCell(21).getDateCellValue();
                            DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");//HH:mm:ss
                            String format = df2.format(d);
                            contract.setShTime(format);
                        } else {
                            String d = row2.getCell(21).toString();
                            contract.setShTime(d);
                        }
                    }
                    readers.add(contract);
                }
            }
        }

        return readers;
    }


    public static List<RunningWater> getRunningWaterExcel() {
        Map<String, Object> map = new HashMap<String, Object>();
        // 添加所需读取的文件
        List<RunningWater> readerList = new ArrayList<>();
        try {
            HSSFWorkbook HSSFWorkbook = new HSSFWorkbook(new FileInputStream(desXlsPath));
            //所有年份的4个sheet
            HSSFSheet sheet = HSSFWorkbook.getSheetAt(2);
            //解析出excel中所需的数据
            List<RunningWater> readers = getRunningWaterSheet(sheet);
            readerList.addAll(readers);
            //检测数据量是否正确
            HSSFWorkbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readerList;
    }

    //读取每一个sheet的数据放入List中
    private static List<RunningWater> getRunningWaterSheet(HSSFSheet sheet) {
        List<RunningWater> readers = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("0");
        if (null != sheet) {
            for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                HSSFRow row2 = sheet.getRow(i);
                if (null != row2.getCell(0) && !row2.getCell(0).toString().equals("")) {
                    RunningWater runningWater = new RunningWater();
                    if (null != row2.getCell(0) && !row2.getCell(0).toString().equals("")) {
                        String code = row2.getCell(0).toString();
                        runningWater.setCode(code);
                    }
                    if (null != row2.getCell(3) && !row2.getCell(3).toString().equals("")) {
                        String one = row2.getCell(3).toString();
                        if (one.contains("万")){
                            one=one.replaceAll("万","");
                            Double aDouble = Double.valueOf(one);
                            Double bDouble=aDouble*Double.valueOf(10000);
                            one=df.format(bDouble);
                        }
                        runningWater.setOne(one);
                    }
                    if (null != row2.getCell(4) && !row2.getCell(4).toString().equals("")) {
                        String two = row2.getCell(4).toString();
                        if (two.contains("万")){
                            two=two.replaceAll("万","");
                            Double aDouble = Double.valueOf(two);
                            Double bDouble=aDouble*Double.valueOf(10000);
                            two=df.format(bDouble);
                        }
                        runningWater.setTwo(two);
                    }
                    if (null != row2.getCell(5) && !row2.getCell(5).toString().equals("")) {
                        String three = row2.getCell(5).toString();
                        if (three.contains("万")){
                            three=three.replaceAll("万","");
                            Double aDouble = Double.valueOf(three);
                            Double bDouble=aDouble*Double.valueOf(10000);
                            three=df.format(bDouble);
                        }
                        runningWater.setThree(three);
                    }
                    if (null != row2.getCell(6) && !row2.getCell(6).toString().equals("")) {
                        String four = row2.getCell(6).toString();
                        if (four.contains("万")){
                            four=four.replaceAll("万","");
                            Double aDouble = Double.valueOf(four);
                            Double bDouble=aDouble*Double.valueOf(10000);
                            four=df.format(bDouble);
                        }
                        runningWater.setFour(four);
                    }
                    readers.add(runningWater);
                }
            }
        }
        return readers;
    }

}
