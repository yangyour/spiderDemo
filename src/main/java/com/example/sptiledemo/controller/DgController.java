package com.example.sptiledemo.controller;

import com.alibaba.fastjson.JSON;
import com.example.sptiledemo.bean.*;
import com.example.sptiledemo.mapper.RunningWaterMapper;
import com.example.sptiledemo.mapper.StockMapper;
import com.example.sptiledemo.mapper.TimeListMapper;
import com.example.sptiledemo.service.ContractExcelService;
import com.example.sptiledemo.service.ExcelService;
import com.google.j2objc.annotations.J2ObjCIncompatible;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.sptiledemo.controller.TestController.getInTex;
import static com.example.sptiledemo.controller.TestController.getOnTex;

@RestController
public class DgController {


    private static String outXlsPath = "D:\\输出文件\\荣大商务\\可研\\北京荣大商务可研2017-2020年台账.xls"; // 生成路径

    @Autowired
    private StockMapper stockMapper;


    @Autowired
    private RunningWaterMapper runningWaterMapper;

    @GetMapping("digao")
    public void test111() {
        List<Contract> contracts = ExcelService.getExcel();
        XSSFWorkbook excel = ContractExcelService.createExcel();
        XSSFRow row0;
        XSSFSheet sheet0 = excel.getSheetAt(0); //获取到工作表，因为一个excel可能有多个工作表
        for (int i = 0; i < contracts.size(); i++) {
            row0 = sheet0.createRow(i + 1); //在现有行号后追加数据
            //给需要添加数据的列赋值
            row0.createCell(0).setCellValue(contracts.get(i).getCode());
            row0.createCell(1).setCellValue(contracts.get(i).getConName());
            row0.createCell(2).setCellValue(contracts.get(i).getType());
            row0.createCell(3).setCellValue("应收类合同");
            row0.createCell(4).setCellValue("不控制");
            row0.createCell(6).setCellValue("正扣");
            row0.createCell(7).setCellValue("含税");
            row0.createCell(11).setCellValue(contracts.get(i).getkCode());
            row0.createCell(13).setCellValue("人民币");
            row0.createCell(14).setCellValue(1);
            row0.createCell(17).setCellValue(contracts.get(i).getqTime());
            row0.createCell(22).setCellValue("demo");
            row0.createCell(24).setCellValue("无");
            if (contracts.get(i).getStatus() != null) {
                row0.createCell(33).setCellValue(contracts.get(i).getStatus() + contracts.get(i).getqTime());
            }
        }


        XSSFSheet sheet = excel.getSheetAt(1); //获取到工作表，因为一个excel可能有多个工作表
        XSSFRow row;
        List<ContractList> contractList = getContractList(contracts);
        Set<String> strings = new HashSet<>();
        Set<String> st2 = new HashSet<>();
//        for (ContractList c : contractList) {
//            if (c.getStatus() != null) {
//                strings.add(c.getCode());
//            }
//            st2.add(c.getCode());
//        }
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
            if (contractList.get(i).getMoney() != null) {
                row.createCell(19).setCellValue(Double.valueOf(contractList.get(i).getMoney()));
            }
            if (contractList.get(i).getInTex() != null) {
                row.createCell(20).setCellValue(Double.valueOf(contractList.get(i).getInTex()));
            }
            if (contractList.get(i).getNoTex() != null) {
                row.createCell(21).setCellValue(Double.valueOf(contractList.get(i).getNoTex()));
            }
            row.createCell(28).setCellValue(contractList.get(i).getEndTime());
            row.createCell(31).setCellValue(contractList.get(i).getStatus());
            row.createCell(32).setCellValue(contractList.get(i).getTime());
            row.createCell(33).setCellValue(contractList.get(i).getStageStatus());
        }
        overExcel(excel);
    }

    private List<ContractList> getContractList(List<Contract> contracts) {
        List<ContractList> contractLists = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("0.00");
        DecimalFormat df000 = new DecimalFormat("0");

        int row = 1;
        Long lId=(long)1;
        for (Contract contract : contracts) {
            List<Stock> stocks = stockMapper.selectByConCode(contract.getType(), "底稿");
            List<TimeList> tL = new ArrayList<>();
            List<TimeList> tLst = new ArrayList<>();
            for (Stock stock : stocks) {
                String[] split = stock.getName().split("-");
                String strings=split[1];
                TimeList timeList = new TimeList();
                timeList.setId(lId);
                timeList.setCode(contract.getCode());
                timeList.setName(stock.getName());
                if (strings.equals("内核")) {
                    timeList.setTime(contract.getNhTime());
                } else if (strings.equals("申报")) {
                    timeList.setTime(contract.getSbTime());
                } else if (strings.equals("上会")) {
                    timeList.setTime(contract.getShTime());
                }else if (strings.equals("上市")) {
                    timeList.setTime(contract.getShTime());
                }
                if (timeList.getTime()!=null){
                    tLst.add(timeList);
                }
                tL.add(timeList);
                lId++;
            }
            if (tL.size() > 0 && tL.get(tL.size() - 1).getTime() != null && !tL.get(tL.size() - 1).getTime().equals("")) {
                Double lM = Double.valueOf(0);
                Double lC = Double.valueOf(0);
                Integer os = 0;
                for (int i = 0; i < tL.size(); i++) {
                    ContractList contractList = new ContractList();
                    contractList.setSource("存货");
                    contractList.setTax("6");
                    contractList.setDiscount("100");
                    contractList.setRows(row);
                    contractList.setCode(contract.getCode());
                    contractList.setName(contract.getName());
                    contractList.setConName(tL.get(i).getName());
                    contractList.setdCode(contract.getType());
                    String fcode = contract.getType().substring(0, 2);
                    contractList.setfCode(fcode);
                    contractList.setbCode(fcode + contract.getType());
                    contractList.setCount(stocks.get(i).getProportion());
                    String money = contract.getManuscript();
                    if (money != null) {
                        contractList.setMoney(money);
                        String onTex = getInTex(money, stocks.get(i).getProportion());
                        contractList.setNoTex(onTex);
                        String inTex = getOnTex(onTex);
                        contractList.setInTex(inTex);
                    }
                    if (tL.size() == 1) {
                        contractList.setMoney(money);
                        contractList.setNoTex(money);
                        String inTex = getOnTex(money);
                        contractList.setInTex(inTex);
                    }
                    contractList.setEndTime(tL.get(i).getTime());
                    contractList.setStatus(contract.getStatus());
                    contractList.setTime(contract.getTime());
                    contractLists.add(contractList);
                    row++;
                }
            } else if (contract.getStatus() == null) {

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
                    String money = contract.getManuscript();
                    if (money != null) {
                        contractList.setMoney(money);
                        String onTex = getInTex(money, stocks.get(i).getProportion());
                        contractList.setNoTex(onTex);
                        String inTex = getOnTex(onTex);
                        contractList.setInTex(inTex);
                    }
                    contractList.setEndTime(tL.get(i).getTime());
                    contractList.setStatus(contract.getStatus());
                    contractList.setTime(contract.getTime());
                    contractLists.add(contractList);
                    row++;
                }
            }else if (contract.getStatus() != null) {
                //当项目为终止状态时
                List<RunningWater> runningWaters = runningWaterMapper.selectByConCode(contract.getCode());
                if (runningWaters.size() > 0) {
                    RunningWater runningWater = runningWaters.get(0);
                    String proportion = null;
                    String one = runningWater.getOne();
                    if (one.equals("1.0")) {
                        TimeList sa = tL.get(tL.size() - 1);
                        List<Long> list = new ArrayList<>();
                        for (TimeList l : tL) {
                            list.add(l.getId());
                        }
                        int i1 = list.indexOf(sa.getId());
                        Double fontMoney = Double.valueOf(0);
                        Double fontCount = Double.valueOf(0);
                        if (tL.size() > 0) {
                            for (int i = 0; i <= i1; i++) {
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
                                String money = contract.getManuscript();
                                if (i == i1) {
                                    Double nowC = Double.valueOf(1) - fontCount;
                                    Double nowM = nowC * Double.valueOf(money);
                                    contractList.setMoney(df.format(nowM));
                                    contractList.setCount(df.format(nowC));
//                                        String onTex = getInTex(money,stocks.get(i).getProportion());
                                    contractList.setNoTex(df.format(nowM));
                                    String inTex = getOnTex(df.format(nowM));
                                    contractList.setInTex(inTex);
                                }
                                fontCount = fontCount + Double.valueOf(stocks.get(i).getProportion());
                                if (money != null && i != i1) {
                                    contractList.setCount(stocks.get(i).getProportion());
                                    contractList.setMoney(money);
                                    String onTex = getInTex(money, stocks.get(i).getProportion());
                                    contractList.setNoTex(onTex);
                                    String inTex = getOnTex(onTex);
                                    contractList.setInTex(inTex);
                                }
                                contractList.setStatus(contract.getStatus());
                                contractList.setTime(contract.getTime());
                                contractList.setStageStatus("一次性付款的情况");
                                contractList.setEndTime(tL.get(i).getTime());
                                contractLists.add(contractList);
                                row++;
                            }
                        } else {
                            ContractList contractList = new ContractList();
                            contractList.setSource("存货");
                            contractList.setTax("6");
                            contractList.setDiscount("100");
                            contractList.setRows(row);
                            contractList.setCode(contract.getCode());
                            contractList.setName(contract.getName());
                            contractList.setConName(stocks.get(0).getName());
                            contractList.setdCode(contract.getType());
                            String fcode = contract.getType().substring(0, 2);
                            contractList.setfCode(fcode);
                            contractList.setbCode(fcode + contract.getType());
                            String money = contract.getManuscript();
                            contractList.setCount("1.00");
                            if (money != null) {
                                contractList.setCount(stocks.get(0).getProportion());
                                contractList.setMoney(money);
                                String onTex = money;
                                contractList.setNoTex(onTex);
                                String inTex = getOnTex(onTex);
                                contractList.setInTex(inTex);
                            }
                            contractList.setStatus(contract.getStatus());
                            contractList.setTime(contract.getTime());
                            contractList.setStageStatus("一次性付款的情况");
                            contractList.setEndTime(tL.get(0).getTime());
                            contractLists.add(contractList);

                            row++;
                        }
                    } else {
                        Double aDouble = Double.valueOf(one);
                        if (aDouble < 1) {

                              if (tLst.size()<1){
                                  if (tL.size() > 0) {
                                      ContractList contractList = new ContractList();
                                      contractList.setSource("存货");
                                      contractList.setTax("6");
                                      contractList.setDiscount("100");
                                      contractList.setRows(row);
                                      contractList.setCode(contract.getCode());
                                      contractList.setName(contract.getName());
                                      contractList.setConName(tL.get(0).getName());
                                      contractList.setdCode(contract.getType());
                                      String fcode = contract.getType().substring(0, 2);
                                      contractList.setfCode(fcode);
                                      contractList.setbCode(fcode + contract.getType());
                                      //对数量进行处理
                                      contractList.setCount("1.0");
//                                contractList.setCount(stocks.get(timeLists.size()-1).getProportion());
                                      String money = contract.getManuscript();
                                      Double amon = Double.valueOf(money);
                                      contractList.setMoney(money);
                                      contractList.setNoTex(money);
                                      String inTex = getOnTex(money);
                                      contractList.setInTex(inTex);
                                      contractList.setStatus(contract.getStatus());
                                      contractList.setTime(contract.getTime());
                                      contractList.setStageStatus("标的结束时间为空，统一数据到第一个节点");
                                      contractLists.add(contractList);
                                      row++;
                                  }
                              }
                            Integer nb = 0;
                            Integer nc = 0;
                            String var1 = runningWater.getOne();
                            String var2 = runningWater.getTwo();
                            String var3 = runningWater.getThree();
                            String var4 = runningWater.getFour();
                            if (var4 != null && !var4.equals("")) {
                                nb = 4;
                            } else if (var3 != null && !var3.equals("")) {
                                nb = 3;
                            } else if (var2 != null && !var2.equals("")) {
                                nb = 2;
                            } else if (var1 != null && !var1.equals("")) {
                                nb = 1;
                            }
                            TimeList t1 = tL.get(0);
                            TimeList t2 = tL.get(1);
                            TimeList t3 = tL.get(2);
                            if (t3.getTime() !=null) {
                                nc = 3;
                            } else if (t2.getTime()!=null) {
                                nc = 2;
                            } else if (t1.getTime()!=null) {
                                nc = 1;
                            }

                            if (nc == 1) {
                                if (tL.size() > 0) {
                                    ContractList contractList = new ContractList();
                                    contractList.setSource("存货");
                                    contractList.setTax("6");
                                    contractList.setDiscount("100");
                                    contractList.setRows(row);
                                    contractList.setCode(contract.getCode());
                                    contractList.setName(contract.getName());
                                    contractList.setConName(tL.get(0).getName());
                                    contractList.setdCode(contract.getType());
                                    String fcode = contract.getType().substring(0, 2);
                                    contractList.setfCode(fcode);
                                    contractList.setbCode(fcode + contract.getType());
                                    //对数量进行处理
                                    String money = contract.getManuscript();
                                    Double aMoney = Double.valueOf(money);
                                    contractList.setMoney(money);
                                    //比例

                                    Double con = Double.valueOf(runningWater.getOne());
                                    Double noTe = aMoney * con;
                                    contractList.setCount("1.0");
                                    contractList.setNoTex(df.format(noTe));
                                    String inTex = getOnTex(noTe.toString());
                                    contractList.setInTex(inTex);

                                    contractList.setEndTime(tL.get(0).getTime());
                                    contractList.setStageStatus("小于等于第一个付款节点");
                                    contractList.setStatus(contract.getStatus());
                                    contractList.setTime(contract.getTime());
                                    contractLists.add(contractList);
                                    row++;
                                }
                            } else if (nc >= nb) {
                                if (tL.size() > 0) {
                                    TimeList sa = tL.get(tL.size() - 1);
                                    List<Long> list = new ArrayList<>();
                                    for (TimeList l : tLst) {
                                        list.add(l.getId());
                                    }
                                    int i1 = list.indexOf(sa.getId());
                                    Double las = Double.valueOf(0);
                                    Double lam = Double.valueOf(0);
                                    for (int i = 0; i <= i1; i++) {

                                        ContractList contractList = new ContractList();
                                        contractList.setSource("存货");
                                        contractList.setTax("6");
                                        contractList.setDiscount("100");
                                        contractList.setRows(row);
                                        contractList.setCode(contract.getCode());
                                        contractList.setName(contract.getName());
                                        contractList.setConName(tLst.get(i).getName());
                                        contractList.setdCode(contract.getType());
                                        String fcode = contract.getType().substring(0, 2);
                                        contractList.setfCode(fcode);
                                        contractList.setbCode(fcode + contract.getType());
                                        //对数量进行处理
                                        String money = contract.getManuscript();
                                        Double aMoney = Double.valueOf(money);
                                        contractList.setMoney(money);

                                        if (money != null && i != i1) {
                                            contractList.setMoney(money);
                                            String pro = stocks.get(i).getProportion();
                                            las = las + Double.valueOf(pro);
                                            contractList.setCount(pro);
                                            String onTex = getInTex(money, pro);
                                            lam = lam + Double.valueOf(onTex);
                                            contractList.setNoTex(onTex);
                                            String inTex = getOnTex(onTex);
                                            contractList.setInTex(inTex);
                                        }
                                        //比例
                                        if (i == i1) {
                                            Double con = Double.valueOf(runningWater.getOne());
                                            Double endM = 1 - las;
                                            Double endD = Double.valueOf(money) - lam;
                                            contractList.setCount(df.format(endM));
                                            contractList.setNoTex(df.format(endD));
                                            String inTex = getOnTex(endD.toString());
                                            contractList.setInTex(inTex);
                                        }
                                        contractList.setEndTime(tLst.get(i).getTime());
                                        contractList.setStageStatus("大于于第一个付款节点");
                                        contractList.setStatus(contract.getStatus());
                                        contractList.setTime(contract.getTime());
                                        contractLists.add(contractList);
                                        row++;
                                    }
                                }
                            } else if (1 < nc && nc < nb) {
                                System.out.println("特殊情况！！！");
//
//                                Double lab = Double.valueOf(0);
//                                Double lad = Double.valueOf(0);
//                                String money = contract.getManuscript();
//                                for (int i = 0; i < stocks.size(); i++) {
//                                    if (ttttts.size() > 0) {
//                                        if (money != null) {
//                                            String pro = stocks.get(i).getProportion();
//                                            lab = lab + Double.valueOf(pro);
//                                        }
//                                        if (stocks.get(i).getName().equals(timeLists.get(timeLists.size() - 1))) {
//                                            lad = lad + lab;
//                                        }
//                                    }
//                                }
//                                String o1 = runningWater.getOne();
//                                String o2 = runningWater.getTwo();
//                                String o3 = runningWater.getThree();
//                                String o4 = runningWater.getFour();
//                                String nowName = null;
//                                List<TimeList> timeL = null;
//                                Double dTime = Double.valueOf(0);
//                                if (o4 != null) {
//                                    nowName = "发行与上市";
//                                    Double.valueOf(o4);
//                                    timeL = timeListMapper.selectByConCode(code, nowName);
//                                    if (timeL != null) {
//                                        dTime = dTime + Double.valueOf(o4) + Double.valueOf(o3) + Double.valueOf(o2) + Double.valueOf(o1);
//                                    }
//                                } else if (o3 != null) {
//                                    nowName = "上会";
//                                    timeL = timeListMapper.selectByConCode(code, nowName);
//                                    if (timeL != null) {
//                                        dTime = dTime + Double.valueOf(o3) + Double.valueOf(o2) + Double.valueOf(o1);
//                                    }
//                                } else if (o2 != null) {
//                                    nowName = "反馈";
//                                    timeL = timeListMapper.selectByConCode(code, nowName);
//                                    if (timeL != null) {
//                                        dTime = dTime + Double.valueOf(o2) + Double.valueOf(o1);
//                                    }
//                                } else if (o1 != null) {
//                                    nowName = "申报";
//                                    timeL = timeListMapper.selectByConCode(code, nowName);
//                                    if (timeL != null) {
//                                        dTime = dTime + Double.valueOf(o1);
//                                    }
//                                }
//                                if (dTime == lad) {
//                                    for (int i = 0; i < stocks.size(); i++) {
//                                        List<TimeList> tie = timeListMapper.selectByConCode(code, stocks.get(i).getName());
//                                        if (tie.size() > 0) {
//                                            ContractList contractList = new ContractList();
//                                            contractList.setSource("存货");
//                                            contractList.setTax("6");
//                                            contractList.setDiscount("100");
//                                            contractList.setRows(row);
//                                            contractList.setCode(contract.getCode());
//                                            contractList.setName(contract.getName());
//                                            contractList.setConName(stocks.get(i).getName());
//                                            contractList.setdCode(contract.getType());
//                                            String fcode = contract.getType().substring(0, 2);
//                                            contractList.setfCode(fcode);
//                                            contractList.setbCode(fcode + contract.getType());
//                                            contractList.setCount(stocks.get(i).getProportion());
//                                            if (money != null) {
//                                                contractList.setMoney(money);
//                                                String onTex = getInTex(money, stocks.get(i).getProportion());
//                                                contractList.setNoTex(onTex);
//                                                String inTex = getOnTex(onTex);
//                                                contractList.setInTex(inTex);
//                                            }
//                                            List<TimeList> endtime = timeListMapper.selectCode(contract.getCode(), stocks.get(i).getName());
//                                            if (endtime.size() > 0) {
//                                                contractList.setEndTime(endtime.get(0).getTime());
//                                            }
//                                            if (conNames.equals("软件")) {
//                                                String endTimes = getEndTime(stocks.get(i).getName(), contract.getqTime(), i, Double.valueOf(contract.getrYear()));
//                                                contractList.setEndTime(endTimes);
//                                            }
//                                            contractList.setStatus(contract.getStatus());
//                                            contractList.setTime(contract.getTime());
//                                            contractLists.add(contractList);
//                                            row++;
//                                        }
//                                    }
//                                } else if (dTime > lad) {
//                                    Double nva = Double.valueOf(0);
//                                    Double nvb = Double.valueOf(0);
//                                    for (int i = 0; i < stocks.size(); i++) {
//                                        if (stocks.get(i).getName().equals(timeLists.get(timeLists.size() - 1))) {
//                                            nvb = nvb + nva;
//                                        }
//                                        if (money != null) {
//                                            String pro = stocks.get(i).getProportion();
//                                            nva = nva + Double.valueOf(pro);
//                                        }
//                                    }
//                                    ContractList contractList = new ContractList();
//                                    contractList.setSource("存货");
//                                    contractList.setTax("6");
//                                    contractList.setDiscount("100");
//                                    contractList.setRows(row);
//                                    contractList.setCode(contract.getCode());
//                                    contractList.setName(contract.getName());
//                                    contractList.setConName(stocks.get(stocks.size() - 1).getName());
//                                    contractList.setdCode(contract.getType());
//                                    String fcode = contract.getType().substring(0, 2);
//                                    contractList.setfCode(fcode);
//                                    contractList.setbCode(fcode + contract.getType());
//                                    Double nvc = Double.valueOf(1) - nvb;
//                                    contractList.setCount(df.format(nvc));
//                                    if (money != null) {
//                                        contractList.setMoney(money);
//                                        Double nM = Double.valueOf(money) * nvc;
//                                        contractList.setNoTex(df.format(nM));
//                                        String inTex = getOnTex(df.format(nM));
//                                        contractList.setInTex(inTex);
//                                    }
//                                    List<TimeList> endtime = timeListMapper.selectCode(contract.getCode(), stocks.get(stocks.size() - 1).getName());
//                                    if (endtime.size() > 0) {
//                                        contractList.setEndTime(endtime.get(0).getTime());
//                                    }
//                                    contractList.setStatus(contract.getStatus());
//                                    contractList.setTime(contract.getTime());
//                                    contractLists.add(contractList);
//                                    row++;
//                                } else if (dTime < lad) {
//                                    for (int i = 0; i < stocks.size(); i++) {
//                                        List<TimeList> tie = timeListMapper.selectByConCode(code, stocks.get(i).getName());
//                                        if (tie.size() > 0) {
//                                            ContractList contractList = new ContractList();
//                                            contractList.setSource("存货");
//                                            contractList.setTax("6");
//                                            contractList.setDiscount("100");
//                                            contractList.setRows(row);
//                                            contractList.setCode(contract.getCode());
//                                            contractList.setName(contract.getName());
//                                            contractList.setConName(stocks.get(i).getName());
//                                            contractList.setdCode(contract.getType());
//                                            String fcode = contract.getType().substring(0, 2);
//                                            contractList.setfCode(fcode);
//                                            contractList.setbCode(fcode + contract.getType());
//                                            String ppppp = stocks.get(i).getProportion();
//                                            Double ddddd = Double.valueOf(ppppp) * dTime;
//                                            contractList.setCount(df.format(ddddd));
//                                            if (money != null) {
//                                                Double laM = Double.valueOf(money) * dTime;
//                                                contractList.setMoney(df.format(laM));
//                                                String onTex = getInTex(df.format(laM), stocks.get(i).getProportion());
//                                                contractList.setNoTex(onTex);
//                                                String inTex = getOnTex(onTex);
//                                                contractList.setInTex(inTex);
//                                            }
//                                            List<TimeList> endtime = timeListMapper.selectCode(contract.getCode(), stocks.get(i).getName());
//                                            if (endtime.size() > 0) {
//                                                contractList.setEndTime(endtime.get(0).getTime());
//                                            }
//                                            if (conNames.equals("软件")) {
//                                                String endTimes = getEndTime(stocks.get(i).getName(), contract.getqTime(), i, Double.valueOf(contract.getrYear()));
//                                                contractList.setEndTime(endTimes);
//                                            }
//                                            contractList.setStatus(contract.getStatus());
//                                            contractList.setTime(contract.getTime());
//                                            contractLists.add(contractList);
//                                            row++;
//                                        }
//                                    }
//                                }
                            }
                        } else if (aDouble > 1) {
                            if (tL.size() < 1) {
                                if (tLst.size() > 0) {
                                    ContractList contractList = new ContractList();
                                    contractList.setSource("存货");
                                    contractList.setTax("6");
                                    contractList.setDiscount("100");
                                    contractList.setRows(row);
                                    contractList.setCode(contract.getCode());
                                    contractList.setName(contract.getName());
                                    contractList.setConName(tLst.get(0).getName());
                                    contractList.setdCode(contract.getType());
                                    String fcode = contract.getType().substring(0, 2);
                                    contractList.setfCode(fcode);
                                    contractList.setbCode(fcode + contract.getType());
                                    //对数量进行处理
                                    contractList.setCount("1.0");
//                                contractList.setCount(stocks.get(timeLists.size()-1).getProportion());
                                    String money = contract.getManuscript();
                                    Double amon = Double.valueOf(money);
                                    contractList.setMoney(money);
                                    contractList.setNoTex(money);
                                    String inTex = getOnTex(money);
                                    contractList.setInTex(inTex);
                                    contractList.setStatus(contract.getStatus());
                                    contractList.setTime(contract.getTime());
                                    contractList.setStageStatus("标的结束时间为空，统一数据到第一个节点");
                                    contractLists.add(contractList);
                                    row++;
                                }
                            }
                            Integer nb = 0;
                            Integer nc = 0;
                            String var1 = runningWater.getOne();
                            String var2 = runningWater.getTwo();
                            String var3 = runningWater.getThree();
                            String var4 = runningWater.getFour();
                            if (var4 != null && !var4.equals("")) {
                                nb = 4;
                            } else if (var3 != null && !var3.equals("")) {
                                nb = 3;
                            } else if (var2 != null && !var2.equals("")) {
                                nb = 2;
                            } else if (var1 != null && !var1.equals("")) {
                                nb = 1;
                            }
                            TimeList t1 = tL.get(0);
                            TimeList t2 = tL.get(1);
                            TimeList t3 = tL.get(2);
                            if (t3.getTime() !=null) {
                                nc = 3;
                            } else if (t2.getTime()!=null) {
                                nc = 2;
                            } else if (t1.getTime()!=null) {
                                nc = 1;
                            }

                            if (nc == 1) {

                                for (int i = 0; i < tL.size(); i++) {
                                    ContractList contractList = new ContractList();
                                    contractList.setSource("存货");
                                    contractList.setTax("6");
                                    contractList.setDiscount("100");
                                    contractList.setRows(row);
                                    contractList.setCode(contract.getCode());
                                    contractList.setName(contract.getName());
                                    contractList.setConName(tL.get(i).getName());
                                    contractList.setdCode(contract.getType());
                                    String fcode = contract.getType().substring(0, 2);
                                    contractList.setfCode(fcode);
                                    contractList.setbCode(fcode + contract.getType());
                                    //对数量进行处理
                                    String money = contract.getManuscript();
                                    Double aMoney = Double.valueOf(money);
                                    contractList.setMoney(money);
                                    //比例
                                    if (i == 0) {
                                        Double con = Double.valueOf(runningWater.getOne());
                                        Double nowMon = Double.valueOf(one) / aMoney;
                                        contractList.setCount(df.format(nowMon));
                                        contractList.setNoTex(df.format(con));
                                        String inTex = getOnTex(df.format(con));
                                        contractList.setInTex(inTex);
                                    }
                                    contractList.setEndTime(tL.get(i).getTime());
                                    contractList.setStageStatus("小于等于第一个付款节点");
                                    contractList.setStatus(contract.getStatus());
                                    contractList.setTime(contract.getTime());
                                    contractLists.add(contractList);
                                    row++;
                                }
                            } else if (nc >= nb) {
                                TimeList sa = tLst.get(tLst.size() - 1);
                                List<Long> list = new ArrayList<>();
                                for (TimeList l : tL) {
                                    list.add(l.getId());
                                }
                                int i1 = list.indexOf(sa.getId());
                                Double las = Double.valueOf(0);
                                Double lam = Double.valueOf(0);
                                for (int i = 0; i <= i1; i++) {

                                    ContractList contractList = new ContractList();
                                    contractList.setSource("存货");
                                    contractList.setTax("6");
                                    contractList.setDiscount("100");
                                    contractList.setRows(row);
                                    contractList.setCode(contract.getCode());
                                    contractList.setName(contract.getName());
                                    contractList.setConName(tL.get(i).getName());
                                    contractList.setdCode(contract.getType());
                                    String fcode = contract.getType().substring(0, 2);
                                    contractList.setfCode(fcode);
                                    contractList.setbCode(fcode + contract.getType());
                                    //对数量进行处理
                                    String money = contract.getManuscript();
                                    Double aMoney = Double.valueOf(money);
                                    contractList.setMoney(money);

                                    if (money != null && i != i1) {
                                        contractList.setMoney(money);
                                        String pro = stocks.get(i).getProportion();
                                        las = las + Double.valueOf(pro);
                                        contractList.setCount(pro);
                                        String onTex = getInTex(money, pro);
                                        lam = lam + Double.valueOf(onTex);
                                        contractList.setNoTex(onTex);
                                        String inTex = getOnTex(onTex);
                                        contractList.setInTex(inTex);
                                    }
                                    //比例
                                    if (i == i1) {
                                        Double con = Double.valueOf(runningWater.getOne());
                                        Double endM = 1 - las;
                                        Double endD = Double.valueOf(money) - lam;
                                        contractList.setCount(df.format(endM));
                                        contractList.setNoTex(df.format(endD));
                                        String inTex = getOnTex(endD.toString());
                                        contractList.setInTex(inTex);
                                    }
                                    contractList.setEndTime(tL.get(i).getTime());
                                    contractList.setStageStatus("大于于第一个付款节点");
                                    contractList.setStatus(contract.getStatus());
                                    contractList.setTime(contract.getTime());
                                    contractLists.add(contractList);
                                    row++;
                                }
                            } else if (1 < nc && nc < nb) {
                                System.out.println("超过1w的特殊情况");
//                                Double lab = Double.valueOf(0);
//                                Double lad = Double.valueOf(0);
//                                String money = getRows(conSp[j], contract);
//                                for (int i = 0; i < stocks.size(); i++) {
//                                    List<TimeList> ttttts = timeListMapper.selectByConCode(code, stocks.get(i).getName());
//                                    if (ttttts.size() > 0) {
//                                        if (money != null) {
//                                            String pro = stocks.get(i).getProportion();
//                                            lab = lab + Double.valueOf(pro);
//                                        }
//                                        if (stocks.get(i).getName().equals(timeLists.get(timeLists.size() - 1))) {
//                                            lad = lad + lab;
//                                        }
//                                    }
//                                }
//                                String o1 = runningWater.getOne();
//                                String o2 = runningWater.getTwo();
//                                String o3 = runningWater.getThree();
//                                String o4 = runningWater.getFour();
//                                String nowName = null;
//                                List<TimeList> timeL = null;
//                                Double dTime = Double.valueOf(0);
//                                if (o4 != null) {
//                                    nowName = "发行与上市";
//                                    Double.valueOf(o4);
//                                    timeL = timeListMapper.selectByConCode(code, nowName);
//                                    if (timeL != null) {
//                                        dTime = dTime + Double.valueOf(o4) + Double.valueOf(o3) + Double.valueOf(o2) + Double.valueOf(o1);
//                                    }
//                                } else if (o3 != null) {
//                                    nowName = "上会";
//                                    timeL = timeListMapper.selectByConCode(code, nowName);
//                                    if (timeL != null) {
//                                        dTime = dTime + Double.valueOf(o3) + Double.valueOf(o2) + Double.valueOf(o1);
//                                    }
//                                } else if (o2 != null) {
//                                    nowName = "反馈";
//                                    timeL = timeListMapper.selectByConCode(code, nowName);
//                                    if (timeL != null) {
//                                        dTime = dTime + Double.valueOf(o2) + Double.valueOf(o1);
//                                    }
//                                } else if (o1 != null) {
//                                    nowName = "申报";
//                                    timeL = timeListMapper.selectByConCode(code, nowName);
//                                    if (timeL != null) {
//                                        dTime = dTime + Double.valueOf(o1);
//                                    }
//                                }
//                                Double lac = Double.valueOf(money) * lad;
//                                if (dTime == lac) {
//                                    for (int i = 0; i < stocks.size(); i++) {
//                                        List<TimeList> tie = timeListMapper.selectByConCode(code, stocks.get(i).getName());
//                                        if (tie.size() > 0) {
//                                            ContractList contractList = new ContractList();
//                                            contractList.setSource("存货");
//                                            contractList.setTax("6");
//                                            contractList.setDiscount("100");
//                                            contractList.setRows(row);
//                                            contractList.setCode(contract.getCode());
//                                            contractList.setName(contract.getName());
//                                            contractList.setConName(stocks.get(i).getName());
//                                            contractList.setdCode(contract.getType());
//                                            String fcode = contract.getType().substring(0, 2);
//                                            contractList.setfCode(fcode);
//                                            contractList.setbCode(fcode + contract.getType());
//                                            contractList.setCount(stocks.get(i).getProportion());
//                                            if (money != null) {
//                                                contractList.setMoney(money);
//                                                String onTex = getInTex(money, stocks.get(i).getProportion());
//                                                contractList.setNoTex(onTex);
//                                                String inTex = getOnTex(onTex);
//                                                contractList.setInTex(inTex);
//                                            }
//                                            List<TimeList> endtime = timeListMapper.selectCode(contract.getCode(), stocks.get(i).getName());
//                                            if (endtime.size() > 0) {
//                                                contractList.setEndTime(endtime.get(0).getTime());
//                                            }
//                                            contractList.setStatus(contract.getStatus());
//                                            contractList.setTime(contract.getTime());
//                                            if (conNames.equals("软件")) {
//                                                String endTimes = getEndTime(stocks.get(i).getName(), contract.getqTime(), i, Double.valueOf(contract.getrYear()));
//                                                contractList.setEndTime(endTimes);
//                                            }
//                                            contractLists.add(contractList);
//                                            row++;
//                                        }
//                                    }
//                                } else if (dTime > lac) {
//                                    Double nva = Double.valueOf(0);
//                                    Double nvb = Double.valueOf(0);
//                                    for (int i = 0; i < stocks.size(); i++) {
//                                        if (stocks.get(i).getName().equals(timeLists.get(timeLists.size() - 1))) {
//                                            nvb = nvb + nva;
//                                        }
//                                        if (money != null) {
//                                            String pro = stocks.get(i).getProportion();
//                                            nva = nva + Double.valueOf(pro);
//                                        }
//                                    }
//                                    ContractList contractList = new ContractList();
//                                    contractList.setSource("存货");
//                                    contractList.setTax("6");
//                                    contractList.setDiscount("100");
//                                    contractList.setRows(row);
//                                    contractList.setCode(contract.getCode());
//                                    contractList.setName(contract.getName());
//                                    contractList.setConName(stocks.get(stocks.size() - 1).getName());
//                                    contractList.setdCode(contract.getType());
//                                    String fcode = contract.getType().substring(0, 2);
//                                    contractList.setfCode(fcode);
//                                    contractList.setbCode(fcode + contract.getType());
//                                    Double nvc = Double.valueOf(1) - nvb;
//                                    contractList.setCount(df.format(nvc));
//                                    if (money != null) {
//                                        contractList.setMoney(money);
//                                        Double nM = Double.valueOf(money) * nvc;
//                                        contractList.setNoTex(df.format(nM));
//                                        String inTex = getOnTex(df.format(nM));
//                                        contractList.setInTex(inTex);
//                                    }
//                                    List<TimeList> endtime = timeListMapper.selectCode(contract.getCode(), stocks.get(stocks.size() - 1).getName());
//                                    if (endtime.size() > 0) {
//                                        contractList.setEndTime(endtime.get(0).getTime());
//                                    }
//                                    contractList.setStatus(contract.getStatus());
//                                    contractList.setTime(contract.getTime());
//                                    contractLists.add(contractList);
//                                    row++;
//                                } else if (dTime < lac) {
//                                    for (int i = 0; i < stocks.size(); i++) {
//                                        List<TimeList> tie = timeListMapper.selectByConCode(code, stocks.get(i).getName());
//                                        if (tie.size() > 0) {
//                                            ContractList contractList = new ContractList();
//                                            contractList.setSource("存货");
//                                            contractList.setTax("6");
//                                            contractList.setDiscount("100");
//                                            contractList.setRows(row);
//                                            contractList.setCode(contract.getCode());
//                                            contractList.setName(contract.getName());
//                                            contractList.setConName(stocks.get(i).getName());
//                                            contractList.setdCode(contract.getType());
//                                            String fcode = contract.getType().substring(0, 2);
//                                            contractList.setfCode(fcode);
//                                            contractList.setbCode(fcode + contract.getType());
//                                            String ppppp = stocks.get(i).getProportion();
//                                            Double ddddd = Double.valueOf(ppppp) * dTime;
//                                            contractList.setCount(df.format(ddddd));
//                                            if (money != null) {
//                                                Double laM = Double.valueOf(money) * dTime;
//                                                contractList.setMoney(df.format(laM));
//                                                String onTex = getInTex(df.format(laM), stocks.get(i).getProportion());
//                                                contractList.setNoTex(onTex);
//                                                String inTex = getOnTex(onTex);
//                                                contractList.setInTex(inTex);
//                                            }
//
//                                                contractList.setEndTime(endtime.get(0).getTime());
//
//                                            contractList.setStatus(contract.getStatus());
//                                            contractList.setTime(contract.getTime());
//                                            contractLists.add(contractList);
//                                            row++;
//                                        }
//                                    }
//                                }
                            }
                        }
                    }
                }
            }
        }
        return contractLists;
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

}
