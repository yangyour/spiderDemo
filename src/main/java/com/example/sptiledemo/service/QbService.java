package com.example.sptiledemo.service;

import com.example.sptiledemo.bean.*;
import com.example.sptiledemo.mapper.StockMapper;
import com.example.sptiledemo.mapper.TimeListMapper;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC;

public class QbService {
    private static String desXlsPath = "C:\\Users\\31205\\Desktop\\数据处理文件\\7-17\\荣大科技2017-2020全包打包合同台账(1).XLS"; // 原文件路径
    private static String timeXlsPath = "D:\\learn\\数据处理文件\\原始文件\\时间.XLS"; // 原文件路径
    private static String time1XlsPath = "C:\\Users\\31205\\Desktop\\数据\\荣大商务\\全包打包\\荣大商务2017-2019全包打包合同台账.XLS"; // 原文件路径
    private static String outXlsPath = "D:\\learn\\数据处理文件\\数据输出文件\\ceshi.xlsx"; // 生成路径
    private static String osXlsPath = "D:\\learn\\数据处理文件\\数据输出文件\\终止对比.xlsx"; // 生成路径
    private static String seXlsPath = "D:\\learn\\数据处理文件\\原始文件\\非科创版项目终止审核2017-2020(1).xlsx"; // 生成路径

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private TimeListMapper timeListMapper;

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

    //对数据源Excel进行处理
    public static List<Stock> getStockExcel() {
        Map<String, Object> map = new HashMap<String, Object>();
        // 添加所需读取的文件
        List<Stock> readerList = new ArrayList<>();
        try {
            HSSFWorkbook HSSFWorkbook = new HSSFWorkbook(new FileInputStream(time1XlsPath));
            //所有年份的4个sheet
            HSSFSheet sheet = HSSFWorkbook.getSheetAt(2);
            //解析出excel中所需的数据
            List<Stock> readers = getStockSheet(sheet);
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

    private static List<Stock> getStockSheet(HSSFSheet sheet) {
        List<Stock> readers = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("0");
        if (null != sheet) {
            for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                HSSFRow row2 = sheet.getRow(i);
                if (row2 != null) {
                    if (null != row2.getCell(0) && !row2.getCell(0).toString().equals("") && !row2.getCell(0).toString().equals("序号")) {
                        Stock stock = new Stock();
                        if (null != row2.getCell(2) && !row2.getCell(2).toString().equals("")) {
                            String code = row2.getCell(2).toString();
                            stock.setCode(code);
                        }
                        if (null != row2.getCell(3) && !row2.getCell(3).toString().equals("")) {
                            String name = row2.getCell(3).toString();
                            stock.setName(name);
                        }
                        if (null != row2.getCell(4) && !row2.getCell(4).toString().equals("")) {
                            String proportion = row2.getCell(4).toString();
                            stock.setProportion(proportion);
                        }
                        if (null != row2.getCell(5) && !row2.getCell(5).toString().equals("")) {
                            String conCode = row2.getCell(5).toString();
                            stock.setConCode(conCode);
                        }
                        readers.add(stock);
                    }
                }
            }
        }
        return readers;
    }

    //对数据源Excel进行处理
    public static List<TimeList> getTimeListExcel() {
        Map<String, Object> map = new HashMap<String, Object>();
        // 添加所需读取的文件
        List<TimeList> readerList = new ArrayList<>();
        try {
            HSSFWorkbook HSSFWorkbook = new HSSFWorkbook(new FileInputStream(time1XlsPath));
            //所有年份的4个sheet
            HSSFSheet sheet = HSSFWorkbook.getSheetAt(1);
            //解析出excel中所需的数据
            List<TimeList> readers = getTimeListSheet(sheet);
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

    private static List<TimeList> getTimeListSheet(HSSFSheet sheet) {
        List<TimeList> readers = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("0");
        if (null != sheet) {
            for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                HSSFRow row2 = sheet.getRow(i);
                if (row2 != null) {
                    if (null != row2.getCell(0) && !row2.getCell(0).toString().equals("")) {
                        TimeList timeList = new TimeList();
                        if (null != row2.getCell(0) && !row2.getCell(0).toString().equals("")) {
                            String code = row2.getCell(0).toString();
                            timeList.setCode(code);
                        }
                        if (null != row2.getCell(2) && !row2.getCell(2).toString().equals("")) {
                            String name = row2.getCell(2).toString();
                            timeList.setName(name);
                        }
                        if (null != row2.getCell(3) && !row2.getCell(3).toString().equals("")) {
                            if (row2.getCell(3).getCellType() == CELL_TYPE_NUMERIC) {
                                Date d = (Date) row2.getCell(3).getDateCellValue();
                                DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");//HH:mm:ss
                                String format = df2.format(d);
                                timeList.setTime(format);
                            } else {
                                String d = row2.getCell(3).toString();
                                timeList.setTime(d);
                            }
                        }
                        readers.add(timeList);
                    }
                }
            }
        }
        return readers;
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
                            contract.setSbTime(format);
                        } else {
                            String d = row2.getCell(19).toString();
                            contract.setSbTime(d);
                        }
                    }

                    if (null != row2.getCell(20) && !row2.getCell(20).toString().equals("")) {
                        if (row2.getCell(20).getCellType() == CELL_TYPE_NUMERIC) {
                            Date d = (Date) row2.getCell(20).getDateCellValue();
                            DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");//HH:mm:ss
                            String format = df2.format(d);
                            contract.setSfTime(format);
                        } else {
                            String d = row2.getCell(20).toString();
                            contract.setSfTime(d);
                        }
                    }

                    if (null != row2.getCell(21) && !row2.getCell(21).toString().equals("")) {
                        if (row2.getCell(21).getCellType() == CELL_TYPE_NUMERIC) {
                            Date d = (Date) row2.getCell(21).getDateCellValue();
                            DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");//HH:mm:ss
                            String format = df2.format(d);
                            contract.setEfTime(format);
                        } else {
                            String d = row2.getCell(21).toString();
                            contract.setEfTime(d);
                        }
                    }

                    if (null != row2.getCell(22) && !row2.getCell(22).toString().equals("")) {
                        if (row2.getCell(22).getCellType() == CELL_TYPE_NUMERIC) {
                            Date d = (Date) row2.getCell(22).getDateCellValue();
                            DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");//HH:mm:ss
                            String format = df2.format(d);
                            contract.setScbTime(format);
                        } else {
                            String d = row2.getCell(22).toString();
                            contract.setScbTime(d);
                        }
                    }

                    if (null != row2.getCell(23) && !row2.getCell(23).toString().equals("")) {
                        if (row2.getCell(23).getCellType() == CELL_TYPE_NUMERIC) {
                            Date d = (Date) row2.getCell(23).getDateCellValue();
                            DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");//HH:mm:ss
                            String format = df2.format(d);
                            contract.setEcbTime(format);
                        } else {
                            String d = row2.getCell(23).toString();
                            contract.setEcbTime(d);
                        }
                    }

                    if (null != row2.getCell(24) && !row2.getCell(24).toString().equals("")) {
                        if (row2.getCell(24).getCellType() == CELL_TYPE_NUMERIC) {
                            Date d = (Date) row2.getCell(24).getDateCellValue();
                            DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");//HH:mm:ss
                            String format = df2.format(d);
                            contract.setShTime(format);
                        } else {
                            String d = row2.getCell(24).toString();
                            contract.setShTime(d);
                        }
                    }

                    if (null != row2.getCell(25) && !row2.getCell(25).toString().equals("")) {
                        if (row2.getCell(25).getCellType() == CELL_TYPE_NUMERIC) {
                            Date d = (Date) row2.getCell(25).getDateCellValue();
                            DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");//HH:mm:ss
                            String format = df2.format(d);
                            contract.setFjTime(format);
                        } else {
                            String d = row2.getCell(25).toString();
                            contract.setFjTime(d);
                        }
                    }

                    if (null != row2.getCell(26) && !row2.getCell(26).toString().equals("")) {
                        if (row2.getCell(26).getCellType() == CELL_TYPE_NUMERIC) {
                            Date d = (Date) row2.getCell(26).getDateCellValue();
                            DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");//HH:mm:ss
                            String format = df2.format(d);
                            contract.setFaTime(format);
                        } else {
                            String d = row2.getCell(26).toString();
                            contract.setFaTime(d);
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


    private static XSSFWorkbook writeMap(List<As1> as1s) {
        XSSFWorkbook excel = createExcel();
        XSSFSheet sheet = excel.getSheetAt(0); //获取到工作表，因为一个excel可能有多个工作表
        XSSFRow row;
        for (As1 as1 : as1s) {
            row = sheet.createRow(as1.getRows()); //在现有行号后追加数据
            //给需要添加数据的列赋值
            row.createCell(0).setCellValue(as1.getName());
            List<As2> as2s = as1.getAs2s();
//            List<As2> as2s1 = deList(as2s);
//            if (as1.getName().equals(""))
            //对as2中无效发票进行处理
//            removeAs2(as2s);
            for (int i = 0; i < as2s.size(); i++) {
                As2 as2 = as2s.get(i);
                row.createCell(1 + (3 * i)).setCellValue(as2.getTime());
                List<As3> as3s = as2.getAs3s();
                String num = getAs3Num(as3s);
                row.createCell(2 + (3 * i)).setCellValue(num);
                BigDecimal amount = getAs3Amount(as3s);
                row.createCell(3 + (3 * i)).setCellValue(String.valueOf(amount));
            }
        }
        return excel;
    }


    private static BigDecimal getAs3Amount(List<As3> as3s) {
        BigDecimal decimal = BigDecimal.valueOf(0);
        for (As3 as3 : as3s) {
            if (as3.getAmount() != null) {
                decimal = decimal.add(as3.getAmount());
            }
        }
        return decimal;
    }

    private static String getAs3Num(List<As3> as3s) {
        String num = "";
        for (As3 as3 : as3s) {
            if (num != null && !num.equals("")) {
                num = num + "、" + as3.getNum();
            } else {
                num = as3.getNum();
            }
        }
        return num;
    }


    public static XSSFWorkbook createExcel() {
        // 声明一个工作薄
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        XSSFFont font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 14);
        font.setBold(true);
        cellStyle.setFont(font);
        cellStyle.setWrapText(true);
        cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);


        XSSFSheet sheet0 = workbook.createSheet("合同概要");


        // 设置表格列宽度
        for (int i = 0; i < 35; i++) {
            sheet0.setColumnWidth(i, 40 * 200);
        }


        //给列设置单元格样式
        for (int i = 0; i < 35; i++) {
            sheet0.setDefaultColumnStyle(i, cellStyle);
        }


        XSSFRow row2 = sheet0.createRow(0);
        XSSFCell c10 = row2.createCell(0);
        c10.setCellValue("合同编码");
        XSSFCell c11 = row2.createCell(1);
        c11.setCellValue("合同名称");
        XSSFCell c12 = row2.createCell(2);
        c12.setCellValue("合同类型编码");
        XSSFCell c13 = row2.createCell(3);
        c13.setCellValue("合同性质");
        XSSFCell c14 = row2.createCell(4);
        c14.setCellValue("参照主合同付款");
        XSSFCell c15 = row2.createCell(5);
        c15.setCellValue("参照主合同付款控制比例%");
        XSSFCell c16 = row2.createCell(6);
        c16.setCellValue("折扣率");
        XSSFCell c17 = row2.createCell(7);
        c17.setCellValue("标的录入方式");
        XSSFCell c18 = row2.createCell(8);
        c18.setCellValue("业务类型");
        XSSFCell c19 = row2.createCell(9);
        c19.setCellValue("所属合同组编码");
        XSSFCell c20 = row2.createCell(10);
        c20.setCellValue("主合同编码");
        XSSFCell c21 = row2.createCell(11);
        c21.setCellValue("对方单位编码");
        XSSFCell c22 = row2.createCell(12);
        c22.setCellValue("对方负责人");
        XSSFCell c23 = row2.createCell(13);
        c23.setCellValue("币种");
        XSSFCell c24 = row2.createCell(14);
        c24.setCellValue("汇率");
        XSSFCell c25 = row2.createCell(15);
        c25.setCellValue("部门编码");
        XSSFCell c26 = row2.createCell(16);
        c26.setCellValue("业务员编码");
        XSSFCell c27 = row2.createCell(17);
        c27.setCellValue("合同签定日期");
        XSSFCell c28 = row2.createCell(18);
        c28.setCellValue("合同开始日期");
        XSSFCell c29 = row2.createCell(19);
        c29.setCellValue("合同结束日期");
        XSSFCell c30 = row2.createCell(20);
        c30.setCellValue("收付款协议编码");
        XSSFCell c31 = row2.createCell(21);
        c31.setCellValue("保修期");
        XSSFCell c32 = row2.createCell(22);
        c32.setCellValue("制单人");
        XSSFCell c33 = row2.createCell(23);
        c33.setCellValue("发运方式编码");
        XSSFCell c34 = row2.createCell(24);
        c34.setCellValue("时效控制环节");
        XSSFCell c35 = row2.createCell(25);
        c35.setCellValue("时效控制方式");
        XSSFCell c36 = row2.createCell(26);
        c36.setCellValue("启用阶段");
        XSSFCell c37 = row2.createCell(27);
        c37.setCellValue("阶段组编码");
        XSSFCell c38 = row2.createCell(28);
        c38.setCellValue("质保金计算方式");
        XSSFCell c39 = row2.createCell(29);
        c39.setCellValue("质保金比例(%)");
        XSSFCell c40 = row2.createCell(30);
        c40.setCellValue("质保金额度");
        XSSFCell c41 = row2.createCell(31);
        c41.setCellValue("质保金开始日期");
        XSSFCell c42 = row2.createCell(32);
        c42.setCellValue("质保金结束日期");
        XSSFCell c43 = row2.createCell(33);
        c43.setCellValue("合同描述");



        XSSFSheet sheet = workbook.createSheet("合同标的");
        // 设置表格列宽度
        for (int i = 0; i < 100; i++) {
            sheet.setColumnWidth(i, 40 * 200);
        }


        //给列设置单元格样式
        for (int i = 0; i < 100; i++) {
            sheet.setDefaultColumnStyle(i, cellStyle);
        }


        XSSFRow row1 = sheet.createRow(0);
        XSSFCell cell10 = row1.createCell(0);
        cell10.setCellValue("合同编码");
        XSSFCell cell11 = row1.createCell(1);
        cell11.setCellValue("公司名称");
        XSSFCell cell12 = row1.createCell(2);
        cell12.setCellValue("来源");
        XSSFCell cell13 = row1.createCell(3);
        cell13.setCellValue("标的名称");
        XSSFCell cell14 = row1.createCell(4);
        cell14.setCellValue("存货分类编码");
        XSSFCell cell15 = row1.createCell(5);
        cell15.setCellValue("对应存货编码");
        XSSFCell cell16 = row1.createCell(6);
        cell16.setCellValue("标的编码");
        XSSFCell cell17 = row1.createCell(7);
        cell17.setCellValue("项目大类");
        XSSFCell cell18 = row1.createCell(8);
        cell18.setCellValue("项目分类编码");
        XSSFCell cell19 = row1.createCell(9);
        cell19.setCellValue("对应项目编码");
        XSSFCell cell20 = row1.createCell(10);
        cell20.setCellValue("项目名称");
        XSSFCell cell21 = row1.createCell(11);
        cell21.setCellValue("数量");
        XSSFCell cell22 = row1.createCell(12);
        cell22.setCellValue("计量单位");
        XSSFCell cell23 = row1.createCell(13);
        cell23.setCellValue("件数");
        XSSFCell cell24 = row1.createCell(14);
        cell24.setCellValue("换算");
        XSSFCell cell25 = row1.createCell(15);
        cell25.setCellValue("辅计量单位");
        XSSFCell cell26 = row1.createCell(16);
        cell26.setCellValue("税率(%)");
        XSSFCell cell27 = row1.createCell(17);
        cell27.setCellValue("折扣率(%)");
        XSSFCell cell28 = row1.createCell(18);
        cell28.setCellValue("无税原币单价");
        XSSFCell cell29 = row1.createCell(19);
        cell29.setCellValue("含税原币单价");
        XSSFCell cell30 = row1.createCell(20);
        cell30.setCellValue("无税原币金额");
        XSSFCell cell31 = row1.createCell(21);
        cell31.setCellValue("含税原币金额");
        XSSFCell cell32 = row1.createCell(22);
        cell32.setCellValue("质保金比例(%)");
        XSSFCell cell33 = row1.createCell(23);
        cell33.setCellValue("质保金开始日期");
        XSSFCell cell34 = row1.createCell(24);
        cell34.setCellValue("质保金结束日期");
        XSSFCell cell35 = row1.createCell(25);
        cell35.setCellValue("执行数量");
        XSSFCell cell36 = row1.createCell(26);
        cell36.setCellValue("执行无税金额原币");
        XSSFCell cell37 = row1.createCell(27);
        cell37.setCellValue("执行含税金额原币");
        XSSFCell cell38 = row1.createCell(28);
        cell38.setCellValue("结束日期");
        XSSFCell cell39 = row1.createCell(29);
        cell39.setCellValue("供应商存货编码");
        XSSFCell cell40 = row1.createCell(30);
        cell40.setCellValue("客户存货编码");
        XSSFCell cell41 = row1.createCell(31);
        cell41.setCellValue("项目状态");
        XSSFCell cell42 = row1.createCell(32);
        cell42.setCellValue("终止时间");
        XSSFCell cell43 = row1.createCell(33);
        cell43.setCellValue("备注");




//        for (int i = 0; i < 31; i++) {
//            XSSFCell cell11 = row1.createCell(1 + (3 * i));
//            cell11.setCellValue("第" + (i + 1) + "次开票时间");
//            XSSFCell cell12 = row1.createCell(2 + (3 * i));
//            cell12.setCellValue("第" + (i + 1) + "次开票发票号码");
//            XSSFCell cell13 = row1.createCell(3 + (3 * i));
//            cell13.setCellValue("第" + (i + 1) + "次开票金额");
//        }

        return workbook;


    }




    public static void overExcel(XSSFWorkbook excel) {
        FileOutputStream ou = null;
        try {
            ou = new FileOutputStream(outXlsPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            excel.write(ou);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setDesPath(String desXlsPaths) throws FileNotFoundException {
        this.desXlsPath = desXlsPaths;
    }


    public static void main(String[] args) {
//        List<Contract> excel = getExcel();
//        List<Stock> stockExcel = getStockExcel();
//        overExcel(createExcel());
//        Map<String, List<Reader>> stringListMap = dealWithReader(excel);
//        dealWithMap(stringListMap);
//        createExcel();
//        List<Contract> excel = getExcel();
//        Set<String> strings = new HashSet<>();
//
//        Set<String> st2 = new HashSet<>();
//        Set<String> test = getTest();
//
//        for (Contract c:excel){
//                strings.add(c.getCode());
//        }
//        System.out.println("原大小"+strings.size());
//        System.out.println("原大小"+test.size());
//        Iterator<String> iterator = strings.iterator();
//        for (String s:strings){
//            if (!test.contains(s)){
//                System.out.println(s);
//            }
//        }
        List<Se> ses=new ArrayList<>();
        Set<String> ssss=new HashSet<>();
        List<Contract> excel1 = getExcel();
        Set<Se> seEx = getSeEx();
        for (Se s:seEx){
            for (Contract contract:excel1){
                if (contract.getStatus()!=null){
                    String sb=null;
                    sb=contract.getName();
                    sb = sb.replaceAll("集团","");
                    sb = sb.replaceAll("有限","");
                    sb = sb.replaceAll("公司","");
                    sb = sb.replaceAll("股份", "");
                    sb = sb.replaceAll("通信", "");
                    sb = sb.replaceAll("技术", "");
                    if (sb.contains(s.getName())){
                        ses.add(s);
                    }else {
                        ssss.add(contract.getName());
                    }
                }
            }

        }
        for (String sssss:ssss){
            System.out.println(sssss);
        }
        XSSFWorkbook excel = createSeExcel();
        XSSFSheet sheet = excel.getSheetAt(0); //获取到工作表，因为一个excel可能有多个工作表
        XSSFRow row;
        Set<String> strings = new HashSet<>();
        Set<String> st2 = new HashSet<>();
//        for (ContractList c : contractList) {
//            if (c.getStatus() != null) {
//                strings.add(c.getCode());
//            }
//            st2.add(c.getCode());
//        }
        for (int i = 0; i < ses.size(); i++) {
            row = sheet.createRow(i + 1); //在现有行号后追加数据
            //给需要添加数据的列赋值
            row.createCell(0).setCellValue(ses.get(i).getName());
            row.createCell(1).setCellValue(ses.get(i).getTime());
        }

        FileOutputStream ou = null;
        try {
            ou = new FileOutputStream(osXlsPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            excel.write(ou);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    //对数据源Excel进行处理
    public static Set<String> getTest() {
        Map<String, Object> map = new HashMap<String, Object>();
        Set<String> readers=new HashSet<>();
        // 添加所需读取的文件
        List<TimeList> readerList = new ArrayList<>();
        try {
            XSSFWorkbook XSSFWorkbook = new XSSFWorkbook(new FileInputStream(outXlsPath));
            //所有年份的4个sheet
            XSSFSheet sheet = XSSFWorkbook.getSheetAt(0);
            //解析出excel中所需的数据
            readers = getTestSheet(sheet);
            //检测数据量是否正确
            XSSFWorkbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readers;

    }


    private static Set<String> getTestSheet(XSSFSheet sheet) {
        Set<String> readers = new HashSet<>();
        DecimalFormat df = new DecimalFormat("0");
        if (null != sheet) {
            for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                XSSFRow row2 = sheet.getRow(i);
                if (row2 != null) {
                    if (null != row2.getCell(0) && !row2.getCell(0).toString().equals("")) {
                        TimeList timeList = new TimeList();
                        if (null != row2.getCell(0) && !row2.getCell(0).toString().equals("")) {
                            String code = row2.getCell(0).toString();
                            readers.add(code);
                        }
                    }
                }
            }
        }
        return readers;
    }

    //对数据源Excel进行处理
    public static Set<Se>  getSeEx() {
        Map<String, Object> map = new HashMap<String, Object>();
        Set<String> readers=new HashSet<>();
        // 添加所需读取的文件
        Set<Se> readerList = new HashSet<>();
        try {
            XSSFWorkbook XSSFWorkbook = new XSSFWorkbook(new FileInputStream(seXlsPath));
            //所有年份的4个sheet
            XSSFSheet sheet = XSSFWorkbook.getSheetAt(0);
            //解析出excel中所需的数据
            readerList = getSeExSheet(sheet);
            //检测数据量是否正确
            XSSFWorkbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readerList;

    }


    private static Set<Se> getSeExSheet(XSSFSheet sheet) {
        Set<Se> readers = new HashSet<>();
        DecimalFormat df = new DecimalFormat("0");
        if (null != sheet) {
            for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                XSSFRow row2 = sheet.getRow(i);
                if (row2 != null) {
                    if (null != row2.getCell(1) && !row2.getCell(1).toString().equals("")) {
                        Se se = new Se();
                        if (null != row2.getCell(1) && !row2.getCell(1).toString().equals("")) {
                            String code = row2.getCell(1).toString();
                            code = code.replaceAll("集团","");
                            code = code.replaceAll("有限","");
                            code = code.replaceAll("公司","");
                            code = code.replaceAll("股份", "");
                            code = code.replaceAll("通信", "");
                            code = code.replaceAll("技术", "");
                            se.setName(code);
                        }
                        if (null != row2.getCell(2) && !row2.getCell(2).toString().equals("")) {
                            if (row2.getCell(2).getCellType() == CELL_TYPE_NUMERIC) {
                                Date d = (Date) row2.getCell(2).getDateCellValue();
                                DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");//HH:mm:ss
                                String format = df2.format(d);
                                se.setTime(format);
                            } else {
                                String d = row2.getCell(2).toString();
                                se.setTime(d);
                            }
                        }
                        readers.add(se);
                    }
                }
            }
        }
        return readers;
    }



    public static XSSFWorkbook createSeExcel() {
        // 声明一个工作薄
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFSheet sheet = workbook.createSheet();

        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        XSSFFont font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 14);
        font.setBold(true);
        cellStyle.setFont(font);
        cellStyle.setWrapText(true);
        cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);

        // 设置表格列宽度
        for (int i = 0; i < 5; i++) {
            sheet.setColumnWidth(i, 40 * 200);
        }


        //给列设置单元格样式
        for (int i = 0; i < 5; i++) {
            sheet.setDefaultColumnStyle(i, cellStyle);
        }


        XSSFRow row1 = sheet.createRow(0);
        XSSFCell cell10 = row1.createCell(0);
        cell10.setCellValue("合同编码");
        XSSFCell cell11 = row1.createCell(1);
        cell11.setCellValue("公司名称");

//        for (int i = 0; i < 31; i++) {
//            XSSFCell cell11 = row1.createCell(1 + (3 * i));
//            cell11.setCellValue("第" + (i + 1) + "次开票时间");
//            XSSFCell cell12 = row1.createCell(2 + (3 * i));
//            cell12.setCellValue("第" + (i + 1) + "次开票发票号码");
//            XSSFCell cell13 = row1.createCell(3 + (3 * i));
//            cell13.setCellValue("第" + (i + 1) + "次开票金额");
//        }

        return workbook;


    }
}

