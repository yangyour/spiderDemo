package com.example.sptiledemo.service;

import com.alibaba.fastjson.JSON;
import com.example.sptiledemo.bean.*;
import com.example.sptiledemo.mapper.StockMapper;
import com.example.sptiledemo.mapper.TimeListMapper;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC;

public class ContractExcelService {
    private static String desXlsPath = "D:\\learn\\数据处理文件\\原始文件\\期初合同导入模板(荣大商务2017-2019)科技处理(最终版)(1).XLS"; // 原文件路径
    private static String timeXlsPath = "D:\\learn\\数据处理文件\\原始文件\\时间.XLS"; // 原文件路径
    private static String outXlsPath = "D:\\learn\\数据处理文件\\数据输出文件\\ceshi.xlsx"; // 生成路径

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
            HSSFWorkbook HSSFWorkbook = new HSSFWorkbook(new FileInputStream(desXlsPath));
            //所有年份的4个sheet
            HSSFSheet sheet = HSSFWorkbook.getSheetAt(1);
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
            HSSFWorkbook HSSFWorkbook = new HSSFWorkbook(new FileInputStream(timeXlsPath));
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
                        if (null != row2.getCell(4) && !row2.getCell(4).toString().equals("")) {
                            String name = row2.getCell(4).toString();
                            timeList.setName(name);
                        }
                        if (null != row2.getCell(29) && !row2.getCell(29).toString().equals("")) {
                            if (row2.getCell(29).getCellType() == CELL_TYPE_NUMERIC) {
                                Date d = (Date) row2.getCell(29).getDateCellValue();
                                DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");//HH:mm:ss
                                String format = df2.format(d);
                                timeList.setTime(format);
                            } else {
                                String d = row2.getCell(29).toString();
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
                    if (null != row2.getCell(1) && !row2.getCell(1).toString().equals("")) {
                        String name = row2.getCell(1).toString();
                        contract.setName(name);
                    }
                    if (null != row2.getCell(2) && !row2.getCell(2).toString().equals("")) {
                        String conName = row2.getCell(2).toString();
                        contract.setConName(conName);
                    }
                    if (null != row2.getCell(4) && !row2.getCell(4).toString().equals("")) {
                        String status = row2.getCell(4).toString();
                        contract.setStatus(status);
                    }
                    if (null != row2.getCell(5) && !row2.getCell(5).toString().equals("")) {
                        if (row2.getCell(5).getCellType() == CELL_TYPE_NUMERIC) {
                            Date d = (Date) row2.getCell(5).getDateCellValue();
                            DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");//HH:mm:ss
                            String format = df2.format(d);
                            contract.setTime(format);
                        } else {
                            String d = row2.getCell(5).toString();
                            contract.setTime(d);
                        }
                    }
                    if (null != row2.getCell(6) && !row2.getCell(6).toString().equals("")) {
                        String type = row2.getCell(6).toString();
                        contract.setType(type);
                    }
                    if (null != row2.getCell(8) && !row2.getCell(8).toString().equals("")) {
                        String printing = row2.getCell(8).toString();
                        contract.setPrinting(printing);
                    }
                    if (null != row2.getCell(9) && !row2.getCell(9).toString().equals("")) {
                        String manuscript = row2.getCell(9).toString();
                        contract.setManuscript(manuscript);
                    }
                    if (null != row2.getCell(10) && !row2.getCell(10).toString().equals("")) {
                        String feasibility = row2.getCell(10).toString();
                        contract.setFeasibility(feasibility);
                    }
                    if (null != row2.getCell(11) && !row2.getCell(11).toString().equals("")) {
                        String letterApproved = row2.getCell(11).toString();
                        contract.setLetterApproved(letterApproved);
                    }
                    if (null != row2.getCell(12) && !row2.getCell(12).toString().equals("")) {
                        String finance = row2.getCell(12).toString();
                        contract.setFinance(finance);
                    }
                    if (null != row2.getCell(13) && !row2.getCell(13).toString().equals("")) {
                        String els = row2.getCell(13).toString();
                        contract.setEls(els);
                    }
                    if (null != row2.getCell(14) && !row2.getCell(14).toString().equals("")) {
                        String yxz = row2.getCell(14).toString();
                        contract.setYxz(yxz);
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
            HSSFSheet sheet = HSSFWorkbook.getSheetAt(4);
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
                        runningWater.setOne(one);
                    }
                    if (null != row2.getCell(4) && !row2.getCell(4).toString().equals("")) {
                        String two = row2.getCell(4).toString();
                        runningWater.setTwo(two);
                    }
                    if (null != row2.getCell(5) && !row2.getCell(5).toString().equals("")) {
                        String three = row2.getCell(5).toString();
                        runningWater.setThree(three);
                    }
                    if (null != row2.getCell(6) && !row2.getCell(6).toString().equals("")) {
                        String four = row2.getCell(6).toString();
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
        overExcel(createExcel());
//        Map<String, List<Reader>> stringListMap = dealWithReader(excel);
//        dealWithMap(stringListMap);
//        createExcel();
    }
}
