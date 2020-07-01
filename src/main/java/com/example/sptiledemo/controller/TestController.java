package com.example.sptiledemo.controller;

import com.alibaba.fastjson.JSON;
import com.example.sptiledemo.bean.*;
import com.example.sptiledemo.mapper.RunningWaterMapper;
import com.example.sptiledemo.mapper.StockMapper;
import com.example.sptiledemo.mapper.TimeListMapper;
import com.example.sptiledemo.service.ContractExcelService;
import org.apache.poi.poifs.filesystem.Ole10Native;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@RestController
public class TestController {


    private static String outXlsPath = "D:\\learn\\数据处理文件\\数据输出文件\\ceshi.xlsx"; // 生成路径

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private TimeListMapper timeListMapper;

    @Autowired
    private RunningWaterMapper runningWaterMapper;

    @GetMapping("test")
    public void test() {
        List<Stock> stocks = ContractExcelService.getStockExcel();
        for (Stock stock : stocks) {
            if (stock.getConCode().contains("，")) {
                String[] split = stock.getConCode().split("，");
                for (int i = 0; i < split.length; i++) {
                    Stock stock1 = new Stock();
                    stock1.setName(stock.getName());
                    stock1.setProportion(stock.getProportion());
                    stock1.setCode(stock.getCode());
                    stock1.setConCode(split[i]);
                    stockMapper.save(stock1);
                }
            } else {
                stockMapper.save(stock);
            }
        }
    }

    @GetMapping("test222")
    public void test222() {
        List<TimeList> timeListExcel = ContractExcelService.getTimeListExcel();
        for (TimeList timeList : timeListExcel) {
            timeListMapper.save(timeList);
        }
    }

    @GetMapping("test333")
    public void test333() {
        List<RunningWater> runningWaterExcel = ContractExcelService.getRunningWaterExcel();
        for (RunningWater runningWater : runningWaterExcel) {
            runningWaterMapper.save(runningWater);
        }
    }

    @GetMapping("test111")
    public void test111() {
        List<Contract> contracts = ContractExcelService.getExcel();
        XSSFWorkbook excel = ContractExcelService.createExcel();
        XSSFSheet sheet = excel.getSheetAt(0); //获取到工作表，因为一个excel可能有多个工作表
        XSSFRow row;
        List<ContractList> contractList = getContractList(contracts);
        for (int i = 0; i < contractList.size(); i++) {
            row = sheet.createRow(i + 1); //在现有行号后追加数据
            //给需要添加数据的列赋值
            row.createCell(0).setCellValue(contractList.get(i).getCode());
            row.createCell(1).setCellValue(contractList.get(i).getName());
            row.createCell(2).setCellValue(contractList.get(i).getSource());
            row.createCell(3).setCellValue(contractList.get(i).getConName());
            row.createCell(4).setCellValue(contractList.get(i).getfCode());
            row.createCell(5).setCellValue(contractList.get(i).getdCode());
            row.createCell(6).setCellValue(contractList.get(i).getbCode());
            row.createCell(11).setCellValue(contractList.get(i).getCount());
            row.createCell(16).setCellValue(contractList.get(i).getTax());
            row.createCell(17).setCellValue(contractList.get(i).getDiscount());
            row.createCell(19).setCellValue(contractList.get(i).getMoney());
            row.createCell(20).setCellValue(contractList.get(i).getInTex());
            row.createCell(21).setCellValue(contractList.get(i).getNoTex());
            row.createCell(28).setCellValue(contractList.get(i).getEndTime());
            row.createCell(31).setCellValue(contractList.get(i).getStatus());
            row.createCell(32).setCellValue(contractList.get(i).getTime());
        }
        overExcel(excel);
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

    public List<ContractList> getContractList(List<Contract> contracts) {
        List<ContractList> contractLists = new ArrayList<>();
        int row = 1;
        for (Contract contract : contracts) {
            if (contract.getStatus() == null) {
                String conName = contract.getConName();
                String[] conSp = conName.split("\\+");
                for (int j = 0; j < conSp.length; j++) {
                    String conNames = conSp[j];
                    conNames = conNames.replaceAll("合同", "");
                    List<Stock> stocks = stockMapper.selectByConCode(contract.getType(), conNames);
                    if (conNames.contains("底稿")) {
                        stocks = stockMapper.selectByConCode("0501", conNames);
                    }
                    for (int i = 0; i < stocks.size(); i++) {
                        ContractList contractList = new ContractList();
                        contractList.setSource("存货");
                        contractList.setTax("6");
                        contractList.setDiscount("100");
                        contractList.setRows(row);
                        contractList.setCode(contract.getCode());
                        contractList.setName(contract.getName());
                        contractList.setConName(stocks.get(i).getName());
                        contractList.setdCode(contract.getType());
                        String fcode = contract.getType().substring(0, 2);
                        contractList.setfCode(fcode);
                        contractList.setbCode(fcode + contract.getType());
                        contractList.setCount(stocks.get(i).getProportion());
                        String money = getRows(conSp[j], contract);
                        if (money != null) {
                            contractList.setMoney(money);
                            String onTex = getInTex(money, stocks.get(i).getProportion());
                            contractList.setNoTex(onTex);
                            String inTex = getOnTex(onTex);
                            contractList.setInTex(inTex);
                        }
                        List<TimeList> endtime = timeListMapper.selectCode(contract.getCode(), stocks.get(i).getName());
                        if (endtime.size() > 0) {
                            contractList.setEndTime(endtime.get(0).getTime());
                        }
                        contractList.setStatus(contract.getStatus());
                        contractLists.add(contractList);
                        row++;
                    }
                }
            } else {
                //当项目为终止状态时
                List<RunningWater> runningWaters = runningWaterMapper.selectByConCode(contract.getCode());
                if (runningWaters.size()>0){
                    RunningWater runningWater = runningWaters.get(0);
                String proportion = null;
                        String one = runningWater.getOne();
                        if (!one.contains("万") && one.equals("1.0")) {
                            String conName = contract.getConName();
                            String[] conSp = conName.split("\\+");
                            String code = contract.getCode();
                            String name = null;

                            for (int j = 0; j < conSp.length; j++) {
                                String conNames = conSp[j];
                                conNames = conNames.replaceAll("合同", "");
                                List<TimeList> timeLists = timeListMapper.selectByConCode(code, conNames);
                                List<Stock> stocks = stockMapper.selectByConCode(contract.getType(), conNames);
                                if (conNames.contains("底稿")) {
                                    stocks = stockMapper.selectByConCode("0501", conNames);
                                    timeLists = timeListMapper.selectByConCode(code, conNames);
                                }
                                Double fontMoney = Double.valueOf(0);
                                Double fontCount = Double.valueOf(0);
                                if (timeLists.size() > 0) {
                                    for (int i = 0; i < timeLists.size(); i++) {
                                        ContractList contractList = new ContractList();
                                        contractList.setSource("存货");
                                        contractList.setTax("6");
                                        contractList.setDiscount("100");
                                        contractList.setRows(row);
                                        contractList.setCode(contract.getCode());
                                        contractList.setName(contract.getName());
                                        contractList.setConName(stocks.get(i).getName());
                                        contractList.setdCode(contract.getType());
                                        String fcode = contract.getType().substring(0, 2);
                                        contractList.setfCode(fcode);
                                        contractList.setbCode(fcode + contract.getType());
                                        contractList.setCount("1");
                                        fontCount = fontCount + Double.valueOf(stocks.get(i).getProportion());
                                        String money = getRows(conSp[j], contract);
                                        if (money != null) {
                                            fontMoney = fontMoney + Double.valueOf(money);
                                            contractList.setMoney(money);
                                            String onTex = money;
                                            contractList.setNoTex(onTex);
                                            String inTex = getOnTex(onTex);
                                            contractList.setInTex(inTex);
                                        }
                                        contractList.setStatus(contract.getStatus());
                                        contractList.setTime(contract.getTime());
                                        contractLists.add(contractList);
                                        List<TimeList> endtime = timeListMapper.selectByConCode(contract.getCode(), stocks.get(i).getName());
                                        if (endtime.size() > 0) {
                                            contractList.setEndTime(endtime.get(0).getTime());
                                        }
                                        row++;
                                    }
                                }
                                //处理最后一个
                                if (timeLists.size() > 1) {
                                    ContractList contractList = new ContractList();
                                    contractList.setSource("存货");
                                    contractList.setTax("6");
                                    contractList.setDiscount("100");
                                    contractList.setRows(row);
                                    contractList.setCode(contract.getCode());
                                    contractList.setName(contract.getName());
                                    contractList.setConName(stocks.get(timeLists.size() - 1).getName());
                                    contractList.setdCode(contract.getType());
                                    String fcode = contract.getType().substring(0, 2);
                                    contractList.setfCode(fcode);
                                    contractList.setbCode(fcode + contract.getType());
                                    //对数量进行处理
                                    Double fontDo = 1 - fontCount;
                                    contractList.setCount(fontDo.toString());
//                                contractList.setCount(stocks.get(timeLists.size()-1).getProportion());
                                    String money = getRows(conSp[j], contract);
                                    Double aDouble = Double.valueOf(money);
                                    Double nowMoney = aDouble - fontMoney;
                                    contractList.setMoney(money);
                                    contractList.setNoTex(nowMoney.toString());
                                    String inTex = getOnTex(nowMoney.toString());
                                    contractList.setInTex(inTex);
                                    contractList.setStatus(contract.getStatus());
                                    contractList.setTime(contract.getTime());
                                    List<TimeList> endtime = timeListMapper.selectByConCode(code, stocks.get(timeLists.size() - 1).getName());
                                    if (endtime.size() > 0) {
                                        contractList.setEndTime(endtime.get(0).getTime());
                                    }
                                    contractLists.add(contractList);
                                    row++;
                                }
                            }
                        } else if (!one.contains("万")) {
                            Double aDouble = Double.valueOf(one);
                            if (aDouble < 1) {
                                String conName = contract.getConName();
                                String[] conSp = conName.split("\\+");
                                String code = contract.getCode();
                                String name = null;
                                for (int j = 0; j < conSp.length; j++) {
                                    String conNames = conSp[j];
                                    conNames = conNames.replaceAll("合同", "");
                                    List<TimeList> timeLists = timeListMapper.selectByConCode(code, conNames);
                                    if (timeLists!=null&&timeLists.size()>0){
                                        Iterator<TimeList> iterator = timeLists.iterator();
                                        while (iterator.hasNext()) {
                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");//HH:mm:ss
                                            try {
                                                Date date1 = format.parse(iterator.next().getTime());
                                                Date date2 = format.parse(contract.getTime());
                                                int compareTo = date1.compareTo(date2);
                                                if (compareTo==1){
                                                    iterator.remove();
                                                }
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                    List<Stock> stocks = stockMapper.selectByConCode(contract.getType(), conNames);
                                    //处理最后一个
//                                    TimeList timeList = timeLists.get(timeLists.lastIndexOf(0));
//                                    TimeList timeList1 = timeLists.get(0);
//                                    Date fDate=fomartTime(timeList);
//                                    Date uDate = fomartTime(timeList1);
//                                    int compareTo = fDate.compareTo(uDate);
                                    if (conNames.contains("底稿")) {
                                        stocks = stockMapper.selectByConCode("0501", conNames);
                                        timeLists = timeListMapper.selectByConCode(code, conNames);
                                    }
                                    if (timeLists.size() < 2 && timeLists.size() > 0) {
                                        ContractList contractList = new ContractList();
                                        contractList.setSource("存货");
                                        contractList.setTax("6");
                                        contractList.setDiscount("100");
                                        contractList.setRows(row);
                                        contractList.setCode(contract.getCode());
                                        contractList.setName(contract.getName());
                                        if (timeLists.size() > 0) {
                                            if (stocks.size() > 0) {
                                                contractList.setConName(stocks.get(timeLists.size() - 1).getName());
                                            }
                                        }
                                        contractList.setdCode(contract.getType());
                                        String fcode = contract.getType().substring(0, 2);
                                        contractList.setfCode(fcode);
                                        contractList.setbCode(fcode + contract.getType());
                                        //对数量进行处理
                                        contractList.setCount("1");
//                                contractList.setCount(stocks.get(timeLists.size()-1).getProportion());
                                        String money = getRows(conSp[j], contract);
                                        Double aMoney = Double.valueOf(money);

                                        contractList.setMoney(money);
                                        //比例
                                        Double con = Double.valueOf(runningWater.getOne());
                                        Double noTe = aMoney * con;
                                        contractList.setNoTex(noTe.toString());
                                        String inTex = getOnTex(noTe.toString());
                                        contractList.setInTex(inTex);
                                        if (stocks.size() > 0) {
                                            List<TimeList> endtime = timeListMapper.selectByConCode(contract.getCode(), stocks.get(timeLists.size() - 1).getName());
                                            if (endtime.size() > 0) {
                                                contractList.setEndTime(endtime.get(0).getTime());
                                            }
                                        }
                                        contractList.setStatus(contract.getStatus());
                                        contractList.setTime(contract.getTime());
                                        contractLists.add(contractList);
                                        row++;
                                    }
                                    if (timeLists.size() >= 8) {
                                        Double fontMoney = Double.valueOf(0);
                                        Double fontCount = Double.valueOf(0);
                                        for (int i = 0; i < stocks.size() - 1; i++) {
                                            ContractList contractList = new ContractList();
                                            contractList.setSource("存货");
                                            contractList.setTax("6");
                                            contractList.setDiscount("100");
                                            contractList.setRows(row);
                                            contractList.setCode(contract.getCode());
                                            contractList.setName(contract.getName());
                                            contractList.setConName(stocks.get(i).getName());
                                            contractList.setdCode(contract.getType());
                                            String fcode = contract.getType().substring(0, 2);
                                            contractList.setfCode(fcode);
                                            contractList.setbCode(fcode + contract.getType());
                                            contractList.setCount(stocks.get(i).getProportion());
                                            fontCount = fontCount + Double.valueOf(stocks.get(i).getProportion());
                                            String money = getRows(conSp[j], contract);
                                            if (money != null) {
                                                fontMoney = fontMoney + Double.valueOf(money);
                                                String onTex = getInTex(money, stocks.get(i).getProportion());
                                                contractList.setNoTex(onTex);
                                                String inTex = getOnTex(onTex);
                                                contractList.setInTex(inTex);
                                            }
                                            List<TimeList> endtime = timeListMapper.selectByConCode(contract.getCode(), stocks.get(i).getName());
                                            if (endtime.size() > 0) {
                                                contractList.setEndTime(endtime.get(0).getTime());
                                            }
                                            contractList.setStatus(contract.getStatus());
                                            contractList.setTime(contract.getTime());
                                            contractLists.add(contractList);
                                            row++;
                                        }
                                        //处理最后一个
                                        ContractList contractList = new ContractList();
                                        contractList.setSource("存货");
                                        contractList.setTax("6");
                                        contractList.setDiscount("100");
                                        contractList.setRows(row);
                                        contractList.setCode(contract.getCode());
                                        contractList.setName(contract.getName());
                                        contractList.setConName(stocks.get(timeLists.size() - 1).getName());
                                        contractList.setdCode(contract.getType());
                                        String fcode = contract.getType().substring(0, 2);
                                        contractList.setfCode(fcode);
                                        contractList.setbCode(fcode + contract.getType());
                                        //对数量进行处理
                                        Double fontDo = 1 - fontCount;
                                        contractList.setCount(fontDo.toString());
//                                contractList.setCount(stocks.get(timeLists.size()-1).getProportion());
                                        String money = getRows(conSp[j], contract);
                                        Double aMoney = Double.valueOf(money);
                                        Double nowMoney = aMoney - fontMoney;
                                        contractList.setMoney(money);
                                        contractList.setNoTex(nowMoney.toString());
                                        String inTex = getOnTex(nowMoney.toString());
                                        contractList.setInTex(inTex);
                                        contractList.setStatus(contract.getStatus());
                                        List<TimeList> endtime = timeListMapper.selectByConCode(contract.getCode(), stocks.get(timeLists.size() - 1).getName());
                                        if (endtime.size() > 0) {
                                            contractList.setEndTime(endtime.get(0).getTime());
                                        }
                                        contractList.setStatus(contract.getStatus());
                                        contractList.setTime(contract.getTime());
                                        contractLists.add(contractList);
                                        row++;
                                    }
                                    if (timeLists.size() < 8 && timeLists.size() > 1) {
                                        String o1 = runningWater.getOne();
                                        String o2 = runningWater.getTwo();
                                        String o3 = runningWater.getThree();
                                        String o4 = runningWater.getFour();
                                        String nowName = null;
                                        List<TimeList> timeL = null;
                                        Double dTime = Double.valueOf(0);
                                        if (o4 != null) {
                                            nowName = "发行与上市";
                                            Double.valueOf(o4);
                                            name = nowName;
                                            timeL = timeListMapper.selectByConCode(code, name);
                                            if (timeL != null) {
                                                dTime = dTime + Double.valueOf(o4) + Double.valueOf(o3) + Double.valueOf(o2) + Double.valueOf(o1);
                                            }
                                        } else if (o3 != null) {
                                            nowName = "上会";
                                            name = nowName;
                                            timeL = timeListMapper.selectByConCode(code, name);
                                            if (timeL != null) {
                                                dTime = dTime + Double.valueOf(o3) + Double.valueOf(o2) + Double.valueOf(o1);
                                            }
                                        } else if (o2 != null) {
                                            nowName = "反馈";
                                            name = nowName;
                                            timeL = timeListMapper.selectByConCode(code, name);
                                            if (timeL != null) {
                                                dTime = dTime + Double.valueOf(o2) + Double.valueOf(o1);
                                            }
                                        } else if (o1 != null) {
                                            nowName = "申报";
                                            name = nowName;
                                            timeL = timeListMapper.selectByConCode(code, name);
                                            if (timeL != null) {
                                                dTime = dTime + Double.valueOf(o1);
                                            }
                                        }
                                        if (dTime < 1) {
                                            for (int i = 0; i < stocks.size(); i++) {
                                                name = stocks.get(i).getName();
                                                List<TimeList> timeLists1 = timeListMapper.selectByConCode(code, name);
                                                for (TimeList timeList : timeLists) {
                                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                    try {
                                                        Date date1 = format.parse(timeList.getTime());
                                                        Date date2 = format.parse(contract.getTime());
                                                        int compareTo = date1.compareTo(date2);
                                                        if (compareTo==1){
                                                            timeLists.remove(timeList);
                                                        }
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                if (timeLists1 != null) {
                                                    ContractList contractList = new ContractList();
                                                    contractList.setSource("存货");
                                                    contractList.setTax("6");
                                                    contractList.setDiscount("100");
                                                    contractList.setRows(row);
                                                    contractList.setCode(contract.getCode());
                                                    contractList.setName(contract.getName());
                                                    contractList.setConName(stocks.get(i).getName());
                                                    contractList.setdCode(contract.getType());
                                                    String fcode = contract.getType().substring(0, 2);
                                                    contractList.setfCode(fcode);
                                                    contractList.setbCode(fcode + contract.getType());
                                                    contractList.setStatus(contract.getStatus());
                                                    contractList.setNoTex("0");
                                                    contractList.setInTex("0");
                                                    contractList.setCount("0");
                                                    String money = getRows(conSp[j], contract);
                                                    contractList.setMoney(money);
                                                    if (o1 != null && stocks.get(i).getName().contains("申报")) {
                                                        if (money != null) {
                                                            Double ont = Double.valueOf(money) * Double.valueOf(o1);
                                                            String onTex = ont.toString();
                                                            contractList.setNoTex(onTex);
                                                            String inTex = getOnTex(onTex);
                                                            contractList.setInTex(inTex);
                                                            Double d1 = Double.valueOf(stocks.get(i).getProportion()) * dTime;
                                                            String toString = Double.toString(d1);
                                                            contractList.setCount(toString);
                                                        }
                                                    } else if (o2 != null && stocks.get(i).getName().contains("反馈")) {
                                                        if (money != null) {
                                                            Double ont = Double.valueOf(money) * Double.valueOf(o2);
                                                            String onTex = ont.toString();
                                                            contractList.setNoTex(onTex);
                                                            String inTex = getOnTex(onTex);
                                                            contractList.setInTex(inTex);
                                                            Double d1 = Double.valueOf(stocks.get(i).getProportion()) * dTime;
                                                            String toString = Double.toString(d1);
                                                            contractList.setCount(toString);
                                                        }
                                                    } else if (o3 != null && stocks.get(i).getName().contains("上会")) {
                                                        if (money != null) {
                                                            Double ont = Double.valueOf(money) * Double.valueOf(o3);
                                                            String onTex = ont.toString();
                                                            contractList.setNoTex(onTex);
                                                            String inTex = getOnTex(onTex);
                                                            contractList.setInTex(inTex);
                                                            Double d1 = Double.valueOf(stocks.get(i).getProportion()) * dTime;
                                                            String toString = Double.toString(d1);
                                                            contractList.setCount(toString);
                                                        }
                                                    } else if (o3 != null && stocks.get(i).getName().contains("发行与上市")) {
                                                        if (money != null) {
                                                            Double ont = Double.valueOf(money) * Double.valueOf(o4);
                                                            String onTex = ont.toString();
                                                            contractList.setNoTex(onTex);
                                                            String inTex = getOnTex(onTex);
                                                            contractList.setInTex(inTex);
                                                            Double d1 = Double.valueOf(stocks.get(i).getProportion()) * dTime;
                                                            String toString = Double.toString(d1);
                                                            contractList.setCount(toString);
                                                        }
                                                    }
                                                    List<TimeList> endtime = timeListMapper.selectByConCode(contract.getCode(), stocks.get(i).getName());
                                                    if (endtime.size() > 0) {
                                                        contractList.setEndTime(endtime.get(0).getTime());
                                                    }
                                                    contractList.setStatus(contract.getStatus());
                                                    contractList.setTime(contract.getTime());
                                                    contractLists.add(contractList);
                                                    row++;
                                                }
                                            }
                                        } else if (dTime == 1) {
                                            for (int i = 0; i < stocks.size(); i++) {
                                                name = stocks.get(i).getName();
                                                List<TimeList> timeLists1 = timeListMapper.selectByConCode(code, name);
                                                if (timeLists1 != null) {
                                                    ContractList contractList = new ContractList();
                                                    contractList.setSource("存货");
                                                    contractList.setTax("6");
                                                    contractList.setDiscount("100");
                                                    contractList.setRows(row);
                                                    contractList.setCode(contract.getCode());
                                                    contractList.setName(contract.getName());
                                                    contractList.setConName(stocks.get(i).getName());
                                                    contractList.setdCode(contract.getType());
                                                    String fcode = contract.getType().substring(0, 2);
                                                    contractList.setfCode(fcode);
                                                    contractList.setbCode(fcode + contract.getType());
                                                    contractList.setCount(stocks.get(i).getProportion());
                                                    String money = getRows(conSp[j], contract);
                                                    if (money != null) {
                                                        String onTex = getInTex(money, stocks.get(i).getProportion());
                                                        contractList.setNoTex(onTex);
                                                        String inTex = getOnTex(onTex);
                                                        contractList.setInTex(inTex);
                                                    }
                                                    List<TimeList> endtime = timeListMapper.selectByConCode(contract.getCode(), stocks.get(i).getName());
                                                    if (endtime.size() > 0) {
                                                        contractList.setEndTime(endtime.get(0).getTime());
                                                    }
                                                    contractList.setStatus(contract.getStatus());
                                                    contractList.setTime(contract.getTime());
                                                    contractLists.add(contractList);
                                                    row++;
                                                }
                                            }
                                        } else if (dTime > 1) {
                                            for (int i = 0; i < stocks.size() - 1; i++) {
                                                name = stocks.get(i).getName();
                                                List<TimeList> timeLists1 = timeListMapper.selectByConCode(code, name);
                                                Double fontMoney = Double.valueOf(0);
                                                Double fontCount = Double.valueOf(0);
                                                if (timeLists1 != null) {
                                                    ContractList contractList = new ContractList();
                                                    contractList.setSource("存货");
                                                    contractList.setTax("6");
                                                    contractList.setDiscount("100");
                                                    contractList.setRows(row);
                                                    contractList.setCode(contract.getCode());
                                                    contractList.setName(contract.getName());
                                                    contractList.setConName(stocks.get(i).getName());
                                                    contractList.setdCode(contract.getType());
                                                    String fcode = contract.getType().substring(0, 2);
                                                    contractList.setfCode(fcode);
                                                    contractList.setbCode(fcode + contract.getType());
                                                    contractList.setCount(stocks.get(i).getProportion());
                                                    String money = getRows(conSp[j], contract);
                                                    if (money != null) {
                                                        String onTex = getInTex(money, stocks.get(i).getProportion());
                                                        contractList.setNoTex(onTex);
                                                        String inTex = getOnTex(onTex);
                                                        contractList.setInTex(inTex);
                                                    }
                                                    contractList.setStatus(contract.getStatus());
                                                    contractList.setTime(contract.getTime());
                                                    contractLists.add(contractList);
                                                    row++;
                                                }
                                                ContractList contractList = new ContractList();
                                                contractList.setSource("存货");
                                                contractList.setTax("6");
                                                contractList.setDiscount("100");
                                                contractList.setRows(row);
                                                contractList.setCode(contract.getCode());
                                                contractList.setName(contract.getName());
                                                contractList.setConName(stocks.get(timeLists.size() - 1).getName());
                                                contractList.setdCode(contract.getType());
                                                String fcode = contract.getType().substring(0, 2);
                                                contractList.setfCode(fcode);
                                                contractList.setbCode(fcode + contract.getType());
                                                //对数量进行处理
                                                Double fontDo = 1 - fontCount;
                                                contractList.setCount(fontDo.toString());
//                                contractList.setCount(stocks.get(timeLists.size()-1).getProportion());
                                                String money = getRows(conSp[j], contract);
                                                Double aMoney = Double.valueOf(money);
                                                Double nowMoney = aMoney - fontMoney;
                                                contractList.setMoney(money);
                                                contractList.setNoTex(nowMoney.toString());
                                                String inTex = getOnTex(nowMoney.toString());
                                                contractList.setInTex(inTex);
                                                contractList.setStatus(contract.getStatus());
                                                contractList.setTime(contract.getTime());
                                                contractLists.add(contractList);
                                                row++;
                                            }
                                        }
                                    }
                                }
                            } else if (aDouble > 1) {
                                String conName = contract.getConName();
                                String[] conSp = conName.split("\\+");
                                String code = contract.getCode();

                                for (int j = 0; j < conSp.length; j++) {
                                    String conNames = conSp[j];
                                    conNames = conNames.replaceAll("合同", "");
                                    String name = null;
                                    List<TimeList> timeLists = timeListMapper.selectByConCode(code, conNames);
                                    List<Stock> stocks = stockMapper.selectByConCode(contract.getType(), conNames);
                                    if (conNames.contains("底稿")) {
                                        stocks = stockMapper.selectByConCode("0501", conNames);
                                    }
                                    //处理最后一个
//                                    TimeList timeList = timeLists.get(timeLists.lastIndexOf(0));
//                                    TimeList timeList1 = timeLists.get(0);
//                                    Date fDate=fomartTime(timeList);
//                                    Date uDate = fomartTime(timeList1);
//                                    int compareTo = fDate.compareTo(uDate);
                                    if (timeLists.size() < 2 && timeLists.size() > 0) {
                                        ContractList contractList = new ContractList();
                                        contractList.setSource("存货");
                                        contractList.setTax("6");
                                        contractList.setDiscount("100");
                                        contractList.setRows(row);
                                        contractList.setCode(contract.getCode());
                                        contractList.setName(contract.getName());
                                        contractList.setConName(stocks.get(timeLists.size() - 1).getName());
                                        contractList.setdCode(contract.getType());
                                        String fcode = contract.getType().substring(0, 2);
                                        contractList.setfCode(fcode);
                                        contractList.setbCode(fcode + contract.getType());
                                        //对数量进行处理
                                        contractList.setCount("1");
//                                contractList.setCount(stocks.get(timeLists.size()-1).getProportion());
                                        String money = getRows(conSp[j], contract);
                                        Double aMoney = Double.valueOf(money);

                                        contractList.setMoney(money);
                                        //比例
                                        Double con = Double.valueOf(runningWater.getOne());
                                        Double noTe = aMoney * con;
                                        contractList.setNoTex(noTe.toString());
                                        String inTex = getOnTex(noTe.toString());
                                        contractList.setInTex(inTex);
                                        contractList.setStatus(contract.getStatus());
                                        contractList.setTime(contract.getTime());
                                        contractLists.add(contractList);
                                        row++;
                                    }
                                    if (timeLists.size() >= 8) {
                                        Double fontMoney = Double.valueOf(0);
                                        Double fontCount = Double.valueOf(0);
                                        for (int i = 0; i < stocks.size() - 1; i++) {
                                            ContractList contractList = new ContractList();
                                            contractList.setSource("存货");
                                            contractList.setTax("6");
                                            contractList.setDiscount("100");
                                            contractList.setRows(row);
                                            contractList.setCode(contract.getCode());
                                            contractList.setName(contract.getName());
                                            contractList.setConName(stocks.get(i).getName());
                                            contractList.setdCode(contract.getType());
                                            String fcode = contract.getType().substring(0, 2);
                                            contractList.setfCode(fcode);
                                            contractList.setbCode(fcode + contract.getType());
                                            contractList.setCount(stocks.get(i).getProportion());
                                            fontCount = fontCount + Double.valueOf(stocks.get(i).getProportion());
                                            String money = getRows(conSp[j], contract);
                                            if (money != null) {
                                                fontMoney = fontMoney + Double.valueOf(money);
                                                String onTex = getInTex(money, stocks.get(i).getProportion());
                                                contractList.setNoTex(onTex);
                                                String inTex = getOnTex(onTex);
                                                contractList.setInTex(inTex);
                                            }
                                            contractList.setStatus(contract.getStatus());
                                            contractList.setTime(contract.getTime());
                                            contractLists.add(contractList);
                                            row++;
                                        }
                                        //处理最后一个
                                        ContractList contractList = new ContractList();
                                        contractList.setSource("存货");
                                        contractList.setTax("6");
                                        contractList.setDiscount("100");
                                        contractList.setRows(row);
                                        contractList.setCode(contract.getCode());
                                        contractList.setName(contract.getName());
                                        contractList.setConName(stocks.get(timeLists.size() - 1).getName());
                                        contractList.setdCode(contract.getType());
                                        String fcode = contract.getType().substring(0, 2);
                                        contractList.setfCode(fcode);
                                        contractList.setbCode(fcode + contract.getType());
                                        //对数量进行处理
                                        Double fontDo = 1 - fontCount;
                                        contractList.setCount(fontDo.toString());
//                                contractList.setCount(stocks.get(timeLists.size()-1).getProportion());
                                        String money = getRows(conSp[j], contract);
                                        Double aMoney = Double.valueOf(money);
                                        Double nowMoney = aMoney - fontMoney;
                                        contractList.setMoney(money);
                                        contractList.setNoTex(nowMoney.toString());
                                        String inTex = getOnTex(nowMoney.toString());
                                        contractList.setInTex(inTex);
                                        contractList.setStatus(contract.getStatus());
                                        contractList.setTime(contract.getTime());
                                        contractLists.add(contractList);
                                        row++;
                                    }
                                    if (timeLists.size() < 8 && timeLists.size() > 1) {
                                        String o1 = runningWater.getOne();
                                        String o2 = runningWater.getTwo();
                                        String o3 = runningWater.getThree();
                                        String o4 = runningWater.getFour();
                                        String nowName = null;
                                        List<TimeList> timeL = null;
                                        Double dTime = Double.valueOf(0);
                                        if (o4 != null) {
                                            nowName = "发行与上市";
                                            Double.valueOf(o4);
                                            name = nowName;
                                            timeL = timeListMapper.selectByConCode(code, name);
                                            if (timeL != null) {
                                                if (o2 != null) {
                                                    dTime = dTime + Double.valueOf(o4) + Double.valueOf(o3) + Double.valueOf(o2) + Double.valueOf(o1);
                                                } else {
                                                    dTime = dTime + Double.valueOf(o4) + Double.valueOf(o3) + Double.valueOf(o1);
                                                }
                                            }
                                        } else if (o3 != null) {
                                            nowName = "上会";
                                            name = nowName;
                                            timeL = timeListMapper.selectByConCode(code, name);
                                            if (timeL != null) {
                                                dTime = dTime + Double.valueOf(o3) + Double.valueOf(o2) + Double.valueOf(o1);
                                            }
                                        } else if (o2 != null) {
                                            nowName = "反馈";
                                            name = nowName;
                                            timeL = timeListMapper.selectByConCode(code, name);
                                            if (timeL != null) {
                                                dTime = dTime + Double.valueOf(o2) + Double.valueOf(o1);
                                            }
                                        } else if (o1 != null) {
                                            nowName = "申报";
                                            name = nowName;
                                            timeL = timeListMapper.selectByConCode(code, name);
                                            if (timeL != null) {
                                                dTime = dTime + Double.valueOf(o1);
                                            }
                                        }
                                        if (dTime < Double.valueOf(getRows(conSp[j], contract))) {
                                            for (int i = 0; i < stocks.size(); i++) {
                                                ContractList contractList = new ContractList();
                                                contractList.setSource("存货");
                                                contractList.setTax("6");
                                                contractList.setDiscount("100");
                                                contractList.setRows(row);
                                                contractList.setCode(contract.getCode());
                                                contractList.setName(contract.getName());
                                                contractList.setConName(stocks.get(i).getName());
                                                contractList.setdCode(contract.getType());
                                                String fcode = contract.getType().substring(0, 2);
                                                contractList.setfCode(fcode);
                                                contractList.setbCode(fcode + contract.getType());
                                                contractList.setStatus(contract.getStatus());
                                                contractList.setNoTex("0");
                                                contractList.setInTex("0");
                                                contractList.setCount("0");
                                                if (o1 != null && stocks.get(i).getName().contains("申报")) {
                                                    String money = getRows(conSp[j], contract);
                                                    if (money != null) {
                                                        Double ont = Double.valueOf(money) * Double.valueOf(o1);
                                                        String onTex = ont.toString();
                                                        contractList.setNoTex(onTex);
                                                        String inTex = getOnTex(onTex);
                                                        contractList.setInTex(inTex);
                                                        Double d1 = Double.valueOf(o1) / Double.valueOf(money);
                                                        String toString = Double.toString(d1);
                                                        contractList.setCount(toString);
                                                    }
                                                } else if (o2 != null && stocks.get(i).getName().contains("反馈")) {
                                                    String money = getRows(conSp[j], contract);
                                                    if (money != null) {
                                                        Double ont = Double.valueOf(money) * Double.valueOf(o2);
                                                        String onTex = ont.toString();
                                                        contractList.setNoTex(onTex);
                                                        String inTex = getOnTex(onTex);
                                                        contractList.setInTex(inTex);
                                                        Double d1 = Double.valueOf(o1) / Double.valueOf(money);
                                                        String toString = Double.toString(d1);
                                                        contractList.setCount(toString);
                                                    }
                                                } else if (o3 != null && stocks.get(i).getName().contains("上会")) {
                                                    String money = getRows(conSp[j], contract);
                                                    if (money != null) {
                                                        Double ont = Double.valueOf(money) * Double.valueOf(o3);
                                                        String onTex = ont.toString();
                                                        contractList.setNoTex(onTex);
                                                        String inTex = getOnTex(onTex);
                                                        contractList.setInTex(inTex);
                                                        Double d1 = Double.valueOf(o1) / Double.valueOf(money);
                                                        String toString = Double.toString(d1);
                                                        contractList.setCount(toString);
                                                    }
                                                } else if (o3 != null && stocks.get(i).getName().contains("发行与上市")) {
                                                    String money = getRows(conSp[j], contract);
                                                    if (money != null) {
                                                        Double ont = Double.valueOf(money) * Double.valueOf(o4);
                                                        String onTex = ont.toString();
                                                        contractList.setNoTex(onTex);
                                                        String inTex = getOnTex(onTex);
                                                        contractList.setInTex(inTex);
                                                        Double d1 = Double.valueOf(o1) / Double.valueOf(money);
                                                        String toString = Double.toString(d1);
                                                        contractList.setCount(toString);
                                                    }
                                                }
                                                contractList.setStatus(contract.getStatus());
                                                contractList.setTime(contract.getTime());
                                                contractLists.add(contractList);
                                                row++;
                                            }
                                        } else if (dTime == Double.valueOf(getRows(conSp[j], contract))) {
                                            for (int i = 0; i < stocks.size(); i++) {
                                                name = stocks.get(i).getName();
                                                List<TimeList> timeLists1 = timeListMapper.selectByConCode(code, name);
                                                if (timeLists1 != null) {
                                                    ContractList contractList = new ContractList();
                                                    contractList.setSource("存货");
                                                    contractList.setTax("6");
                                                    contractList.setDiscount("100");
                                                    contractList.setRows(row);
                                                    contractList.setCode(contract.getCode());
                                                    contractList.setName(contract.getName());
                                                    contractList.setConName(stocks.get(i).getName());
                                                    contractList.setdCode(contract.getType());
                                                    String fcode = contract.getType().substring(0, 2);
                                                    contractList.setfCode(fcode);
                                                    contractList.setbCode(fcode + contract.getType());
                                                    contractList.setCount(stocks.get(i).getProportion());
                                                    String money = getRows(conSp[j], contract);
                                                    if (money != null) {
                                                        String onTex = getInTex(money, stocks.get(i).getProportion());
                                                        contractList.setNoTex(onTex);
                                                        String inTex = getOnTex(onTex);
                                                        contractList.setInTex(inTex);
                                                    }
                                                    contractList.setStatus(contract.getStatus());
                                                    contractList.setTime(contract.getTime());
                                                    contractLists.add(contractList);
                                                    row++;
                                                }
                                            }
                                        } else if (dTime > Double.valueOf(getRows(conSp[j], contract))) {
                                            for (int i = 0; i < stocks.size() - 1; i++) {
                                                name = stocks.get(i).getName();
                                                List<TimeList> timeLists1 = timeListMapper.selectByConCode(code, name);
                                                Double fontMoney = Double.valueOf(0);
                                                Double fontCount = Double.valueOf(0);
                                                if (timeLists1 != null) {
                                                    ContractList contractList = new ContractList();
                                                    contractList.setSource("存货");
                                                    contractList.setTax("6");
                                                    contractList.setDiscount("100");
                                                    contractList.setRows(row);
                                                    contractList.setCode(contract.getCode());
                                                    contractList.setName(contract.getName());
                                                    contractList.setConName(stocks.get(i).getName());
                                                    contractList.setdCode(contract.getType());
                                                    String fcode = contract.getType().substring(0, 2);
                                                    contractList.setfCode(fcode);
                                                    contractList.setbCode(fcode + contract.getType());
                                                    contractList.setCount(stocks.get(i).getProportion());
                                                    String money = getRows(conSp[j], contract);
                                                    if (money != null) {
                                                        String onTex = getInTex(money, stocks.get(i).getProportion());
                                                        contractList.setNoTex(onTex);
                                                        String inTex = getOnTex(onTex);
                                                        contractList.setInTex(inTex);
                                                    }
                                                    contractList.setStatus(contract.getStatus());
                                                    contractList.setTime(contract.getTime());
                                                    contractLists.add(contractList);
                                                    row++;
                                                }
                                                ContractList contractList = new ContractList();
                                                contractList.setSource("存货");
                                                contractList.setTax("6");
                                                contractList.setDiscount("100");
                                                contractList.setRows(row);
                                                contractList.setCode(contract.getCode());
                                                contractList.setName(contract.getName());
                                                contractList.setConName(stocks.get(timeLists.size() - 1).getName());
                                                contractList.setdCode(contract.getType());
                                                String fcode = contract.getType().substring(0, 2);
                                                contractList.setfCode(fcode);
                                                contractList.setbCode(fcode + contract.getType());
                                                //对数量进行处理
                                                Double fontDo = 1 - fontCount;
                                                contractList.setCount(fontDo.toString());
//                                contractList.setCount(stocks.get(timeLists.size()-1).getProportion());
                                                String money = getRows(conSp[j], contract);
                                                Double aMoney = Double.valueOf(money);
                                                Double nowMoney = aMoney - fontMoney;
                                                contractList.setMoney(money);
                                                contractList.setNoTex(nowMoney.toString());
                                                String inTex = getOnTex(nowMoney.toString());
                                                contractList.setInTex(inTex);
                                                contractList.setStatus(contract.getStatus());
                                                contractList.setTime(contract.getTime());
                                                contractLists.add(contractList);
                                                row++;
                                            }
                                        }
                                    }
                                }
                                }
                            }
                        }
            }
        }
        return contractLists;
    }

    private Date fomartTime(TimeList timeList) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM
        Date date = null;
        try {
            date = simpleDateFormat.parse(timeList.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private String getOnTex(String money) {
        BigDecimal bigDecimal = new BigDecimal(money);
        BigDecimal multiply = bigDecimal.divide(new BigDecimal("1.06"), 6);
        String toString = multiply.toString();
        return toString;
    }

    private String getInTex(String money, String proportion) {
        Double mon = Double.valueOf(money);
        Double dou = Double.valueOf(proportion);
        double v = mon * dou;
        return Double.toString(v);
    }

    private String getRows(String name, Contract contract) {
        if (name.contains("印务")) {
            return contract.getPrinting();
        }
        if (name.contains("底稿")) {
            return contract.getManuscript();
        }
        if (name.contains("可研")) {
            return contract.getFeasibility();
        }
        if (name.contains("信批")) {
            return contract.getLetterApproved();
        }
        if (name.contains("财务数据核对")) {
            return contract.getFinance();
        }
        if (name.contains("软件")) {
            double v = Double.valueOf(contract.getEls()) + Double.valueOf(contract.getYxz());
            String toString = Double.toString(v);
            return toString;
        }
        return null;
    }


//    private static XSSFWorkbook writeMap(List<As1> as1s) {
////        System.out.println("值"+JSON.toJSON(as1s));
//        for (As1 as1 : as1s) {
//            row = sheet.createRow(as1.getRows()); //在现有行号后追加数据
//            //给需要添加数据的列赋值
//            row.createCell(0).setCellValue(as1.getName());
//            List<As2> as2s = as1.getAs2s();
////            List<As2> as2s1 = deList(as2s);
////            if (as1.getName().equals(""))
//            //对as2中无效发票进行处理
////            removeAs2(as2s);
//            for (int i = 0; i < as2s.size(); i++) {
//                As2 as2 = as2s.get(i);
//                row.createCell(1 + (3 * i)).setCellValue(as2.getTime());
//                List<As3> as3s = as2.getAs3s();
//                String num = getAs3Num(as3s);
//                row.createCell(2 + (3 * i)).setCellValue(num);
//                BigDecimal amount = getAs3Amount(as3s);
//                row.createCell(3 + (3 * i)).setCellValue(String.valueOf(amount));
//            }
//        }
//        return excel;
//    }
}
