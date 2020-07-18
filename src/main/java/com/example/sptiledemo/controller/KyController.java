package com.example.sptiledemo.controller;


import com.example.sptiledemo.bean.Contract;
import com.example.sptiledemo.bean.ContractList;
import com.example.sptiledemo.bean.Stock;
import com.example.sptiledemo.mapper.RunningWaterMapper;
import com.example.sptiledemo.mapper.StockMapper;
import com.example.sptiledemo.service.ContractExcelService;
import com.example.sptiledemo.service.KyService;
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

import static com.example.sptiledemo.common.CalculationMoney.getOnTex;
import static com.example.sptiledemo.controller.TestController.getInTex;

@RestController
public class KyController {

    private static String outXlsPath = "C:\\Users\\31205\\Desktop\\数据\\荣大商务\\主体\\荣大科技\\可研\\可研整理.xlsx"; // 生成路径

    @Autowired
    private StockMapper stockMapper;


    @Autowired
    private RunningWaterMapper runningWaterMapper;

    @GetMapping("ky")
    public void test111() {
        List<Contract> contracts = KyService.getExcel();
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
        List<Stock> stocks = stockMapper.selectByConCode("0701", "可研");
        for (Contract contract : contracts) {
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
                String money = contract.getFeasibility();
                contractList.setEndTime(contract.getFaTime());
                if (money != null) {
                    contractList.setMoney(money);
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
