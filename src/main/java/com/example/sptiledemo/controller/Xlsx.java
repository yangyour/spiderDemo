package com.example.sptiledemo.controller;

import com.alibaba.fastjson.JSON;
import com.example.sptiledemo.bean.As1;
import com.example.sptiledemo.bean.As2;
import com.example.sptiledemo.bean.As3;
import com.example.sptiledemo.bean.Reader;
import org.apache.poi.xssf.usermodel.*;

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

public class Xlsx {
    private static String desXlsPath = "C:\\Users\\31205\\Desktop\\上海、深圳\\深圳发票2017-2020.xlsx"; // 原文件路径
    private static String outXlsPath = "C:\\Users\\31205\\Desktop\\上海、深圳\\深圳发票2017-2020整理.xlsx"; // 生成路径

    //对数据源Excel进行处理
    public static List<Reader> getExcel() {
        Map<String, Object> map = new HashMap<String, Object>();
        // 添加所需读取的文件
        List<Reader> readerList = new ArrayList<>();
        try {
            XSSFWorkbook XSSFWorkbook = new XSSFWorkbook(new FileInputStream(desXlsPath));
            //所有年份的4个sheet
            XSSFSheet sheet = XSSFWorkbook.getSheetAt(0);
            //解析出excel中所需的数据
            List<Reader> readers = getSheet(sheet);
            readerList.addAll(readers);
            //检测数据量是否正确
//                System.out.println("值"+readerList.size());
//            System.out.println(JSON.toJSON(readerList));
            XSSFWorkbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readerList;
    }

    //读取每一个sheet的数据放入List中
    private static List<Reader> getSheet(XSSFSheet sheet) {
        List<Reader> readers = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("0");
        if (null != sheet) {
            for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                XSSFRow row2 = sheet.getRow(i);
                Reader reader = new Reader();
                if (null != row2.getCell(1) && !row2.getCell(1).toString().equals("发票号码")&&!row2.getCell(13).toString().equals("空白作废")) {
                    if (null != row2.getCell(1) && !row2.getCell(1).toString().equals("")) {
                        String num = row2.getCell(1).toString();
                        reader.setNum(num);
                    }
                    if (null != row2.getCell(2) && !row2.getCell(2).toString().equals("")) {
                        String name = row2.getCell(2).toString();
                        reader.setName(name);
                    }
                    if (null != row2.getCell(6) && !row2.getCell(6).toString().equals("")) {
                        String d = row2.getCell(6).toString();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM
                        Date date=null;
                        try {
                            date= simpleDateFormat.parse(d);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");//HH:mm:ss
                        String format = df2.format(date);
                        reader.setTime(format);
                    }
                    if (null != row2.getCell(18) && !row2.getCell(18).toString().equals("")) {
                        String amount = row2.getCell(18).toString();
                        BigDecimal bd = new BigDecimal(amount);
                        reader.setAmount(bd);
                    }
                }
                readers.add(reader);
            }
        }
        return readers;
    }

    //对获取的List进行第一步处理，按公司名分组
    public static Map<String, List<Reader>> dealWithReader(List<Reader> readers) {
        Map<String, List<Reader>> map = new HashMap<>();
        for (Reader reader : readers) {
            String key = reader.getName();
            if (map.containsKey(key)) {
                //map中存在以此name作为的key，将数据存放当前key的map中
                map.get(key).add(reader);
            } else {
                //map中不存在以此id作为的key，新建key用来存放数据
                List<Reader> readerList = new ArrayList<>();
                readerList.add(reader);
                map.put(key, readerList);
            }
        }
        return map;
    }

    public static void dealWithMap(Map<String, List<Reader>> readerMap) {
        int row = 1;
        List<As1> as1s = new ArrayList<>();
        for (Map.Entry<String, List<Reader>> entry : readerMap.entrySet()) {
            String mapKey = entry.getKey();
            List<Reader> readers = entry.getValue();
            Map<String, List<Reader>> map = new HashMap<>();
            for (Reader reader : readers) {
                String key = reader.getTime();
                if (map.containsKey(key)) {
                    //map中存在以此name作为的key，将数据存放当前key的map中
                    map.get(key).add(reader);
                } else {
                    //map中不存在以此id作为的key，新建key用来存放数据
                    List<Reader> readerList = new ArrayList<>();
                    readerList.add(reader);
                    map.put(key, readerList);
                }
            }
            //遍历并根据日期对数据进行排序
            As1 as1 = makeList(mapKey, map, row);
            as1s.add(as1);
            row++;
        }
        XSSFWorkbook sheets = writeMap(as1s);
        overExcel(sheets);
    }

    private static As1 makeList(String mapKey, Map<String, List<Reader>> map, int row) {
        As1 as1 = new As1();
        as1.setName(mapKey);
        as1.setRows(row);
        List<As2> as2s = new ArrayList<>();
        Set<String> entrySet = map.keySet();    //获取map集合的所有键的Set集合（于Set集合中无序存放）
        List<String> list = new ArrayList<String>(entrySet);    //新建List集合获取Set集合的所有元素（键对象）（顺序与Set集合一样）

        Collections.sort(list);
        for (int i = 0; i < list.size(); i++) {
            As2 as2 = new As2();
            String key = list.get(i);
            List<Reader> value = map.get(key);
//            List<Reader> newValue = deList(value);
            as2.setTime(key);
            List<As3> as3s = new ArrayList<>();
            for (Reader reader : value) {
                As3 as3 = new As3();
                as3.setNum(reader.getNum());
//                System.out.println(reader.getNum());
                as3.setAmount(reader.getAmount());
                as3s.add(as3);
            }
            as2.setAs3s(as3s);
            as2s.add(as2);
        }
        as1.setAs2s(as2s);
        return as1;
    }

    //    去重
    private static List<As2> deList(List<As2> list) {
        list = list.stream().distinct().collect(Collectors.toList());
        return list;
//        List<String> listNew = new ArrayList<String>();
//        for (String str : list) {
//            if (!listNew.contains(str)) {
//                listNew.add(str);
//            }
//        }
//        return listNew ;
    }

    private static Integer countMap(Map<String, List<Reader>> readerMap) {
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, List<Reader>> entry : readerMap.entrySet()) {
//            list.add(entry.getValue().size());
            if (entry.getValue().size() == 985) {
                Map<String, List<Reader>> map = new HashMap<>();
                for (Reader reader : entry.getValue()) {
                    String key = reader.getTime();
                    if (map.containsKey(key)) {
                        //map中存在以此name作为的key，将数据存放当前key的map中
                        map.get(key).add(reader);
                    } else {
                        //map中不存在以此id作为的key，新建key用来存放数据
                        List<Reader> readerList = new ArrayList<>();
                        readerList.add(reader);
                        map.put(key, readerList);
                    }
                }
                for (Map.Entry<String, List<Reader>> e : map.entrySet()) {
                    list.add(e.getKey());
//                    System.out.println(e.getKey()+":"+JSON.toJSON(e));
                }
            }
        }
        return list.size();
    }

    private static void WriteExcel(Map<String, List<Reader>> map, String name) {
        Set<String> entrySet = map.keySet();    //获取map集合的所有键的Set集合（于Set集合中无序存放）
        List<String> list = new ArrayList<String>(entrySet);    //新建List集合获取Set集合的所有元素（键对象）（顺序与Set集合一样）
        Collections.sort(list);
        for (int i = 0; i < list.size(); i++) {
            String key = list.get(i);
            List<Reader> value = map.get(key);
            System.out.println(name + "key:" + key + "-->value:" + JSON.toJSON(value));
            //往建好的excel中插入数据
        }

    }

    private static XSSFWorkbook writeMap(List<As1> as1s) {
//        System.out.println("值"+JSON.toJSON(as1s));
        XSSFWorkbook excel = createExcel();
        XSSFSheet sheet = excel.getSheetAt(0); //获取到工作表，因为一个excel可能有多个工作表
        XSSFRow row;
        removeAs1(as1s);
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

    private static void removeAs1(List<As1> as1s) {
        Iterator<As1> iterator = as1s.iterator();
        while (iterator.hasNext()) {
            List<BigDecimal> bigDecimals = new ArrayList<>();
            As1 next = iterator.next();
            List<As2> as2s = next.getAs2s();
            for (As2 as2 : as2s) {
                List<As3> as3s = as2.getAs3s();
                Iterator<As3> as3sIterator = as3s.iterator();
                while (as3sIterator.hasNext()) {
                    As3 as3 = as3sIterator.next();
                    if (as3.getAmount() != null) {
                        if (as3.getAmount().doubleValue() < 0) {
                            if (next.getName().equals("成都国光电气股份有限公司")){
                                System.out.println(as3.getAmount());
                            }
                            bigDecimals.add(as3.getAmount().abs());
                            as3sIterator.remove();
                        }
                    }
                }
            }
            for (BigDecimal bigDecimal:bigDecimals){
                for (As2 as2:as2s){
                    List<As3> as3s = as2.getAs3s();
                    Iterator<As3> as3sIterator = as3s.iterator();
                    while (as3sIterator.hasNext()) {
                        As3 as3 = as3sIterator.next();
                        if (as3.getAmount() != null) {
                            if (as3.getAmount().equals(bigDecimal)) {
                                as3sIterator.remove();
                            }
                        }
                    }
                }
            }
        }
    }

    private static void removeAs2(List<As2> as2s) {
        BigDecimal lastValue = null;
        BigDecimal afterValue = null;
        List<BigDecimal> bigDecimals = new ArrayList<>();
        Iterator<As2> iterator = as2s.iterator();
        while (iterator.hasNext()) {
            As2 as2 = iterator.next();
            List<As3> as3s = as2.getAs3s();
            Iterator<As3> as3sIterator = as3s.iterator();
            while (as3sIterator.hasNext()) {
                As3 as3 = as3sIterator.next();
                if (as3.getAmount() != null) {
                    if (as3.getAmount().doubleValue() < 0) {
                        lastValue = as3.getAmount().abs();
                        bigDecimals.add(as3.getAmount().abs());
                        as3sIterator.remove();
                    }
                }
//                if (lastValue!=null){
//                    Iterator<As2> iterator1 = as2s.iterator();
//                    while (iterator1.hasNext()){
//                        int i=0;
//                        As2 as2one=iterator1.next();
//                        List<As3> as3sOne = as2one.getAs3s();
//                        Iterator<As3> as3sOneIterator = as3sOne.iterator();
//                        while (as3sOneIterator.hasNext()){
//                            As3 next = as3sOneIterator.next();
//                            System.out.println(next.getAmount());
//                            if (next.getAmount().equals(lastValue)){
//                                i=1;
//                                as3sOneIterator.remove();
//                                System.out.println("删除执行了！");
//                                break;
//                            }
//                        }
//                        if (i==1){
//                            lastValue=null;
//                            break;
//                        }
//                    }
//                }
            }
        }
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
            sheet.setColumnWidth(i, 60 * 200);
        }


        //给列设置单元格样式
        for (int i = 0; i < 100; i++) {
            sheet.setDefaultColumnStyle(i, cellStyle);
        }


        XSSFRow row1 = sheet.createRow(0);
        XSSFCell cell10 = row1.createCell(0);
        cell10.setCellValue("名称");
        for (int i = 0; i < 31; i++) {
            XSSFCell cell11 = row1.createCell(1 + (3 * i));
            cell11.setCellValue("第" + (i + 1) + "次开票时间");
            XSSFCell cell12 = row1.createCell(2 + (3 * i));
            cell12.setCellValue("第" + (i + 1) + "次开票发票号码");
            XSSFCell cell13 = row1.createCell(3 + (3 * i));
            cell13.setCellValue("第" + (i + 1) + "次开票金额");
        }

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
        List<Reader> excel = getExcel();
        Map<String, List<Reader>> stringListMap = dealWithReader(excel);
        dealWithMap(stringListMap);
//        createExcel();
    }

}