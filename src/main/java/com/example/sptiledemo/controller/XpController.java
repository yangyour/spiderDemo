package com.example.sptiledemo.controller;

import com.example.sptiledemo.bean.Contract;
import com.example.sptiledemo.bean.ContractList;
import com.example.sptiledemo.bean.Stock;
import com.example.sptiledemo.mapper.RunningWaterMapper;
import com.example.sptiledemo.mapper.StockMapper;
import com.example.sptiledemo.service.ContractExcelService;
import com.example.sptiledemo.service.XpService;
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

import static com.example.sptiledemo.common.CalculationMoney.getEndTime;
import static com.example.sptiledemo.common.CalculationMoney.getOnTex;
import static com.example.sptiledemo.controller.TestController.getInTex;
import static com.example.sptiledemo.controller.TestController.getRows;

@RestController
public class XpController {
    private static String outXlsPath = "D:\\输出文件\\信批整理.xlsx"; // 生成路径

    @Autowired
    private StockMapper stockMapper;


    @Autowired
    private RunningWaterMapper runningWaterMapper;

    @GetMapping("xp")
    public void test111() {
        List<Contract> contracts = XpService.getExcel();
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
                row0.createCell(33).setCellValue(contracts.get(i).getStatus()+contracts.get(i).getqTime());
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
        for (Contract contract : contracts) {
            String conName = contract.getConName();
            String[] conSp = conName.split("\\+");
            String code = contract.getCode();
            for (int j = 0; j < conSp.length; j++) {
                String conNames = conSp[j];
                conNames = conNames.replaceAll("合同", "");
                System.out.println(conNames);
                List<Stock> stocks = new ArrayList<>();
                if (conNames.equals("软件")) {
                    System.out.println("软件年份："+contract.getxYear());
                    if (contract.getrYear()!=null){
                        if (Double.valueOf(contract.getrYear()).equals(24.0)) {
                            List<Stock> st = new ArrayList<>();
                            List<Stock> st1 = stockMapper.selectCode("08", "ELS-24");
                            List<Stock> st2 = stockMapper.selectCode("08", "YXZ-24");
                            st.addAll(st1);
                            st.addAll(st2);
                            stocks = st;
                        } else if (Double.valueOf(contract.getrYear()).equals(12.0)) {
                            List<Stock> st = new ArrayList<>();
                            List<Stock> st1 = stockMapper.selectCode("08", "ELS-12");
                            List<Stock> st2 = stockMapper.selectCode("08", "YXZ-12");
                            st.addAll(st1);
                            st.addAll(st2);
                            stocks = st;
                        }
                    }
                }
                    if (conNames.equals("信披")) {
                        System.out.println("信批年份："+contract.getxYear());
                        if (contract.getxYear()!=null){
                            String toString=null;
                            if (contract.getxYear().equals("1/12")){
                                toString="XP-4-1";
                            }else {
                                Double integer = Double.valueOf(contract.getxYear());
                                if (integer!=null){
                                    if (integer.equals(Double.valueOf(1))){
                                        toString="XP-4";
                                    }else if (integer.equals(Double.valueOf(2))){
                                        toString="XP-8";
                                    }else if (integer.equals(Double.valueOf(3))){
                                        toString="XP-12";
                                    }
                            }
                            }
                            stocks = stockMapper.selectCode("06", toString);
                        }
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
                            String money = getRows(conSp[j], contract, stocks.get(i).getName());
                            if (money != null) {
                                contractList.setMoney(money);
                                String onTex = getInTex(money, stocks.get(i).getProportion());
                                contractList.setNoTex(onTex);
                                String inTex = getOnTex(onTex);
                                contractList.setInTex(inTex);
                            }
                            if (stocks.size()==1){
                                contractList.setMoney(money);
                                contractList.setNoTex(money);
                                String inTex = getOnTex(money);
                                contractList.setInTex(inTex);
                            }
                            if (conNames.equals("软件")) {
                                String endTimes = getEndTime(stocks.get(i).getName(), contract.getqTime(), i, Double.valueOf(contract.getrYear()));
                                contractList.setEndTime(endTimes);
                                if (Double.valueOf(contract.getrYear()).equals(24.0)) {
                                    if (i == 23 || i == 47) {
                                        contractList.setMoney(money);
                                        Double dw = Double.valueOf(money) - Double.valueOf(money) * Double.valueOf(23 / 24);
                                        contractList.setNoTex(df.format(dw));
                                        String inTex = getOnTex(df.format(dw));
                                        contractList.setInTex(inTex);
                                    }
                                } else if (Double.valueOf(contract.getrYear()).equals(12.0)) {
                                    if (i == 11 || i == 23) {
                                        contractList.setMoney(money);
                                        Double dw = Double.valueOf(money) - Double.valueOf(money) * Double.valueOf(11 / 12);
                                        contractList.setNoTex(df.format(dw));
                                        String inTex = getOnTex(df.format(dw));
                                        contractList.setInTex(inTex);
                                    }
                                }
                            }
                            if (conNames.equals("信披")) {
                                String xpti=getXpTime(code,stocks,i,contract.getFaTime());
                                contractList.setEndTime(xpti);
                            }
                            contractList.setStatus(contract.getStatus());
                            contractList.setTime(contract.getTime());
                            contractLists.add(contractList);
                            row++;
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



    private String getXpTime(String code, List<Stock> tL, int i, String times) {
        i=i+1;

        if (times!=null){
            String time = times;
            String[] sp = time.split("-");
            String endTime = null;
            Integer day = Integer.valueOf(sp[2]);
            Integer mon = Integer.valueOf(sp[1]);
            Integer year = Integer.valueOf(sp[0]);
            if (mon+(3*i)>12){
                mon=mon+(3*i)-12;
                year=year+1;
                if (mon>12){
                    mon=mon-12;
                    year=year+1;
                    if (mon>12){
                        mon=mon-12;
                        year=year+1;
                        if (mon>12){
                            mon=mon-12;
                            year=year+1;
                        }
                    }
                }
            }else {
                mon=mon+(3*i);
            }
            String nti=year.toString()+"-"+mon.toString()+"-"+day.toString();
            return nti;
        }else {
            return null;
        }
    }
}
