package com.example.sptiledemo.controller;

import com.example.sptiledemo.bean.*;
import com.example.sptiledemo.common.CalculationMoney;
import com.example.sptiledemo.mapper.RunningWaterMapper;
import com.example.sptiledemo.mapper.StockMapper;
import com.example.sptiledemo.mapper.TimeListMapper;
import com.example.sptiledemo.service.ContractExcelService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class TestController {


    private static String outXlsPath = "C:\\Users\\31205\\Desktop\\数据\\荣大商务\\全包打包\\荣大商务2017-2019全包打包合同台账整理.xlsx"; // 生成路径

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private TimeListMapper timeListMapper;

    @Autowired
    private RunningWaterMapper runningWaterMapper;



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
                List<Stock> stocks = stockMapper.selectByConCode(contract.getType(), conNames);
                if (conNames.contains("底稿")) {
                    stocks = stockMapper.selectByConCode("0501", conNames);
                }
                if (conNames.equals("可研")) {
                    stocks = stockMapper.selectByConCode("0701", conNames);
                }
                if (conNames.equals("软件")) {
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
                if (conNames.equals("信披")) {
                    Double integer = Double.valueOf(contract.getxYear());
                    String toString=null;
                    if (integer!=null){
                        if (integer.equals(Double.valueOf(1))){
                            toString="XP-4";
                        }else if (integer.equals(Double.valueOf(0.25))){
                            toString="XP-4-1";
                        }
                        stocks = stockMapper.selectCode("06", toString);

                    }
                }
                List<TimeList> tL = timeListMapper.selectCode(contract.getCode(), conNames);
                if (tL.size() > 0 &&tL.size()>=stocks.size() && tL.get(tL.size() - 1).getTime() != null && !tL.get(tL.size() - 1).getTime().equals("")) {
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
                        String money = getRows(conSp[j], contract,stocks.get(i).getName());
                        if (money != null) {
                            contractList.setMoney(money);
                            String onTex = getInTex(money, stocks.get(i).getProportion());
                            contractList.setNoTex(onTex);
                            String inTex = getOnTex(onTex);
                            contractList.setInTex(inTex);
                        }
                        if (tL.size()==1){
                            contractList.setMoney(money);
                            contractList.setNoTex(money);
                            String inTex = getOnTex(money);
                            contractList.setInTex(inTex);
                        }
                        contractList.setEndTime(tL.get(i).getTime());
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
                            String xpti=getXpTime(code,tL,i);
                            contractList.setEndTime(xpti);
                        }
                        if (tL.get(i).getTime() == null&&!conNames.equals("信批")) {

                            String time = getNullTime(contract, tL.get(i), tL, i);
                            String[] tp = time.split("-");
                            String s = tp[0] + tp[1] + tp[2];
                            Integer nowtime = Integer.valueOf(s);
                            List<TimeList> tLsss = timeListMapper.selectCode(contract.getCode(), "发行与上市");
                            if (tLsss.size() > 0) {
                                String tls = tLsss.get(0).getTime();
                                String[] ta = tls.split("-");
                                String ssss = ta[0] + ta[1] + ta[2];
                                Integer lasti = Integer.valueOf(ssss);
                                if (nowtime > lasti) {
                                    lC = lC + Double.valueOf(stocks.get(i).getProportion());
                                    lM = lM + Double.valueOf(getInTex(money, stocks.get(i).getProportion()));
                                    os = 10;
                                }
                            }
                            if (os == 0) {
                                contractList.setEndTime(time);
                            }
                        }
                        contractList.setStatus(contract.getStatus());
                        contractList.setTime(contract.getTime());
                        if (os == 0) {
                            contractLists.add(contractList);
                        }
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
                        String money = getRows(conSp[j], contract, stocks.get(i).getName());
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
                        if (conNames.equals("软件")) {
                            String endTimes = getEndTime(stocks.get(i).getName(), contract.getqTime(), i, Double.valueOf(contract.getrYear()));
                            contractList.setEndTime(endTimes);
                        }
                        if (conNames.equals("信披")) {
                            String xpti=getXpTime(code,tL,i);
                            contractList.setEndTime(xpti);
                        }
                        contractList.setStatus(contract.getStatus());
                        contractList.setTime(contract.getTime());
                        contractLists.add(contractList);
                        row++;
                    }
                } else if (contract.getStatus() != null) {
                    //当项目为终止状态时
                    List<RunningWater> runningWaters = runningWaterMapper.selectByConCode(contract.getCode());
                    if (runningWaters.size() > 0) {
                        RunningWater runningWater = runningWaters.get(0);
                        String proportion = null;
                        String one = runningWater.getOne();
                        if (one.equals("1.0")) {
                            List<TimeList> timeLists = timeListMapper.selectByConCode(code, conNames);
                            TimeList sa = timeLists.get(timeLists.size() - 1);
                            List<TimeList> sh1 = timeListMapper.selectCode(code, conNames);
                            List<Long> list = new ArrayList<>();
                            for (TimeList l : sh1) {
                                list.add(l.getId());
                            }
                            int i1 = list.indexOf(sa.getId());
                            Double fontMoney = Double.valueOf(0);
                            Double fontCount = Double.valueOf(0);
                            if (sh1.size() > 0) {
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
                                    String money = getRows(conSp[j], contract, stocks.get(i).getName());
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
                                    contractLists.add(contractList);
                                    contractList.setEndTime(sh1.get(i).getTime());
                                    if (conNames.equals("软件")) {
                                        String endTimes = getEndTime(stocks.get(i).getName(), contract.getqTime(), i, Double.valueOf(contract.getrYear()));
                                        contractList.setEndTime(endTimes);
                                    }
                                    if (conNames.equals("信披")) {
                                        String xpti=getXpTime(code,tL,i);
                                        contractList.setEndTime(xpti);
                                    }
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
                                String money = getRows(conSp[j], contract, stocks.get(0).getName());
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
                                contractLists.add(contractList);
                                List<TimeList> endtime = timeListMapper.selectByConCode(contract.getCode(), stocks.get(0).getName());
                                if (endtime.size() > 0) {
                                    contractList.setEndTime(endtime.get(0).getTime());
                                }
                                row++;
                            }
                        } else {
                            Double aDouble = Double.valueOf(one);
                            if (aDouble < 1) {
                                List<TimeList> timeLists = timeListMapper.selectByConCode(code, conNames);
                                if (timeLists.size() < 1) {
                                    List<TimeList> sbh = timeListMapper.selectCode(code, conNames);
                                    if (sbh.size() > 0) {
                                        ContractList contractList = new ContractList();
                                        contractList.setSource("存货");
                                        contractList.setTax("6");
                                        contractList.setDiscount("100");
                                        contractList.setRows(row);
                                        contractList.setCode(contract.getCode());
                                        contractList.setName(contract.getName());
                                        contractList.setConName(sbh.get(0).getName());
                                        contractList.setdCode(contract.getType());
                                        String fcode = contract.getType().substring(0, 2);
                                        contractList.setfCode(fcode);
                                        contractList.setbCode(fcode + contract.getType());
                                        //对数量进行处理
                                        contractList.setCount("1.0");
//                                contractList.setCount(stocks.get(timeLists.size()-1).getProportion());
                                        String money = getRows(conSp[j], contract, stocks.get(0).getName());
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
                                if (conNames.contains("底稿")) {
                                    stocks = stockMapper.selectByConCode("0501", conNames);
                                    timeLists = timeListMapper.selectByConCode(code, conNames);
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
                                List<TimeList> t1 = timeListMapper.selectByConCode(code, "申报");
                                List<TimeList> t2 = timeListMapper.selectByConCode(code, "反馈");
                                List<TimeList> t3 = timeListMapper.selectByConCode(code, "上会");
                                List<TimeList> t4 = timeListMapper.selectByConCode(code, "发行与上市");
                                if (t4.size() > 0) {
                                    nc = 4;
                                } else if (t3.size() > 0) {
                                    nc = 3;
                                } else if (t2.size() > 0) {
                                    nc = 2;
                                } else if (t1.size() > 0) {
                                    nc = 1;
                                }
                                if (code.equals("101-QB-13")){
                                    System.out.println("我在这"+nc+"-"+nb);
                                }
                                if (nc == 1) {
                                    List<TimeList> t10 = timeListMapper.selectByConCode(code, conNames);
                                    if (t10.size() > 0) {
                                        ContractList contractList = new ContractList();
                                        contractList.setSource("存货");
                                        contractList.setTax("6");
                                        contractList.setDiscount("100");
                                        contractList.setRows(row);
                                        contractList.setCode(contract.getCode());
                                        contractList.setName(contract.getName());
                                        contractList.setConName(t10.get(0).getName());
                                        contractList.setdCode(contract.getType());
                                        String fcode = contract.getType().substring(0, 2);
                                        contractList.setfCode(fcode);
                                        contractList.setbCode(fcode + contract.getType());
                                        //对数量进行处理
                                        String money = getRows(conSp[j], contract, stocks.get(0).getName());
                                        Double aMoney = Double.valueOf(money);
                                        contractList.setMoney(money);
                                        //比例

                                        Double con = Double.valueOf(runningWater.getOne());
                                        Double noTe = aMoney * con;
                                        contractList.setCount("1.0");
                                        contractList.setNoTex(df.format(noTe));
                                        String inTex = getOnTex(noTe.toString());
                                        contractList.setInTex(inTex);

                                        contractList.setEndTime(t10.get(0).getTime());
                                        contractList.setStageStatus("小于等于第一个付款节点");
                                        contractList.setStatus(contract.getStatus());
                                        contractList.setTime(contract.getTime());
                                        if (conNames.equals("软件")) {
                                            String endTimes = getEndTime(stocks.get(0).getName(), contract.getqTime(), 0, Double.valueOf(contract.getrYear()));
                                            contractList.setEndTime(endTimes);
                                        }
                                        contractLists.add(contractList);
                                        row++;
                                    }
                                } else if (nc==2){
                                    List<TimeList> tls = timeListMapper.selectByConCode(code, conNames);
                                    TimeList sa = tls.get(tls.size() - 1);
                                    List<TimeList> sh1 = timeListMapper.selectCode(code, conNames);
                                    List<Long> list = new ArrayList<>();
                                    for (TimeList l : sh1) {
                                        list.add(l.getId());
                                    }
                                    int i1 = list.indexOf(sa.getId());
                                    Double nNum=Double.valueOf(0);
                                    for (int i=0;i<=i1;i++){
                                        nNum=nNum+Double.valueOf(stocks.get(i).getProportion());
                                    }
                                    for (int i=0;i<=i1;i++){
                                        ContractList contractList = new ContractList();
                                        contractList.setSource("存货");
                                        contractList.setTax("6");
                                        contractList.setDiscount("100");
                                        contractList.setRows(row);
                                        contractList.setCode(contract.getCode());
                                        contractList.setName(contract.getName());
                                        contractList.setConName(sh1.get(i).getName());
                                        contractList.setdCode(contract.getType());
                                        String fcode = contract.getType().substring(0, 2);
                                        contractList.setfCode(fcode);
                                        contractList.setbCode(fcode + contract.getType());
                                        //对数量进行处理
                                        String money = getRows(conSp[j], contract, stocks.get(i).getName());
                                        Double aMoney = Double.valueOf(money);
                                        contractList.setMoney(money);
                                        //比例
                                        //获取第一阶段比例
                                        Double oD=Double.valueOf(one);
                                        //计算能收的金额
                                        Double noTe = aMoney * oD;
                                        //计算当前数量
                                        Double dC=Double.valueOf(stocks.get(i).getProportion())*(oD/nNum);
                                        //计算应税金额
                                        Double nowMo = aMoney * dC;
                                        contractList.setCount(df.format(dC));
                                        contractList.setNoTex(df.format(nowMo));
                                        String inTex = getOnTex(nowMo.toString());
                                        contractList.setInTex(inTex);

                                        contractList.setEndTime(sh1.get(i).getTime());
                                        contractList.setStageStatus("只到达了申报");
                                        contractList.setStatus(contract.getStatus());
                                        contractList.setTime(contract.getTime());
                                        if (conNames.equals("软件")) {
                                            String endTimes = getEndTime(stocks.get(i).getName(), contract.getqTime(), 0, Double.valueOf(contract.getrYear()));
                                            contractList.setEndTime(endTimes);
                                        }
                                        contractLists.add(contractList);
                                        row++;
                                    }
                                }else if (nc >= nb) {
                                    if (timeLists.size() > 0) {
                                        TimeList sa = timeLists.get(timeLists.size() - 1);
                                        List<TimeList> sh1 = timeListMapper.selectCode(code, conNames);
                                        List<Long> list = new ArrayList<>();
                                        for (TimeList l : sh1) {
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
                                            contractList.setConName(sh1.get(i).getName());
                                            contractList.setdCode(contract.getType());
                                            String fcode = contract.getType().substring(0, 2);
                                            contractList.setfCode(fcode);
                                            contractList.setbCode(fcode + contract.getType());
                                            //对数量进行处理
                                            String money = getRows(conSp[j], contract, stocks.get(i).getName());
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
                                            contractList.setEndTime(sh1.get(i).getTime());
                                            contractList.setStageStatus("大于于第一个付款节点");
                                            contractList.setStatus(contract.getStatus());
                                            contractList.setTime(contract.getTime());
                                            if (conNames.equals("软件")) {
                                                String endTimes = getEndTime(stocks.get(i).getName(), contract.getqTime(), i, Double.valueOf(contract.getrYear()));
                                                contractList.setEndTime(endTimes);
                                            }
                                            contractLists.add(contractList);
                                            row++;
                                        }
                                    }
                                } else if (1 < nc && nc < nb) {
                                    Double lab = Double.valueOf(0);
                                    Double lad = Double.valueOf(0);
                                    String money = getRows(conSp[j], contract, stocks.get(0).getName());
                                    for (int i = 0; i < stocks.size(); i++) {
                                        List<TimeList> ttttts = timeListMapper.selectByConCode(code, stocks.get(i).getName());
                                        if (ttttts.size() > 0) {
                                            if (money != null) {
                                                String pro = stocks.get(i).getProportion();
                                                lab = lab + Double.valueOf(pro);
                                            }
                                            if (stocks.get(i).getName().equals(timeLists.get(timeLists.size() - 1))) {
                                                lad = lad + lab;
                                            }
                                        }
                                    }
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
                                        timeL = timeListMapper.selectByConCode(code, nowName);
                                        if (timeL != null) {
                                            dTime = dTime + Double.valueOf(o4) + Double.valueOf(o3) + Double.valueOf(o2) + Double.valueOf(o1);
                                        }
                                    } else if (o3 != null) {
                                        nowName = "上会";
                                        timeL = timeListMapper.selectByConCode(code, nowName);
                                        if (timeL != null) {
                                            dTime = dTime + Double.valueOf(o3) + Double.valueOf(o2) + Double.valueOf(o1);
                                        }
                                    } else if (o2 != null) {
                                        nowName = "反馈";
                                        timeL = timeListMapper.selectByConCode(code, nowName);
                                        if (timeL != null) {
                                            dTime = dTime + Double.valueOf(o2) + Double.valueOf(o1);
                                        }
                                    } else if (o1 != null) {
                                        nowName = "申报";
                                        timeL = timeListMapper.selectByConCode(code, nowName);
                                        if (timeL != null) {
                                            dTime = dTime + Double.valueOf(o1);
                                        }
                                    }
                                    if (dTime == lad) {
                                        for (int i = 0; i < stocks.size(); i++) {
                                            List<TimeList> tie = timeListMapper.selectByConCode(code, stocks.get(i).getName());
                                            if (tie.size() > 0) {
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
                                                if (conNames.equals("软件")) {
                                                    String endTimes = getEndTime(stocks.get(i).getName(), contract.getqTime(), i, Double.valueOf(contract.getrYear()));
                                                    contractList.setEndTime(endTimes);
                                                }
                                                contractList.setStatus(contract.getStatus());
                                                contractList.setTime(contract.getTime());
                                                contractLists.add(contractList);
                                                row++;
                                            }
                                        }
                                    } else if (dTime > lad) {
                                        Double nva = Double.valueOf(0);
                                        Double nvb = Double.valueOf(0);
                                        for (int i = 0; i < stocks.size(); i++) {
                                            if (stocks.get(i).getName().equals(timeLists.get(timeLists.size() - 1))) {
                                                nvb = nvb + nva;
                                            }
                                            if (money != null) {
                                                String pro = stocks.get(i).getProportion();
                                                nva = nva + Double.valueOf(pro);
                                            }
                                        }
                                        ContractList contractList = new ContractList();
                                        contractList.setSource("存货");
                                        contractList.setTax("6");
                                        contractList.setDiscount("100");
                                        contractList.setRows(row);
                                        contractList.setCode(contract.getCode());
                                        contractList.setName(contract.getName());
                                        contractList.setConName(stocks.get(stocks.size() - 1).getName());
                                        contractList.setdCode(contract.getType());
                                        String fcode = contract.getType().substring(0, 2);
                                        contractList.setfCode(fcode);
                                        contractList.setbCode(fcode + contract.getType());
                                        Double nvc = Double.valueOf(1) - nvb;
                                        contractList.setCount(df.format(nvc));
                                        if (money != null) {
                                            contractList.setMoney(money);
                                            Double nM = Double.valueOf(money) * nvc;
                                            contractList.setNoTex(df.format(nM));
                                            String inTex = getOnTex(df.format(nM));
                                            contractList.setInTex(inTex);
                                        }
                                        List<TimeList> endtime = timeListMapper.selectCode(contract.getCode(), stocks.get(stocks.size() - 1).getName());
                                        if (endtime.size() > 0) {
                                            contractList.setEndTime(endtime.get(0).getTime());
                                        }
                                        contractList.setStatus(contract.getStatus());
                                        contractList.setTime(contract.getTime());
                                        contractLists.add(contractList);
                                        row++;
                                    } else if (dTime < lad) {
                                        for (int i = 0; i < stocks.size(); i++) {
                                            List<TimeList> tie = timeListMapper.selectByConCode(code, stocks.get(i).getName());
                                            if (tie.size() > 0) {
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
                                                String ppppp = stocks.get(i).getProportion();
                                                Double ddddd = Double.valueOf(ppppp) * dTime;
                                                contractList.setCount(df.format(ddddd));
                                                if (money != null) {
                                                    Double laM = Double.valueOf(money) * dTime;
                                                    contractList.setMoney(df.format(laM));
                                                    String onTex = getInTex(df.format(laM), stocks.get(i).getProportion());
                                                    contractList.setNoTex(onTex);
                                                    String inTex = getOnTex(onTex);
                                                    contractList.setInTex(inTex);
                                                }
                                                List<TimeList> endtime = timeListMapper.selectCode(contract.getCode(), stocks.get(i).getName());
                                                if (endtime.size() > 0) {
                                                    contractList.setEndTime(endtime.get(0).getTime());
                                                }
                                                if (conNames.equals("软件")) {
                                                    String endTimes = getEndTime(stocks.get(i).getName(), contract.getqTime(), i, Double.valueOf(contract.getrYear()));
                                                    contractList.setEndTime(endTimes);
                                                }
                                                contractList.setStatus(contract.getStatus());
                                                contractList.setTime(contract.getTime());
                                                contractLists.add(contractList);
                                                row++;
                                            }
                                        }
                                    }
                                }
                            } else if (aDouble > 1) {
                                List<TimeList> timeLists = timeListMapper.selectByConCode(code, conNames);
                                if (timeLists.size() < 1) {
                                    List<TimeList> sbh = timeListMapper.selectCode(code, conNames);
                                    if (sbh.size() > 0) {
                                        ContractList contractList = new ContractList();
                                        contractList.setSource("存货");
                                        contractList.setTax("6");
                                        contractList.setDiscount("100");
                                        contractList.setRows(row);
                                        contractList.setCode(contract.getCode());
                                        contractList.setName(contract.getName());
                                        contractList.setConName(sbh.get(0).getName());
                                        contractList.setdCode(contract.getType());
                                        String fcode = contract.getType().substring(0, 2);
                                        contractList.setfCode(fcode);
                                        contractList.setbCode(fcode + contract.getType());
                                        //对数量进行处理
                                        contractList.setCount("1.0");
//                                contractList.setCount(stocks.get(timeLists.size()-1).getProportion());
                                        String money = getRows(conSp[j], contract, stocks.get(0).getName());
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
                                //处理最后一个
                                if (conNames.contains("底稿")) {
                                    stocks = stockMapper.selectByConCode("0501", conNames);
                                    timeLists = timeListMapper.selectByConCode(code, conNames);
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
                                List<TimeList> t1 = timeListMapper.selectByConCode(code, "申报");
                                List<TimeList> t2 = timeListMapper.selectByConCode(code, "反馈");
                                List<TimeList> t3 = timeListMapper.selectByConCode(code, "上会");
                                List<TimeList> t4 = timeListMapper.selectByConCode(code, "发行与上市");
                                if (t4.size() > 0) {
                                    nc = 4;
                                } else if (t3.size() > 0) {
                                    nc = 3;
                                } else if (t2.size() > 0) {
                                    nc = 2;
                                } else if (t1.size() > 0) {
                                    nc = 1;
                                }
                                if (nc == 1) {
                                    List<TimeList> t10 = timeListMapper.selectByConCode(code, conNames);

                                    for (int i = 0; i < t10.size(); i++) {
                                        ContractList contractList = new ContractList();
                                        contractList.setSource("存货");
                                        contractList.setTax("6");
                                        contractList.setDiscount("100");
                                        contractList.setRows(row);
                                        contractList.setCode(contract.getCode());
                                        contractList.setName(contract.getName());
                                        contractList.setConName(t10.get(i).getName());
                                        contractList.setdCode(contract.getType());
                                        String fcode = contract.getType().substring(0, 2);
                                        contractList.setfCode(fcode);
                                        contractList.setbCode(fcode + contract.getType());
                                        //对数量进行处理
                                        String money = getRows(conSp[j], contract, stocks.get(i).getName());
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
                                        contractList.setEndTime(t10.get(i).getTime());
                                        contractList.setStageStatus("小于等于第一个付款节点");
                                        contractList.setStatus(contract.getStatus());
                                        contractList.setTime(contract.getTime());
                                        if (conNames.equals("软件")) {
                                            String endTimes = getEndTime(stocks.get(i).getName(), contract.getqTime(), i, Double.valueOf(contract.getrYear()));
                                            contractList.setEndTime(endTimes);
                                        }
                                        contractLists.add(contractList);
                                        row++;
                                    }
                                } else if (nc==2){
                                    List<TimeList> tls = timeListMapper.selectByConCode(code, conNames);
                                    TimeList sa = tls.get(tls.size() - 1);
                                    List<TimeList> sh1 = timeListMapper.selectCode(code, conNames);
                                    List<Long> list = new ArrayList<>();
                                    for (TimeList l : sh1) {
                                        list.add(l.getId());
                                    }
                                    int i1 = list.indexOf(sa.getId());
                                    Double nNum=Double.valueOf(0);
                                    for (int i=0;i<=i1;i++){
                                        nNum=nNum+Double.valueOf(stocks.get(i).getProportion());
                                    }
                                    for (int i=0;i<=i1;i++){
                                        ContractList contractList = new ContractList();
                                        contractList.setSource("存货");
                                        contractList.setTax("6");
                                        contractList.setDiscount("100");
                                        contractList.setRows(row);
                                        contractList.setCode(contract.getCode());
                                        contractList.setName(contract.getName());
                                        contractList.setConName(sh1.get(i).getName());
                                        contractList.setdCode(contract.getType());
                                        String fcode = contract.getType().substring(0, 2);
                                        contractList.setfCode(fcode);
                                        contractList.setbCode(fcode + contract.getType());
                                        //对数量进行处理
                                        String money = getRows(conSp[j], contract, stocks.get(i).getName());
                                        contractList.setMoney(money);
                                        //比例
                                        //获取第一阶段比例
                                        Double aMoney=Double.valueOf(one);
                                        //计算当前数量
                                        Double dC=Double.valueOf(stocks.get(i).getProportion())*((aDouble/Double.valueOf(money))/nNum);
                                        //计算应税金额
                                        Double nowMo = aMoney * dC;
                                        System.out.println("数量："+dC);
                                        contractList.setCount(df.format(dC));
                                        contractList.setNoTex(df.format(nowMo));
                                        String inTex = getOnTex(nowMo.toString());
                                        contractList.setInTex(inTex);

                                        contractList.setEndTime(sh1.get(i).getTime());
                                        contractList.setStageStatus("只到达了申报");
                                        contractList.setStatus(contract.getStatus());
                                        contractList.setTime(contract.getTime());
                                        if (conNames.equals("软件")) {
                                            String endTimes = getEndTime(stocks.get(i).getName(), contract.getqTime(), 0, Double.valueOf(contract.getrYear()));
                                            contractList.setEndTime(endTimes);
                                        }
                                        contractLists.add(contractList);
                                        row++;
                                    }
                                }else if (nc >= nb) {
                                    TimeList sa = timeLists.get(timeLists.size() - 1);
                                    List<TimeList> sh1 = timeListMapper.selectCode(code, conNames);
                                    List<Long> list = new ArrayList<>();
                                    for (TimeList l : sh1) {
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
                                        contractList.setConName(sh1.get(i).getName());
                                        contractList.setdCode(contract.getType());
                                        String fcode = contract.getType().substring(0, 2);
                                        contractList.setfCode(fcode);
                                        contractList.setbCode(fcode + contract.getType());
                                        //对数量进行处理
                                        String money = getRows(conSp[j], contract, stocks.get(i).getName());
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
                                        contractList.setEndTime(sh1.get(i).getTime());
                                        contractList.setStageStatus("大于于第一个付款节点");
                                        contractList.setStatus(contract.getStatus());
                                        contractList.setTime(contract.getTime());
                                        if (conNames.equals("软件")) {
                                            String endTimes = getEndTime(stocks.get(i).getName(), contract.getqTime(), i, Double.valueOf(contract.getrYear()));
                                            contractList.setEndTime(endTimes);
                                        }
                                        contractLists.add(contractList);
                                        row++;
                                    }
                                } else if (1 < nc && nc < nb) {
                                    Double lab = Double.valueOf(0);
                                    Double lad = Double.valueOf(0);
                                    String money = getRows(conSp[j], contract, stocks.get(0).getName());
                                    for (int i = 0; i < stocks.size(); i++) {
                                        List<TimeList> ttttts = timeListMapper.selectByConCode(code, stocks.get(i).getName());
                                        if (ttttts.size() > 0) {
                                            if (money != null) {
                                                String pro = stocks.get(i).getProportion();
                                                lab = lab + Double.valueOf(pro);
                                            }
                                            if (stocks.get(i).getName().equals(timeLists.get(timeLists.size() - 1))) {
                                                lad = lad + lab;
                                            }
                                        }
                                    }
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
                                        timeL = timeListMapper.selectByConCode(code, nowName);
                                        if (timeL != null) {
                                            dTime = dTime + Double.valueOf(o4) + Double.valueOf(o3) + Double.valueOf(o2) + Double.valueOf(o1);
                                        }
                                    } else if (o3 != null) {
                                        nowName = "上会";
                                        timeL = timeListMapper.selectByConCode(code, nowName);
                                        if (timeL != null) {
                                            dTime = dTime + Double.valueOf(o3) + Double.valueOf(o2) + Double.valueOf(o1);
                                        }
                                    } else if (o2 != null) {
                                        nowName = "反馈";
                                        timeL = timeListMapper.selectByConCode(code, nowName);
                                        if (timeL != null) {
                                            dTime = dTime + Double.valueOf(o2) + Double.valueOf(o1);
                                        }
                                    } else if (o1 != null) {
                                        nowName = "申报";
                                        timeL = timeListMapper.selectByConCode(code, nowName);
                                        if (timeL != null) {
                                            dTime = dTime + Double.valueOf(o1);
                                        }
                                    }
                                    Double lac = Double.valueOf(money) * lad;
                                    if (dTime == lac) {
                                        for (int i = 0; i < stocks.size(); i++) {
                                            List<TimeList> tie = timeListMapper.selectByConCode(code, stocks.get(i).getName());
                                            if (tie.size() > 0) {
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
                                                contractList.setTime(contract.getTime());
                                                if (conNames.equals("软件")) {
                                                    String endTimes = getEndTime(stocks.get(i).getName(), contract.getqTime(), i, Double.valueOf(contract.getrYear()));
                                                    contractList.setEndTime(endTimes);
                                                }
                                                contractLists.add(contractList);
                                                row++;
                                            }
                                        }
                                    } else if (dTime > lac) {
                                        Double nva = Double.valueOf(0);
                                        Double nvb = Double.valueOf(0);
                                        for (int i = 0; i < stocks.size(); i++) {
                                            if (stocks.get(i).getName().equals(timeLists.get(timeLists.size() - 1))) {
                                                nvb = nvb + nva;
                                            }
                                            if (money != null) {
                                                String pro = stocks.get(i).getProportion();
                                                nva = nva + Double.valueOf(pro);
                                            }
                                        }
                                        ContractList contractList = new ContractList();
                                        contractList.setSource("存货");
                                        contractList.setTax("6");
                                        contractList.setDiscount("100");
                                        contractList.setRows(row);
                                        contractList.setCode(contract.getCode());
                                        contractList.setName(contract.getName());
                                        contractList.setConName(stocks.get(stocks.size() - 1).getName());
                                        contractList.setdCode(contract.getType());
                                        String fcode = contract.getType().substring(0, 2);
                                        contractList.setfCode(fcode);
                                        contractList.setbCode(fcode + contract.getType());
                                        Double nvc = Double.valueOf(1) - nvb;
                                        contractList.setCount(df.format(nvc));
                                        if (money != null) {
                                            contractList.setMoney(money);
                                            Double nM = Double.valueOf(money) * nvc;
                                            contractList.setNoTex(df.format(nM));
                                            String inTex = getOnTex(df.format(nM));
                                            contractList.setInTex(inTex);
                                        }
                                        List<TimeList> endtime = timeListMapper.selectCode(contract.getCode(), stocks.get(stocks.size() - 1).getName());
                                        if (endtime.size() > 0) {
                                            contractList.setEndTime(endtime.get(0).getTime());
                                        }
                                        contractList.setStatus(contract.getStatus());
                                        contractList.setTime(contract.getTime());
                                        contractLists.add(contractList);
                                        row++;
                                    } else if (dTime < lac) {
                                        for (int i = 0; i < stocks.size(); i++) {
                                            List<TimeList> tie = timeListMapper.selectByConCode(code, stocks.get(i).getName());
                                            if (tie.size() > 0) {
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
                                                String ppppp = stocks.get(i).getProportion();
                                                Double ddddd = Double.valueOf(ppppp) * dTime;
                                                contractList.setCount(df.format(ddddd));
                                                if (money != null) {
                                                    Double laM = Double.valueOf(money) * dTime;
                                                    contractList.setMoney(df.format(laM));
                                                    String onTex = getInTex(df.format(laM), stocks.get(i).getProportion());
                                                    contractList.setNoTex(onTex);
                                                    String inTex = getOnTex(onTex);
                                                    contractList.setInTex(inTex);
                                                }
                                                List<TimeList> endtime = timeListMapper.selectCode(contract.getCode(), stocks.get(i).getName());
                                                if (endtime.size() > 0) {
                                                    contractList.setEndTime(endtime.get(0).getTime());
                                                }
                                                if (conNames.equals("软件")) {
                                                    String endTimes = getEndTime(stocks.get(i).getName(), contract.getqTime(), i, Double.valueOf(contract.getrYear()));
                                                    contractList.setEndTime(endTimes);
                                                }
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
        }
        return contractLists;
    }

    private String getXpTime(String code, List<TimeList> tL, int i) {
        i=i+1;
        List<TimeList> listList = timeListMapper.selectByConCode(code, "发行与上市");
        if (listList.size()>0){
            String time = listList.get(0).getTime();
            String[] sp = time.split("-");
            String endTime = null;
            Integer day = Integer.valueOf(sp[2]);
            Integer mon = Integer.valueOf(sp[1]);
            Integer year = Integer.valueOf(sp[0]);
            if (mon+(3*i)>12){
                mon=mon+(3*i)-12;
                year=year+1;
            }else {
                mon=mon+(3*i);
            }
            String nti=year.toString()+"-"+mon.toString()+"-"+day.toString();
            return nti;
        }else {
            return null;
        }
    }

    private String getNullTime(Contract contract, TimeList tL, List<TimeList> l, int i) {
        String name = tL.getName();
        List<TimeList> listList = timeListMapper.selectByConCode(contract.getCode(), "申报");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM

        if (listList.size() > 0) {
            String time = listList.get(0).getTime();
            String[] sp = time.split("-");
            String endTime = null;
            Integer day = Integer.valueOf(sp[2]);
            Integer mon = Integer.valueOf(sp[1]);
            Integer year = Integer.valueOf(sp[0]);

            if (mon == 1 && mon == 2 && mon == 3 && mon == 4 && mon == 5 && mon == 6) {
                if (name.contains("补中报")) {
                    Integer da = (int) (9 + Math.random() * (25 - 9 + 1));
                    String bu = year.toString() + "-" + "09" + "-" + da.toString();
                    return bu;
                } else if (name.contains("补年报")) {
                    Integer da = (int) (9 + Math.random() * (25 - 9 + 1));
                    Integer y = year + 1;
                    String bu = y.toString() + "-" + "03" + "-" + da.toString();
                    return bu;
                } else {
                    if (i > 0) {
                        String time2 = l.get(i - 1).getTime();
                        String time3 = l.get(i + 1).getTime();
                        if (!time2.equals(time3)){
                            Date date = CalculationMoney.randomDate(time2, time3);
                            String format = simpleDateFormat.format(date);
                            return format;
                        }else {
                            return time2;
                        }
                    }
                }
            } else {
                if (name.contains("补中报")) {
                    Integer da = (int) (9 + Math.random() * (25 - 9 + 1));
                    Integer y = year + 1;
                    String bu = y.toString() + "-" + "09" + "-" + da.toString();
                    return bu;
                } else if (name.contains("补年报")) {
                    Integer da = (int) (9 + Math.random() * (25 - 9 + 1));
                    Integer y = year + 1;
                    String bu = y.toString() + "-" + "03" + "-" + da.toString();
                    return bu;
                } else {
                    if (i > 0) {
                        String time2 = l.get(i - 1).getTime();
                        String time3 = l.get(i + 1).getTime();
                        if (!time2.equals(time3)){
                            Date date = CalculationMoney.randomDate(time2, time3);
                            String format = simpleDateFormat.format(date);
                            return format;
                        }else {
                            return time2;
                        }
                    }
                }
            }

        } else {
            if (i > 0) {
                String time2 = l.get(i - 1).getTime();
                String time3 = l.get(i + 1).getTime();
                if (!time2.equals(time3)){
                    Date date = CalculationMoney.randomDate(time2, time3);
                    String format = simpleDateFormat.format(date);
                    return format;
                }else {
                    return time2;
                }
            }
        }
        return null;
    }

    public static String getEndTime(String name, String getqTime, int i, Double aDouble) {
        String[] split = name.split("-");
        String[] sp = getqTime.split("-");
        String endTime = null;
        Integer in = Integer.valueOf(sp[2]);
        Integer mon = Integer.valueOf(sp[1]);
        Integer year = Integer.valueOf(sp[0]);
        Integer pt = 0;
        if (aDouble.equals(Double.valueOf("12.0"))) {
            pt = 12;
        } else if (aDouble.equals(Double.valueOf("24.0"))) {
            pt = 24;
        }
        if (split[0].equals("YXZ")) {
            i = i - pt;
        }
        if (in > 15) {
            in = 1;
            mon = mon + 1;
        }
        if (mon + i > 12) {
            mon = mon + i - 12;
            if (mon > 12) {
                mon = mon - 12;
                year = year + 1;
            }
            year = year + 1;
        } else {
            mon = mon + i;
        }
        endTime = year.toString() + "-" + mon.toString() + "-" + "01";
        return endTime;
    }

    public static Date fomartTime(TimeList timeList) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM
        Date date = null;
        try {
            date = simpleDateFormat.parse(timeList.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getOnTex(String money) {
        BigDecimal bigDecimal = new BigDecimal(money);
        BigDecimal multiply = bigDecimal.divide(new BigDecimal("1.06"), 6);
        DecimalFormat df = new DecimalFormat("0.00");
        String format = df.format(multiply);
        return format;
    }

    public static String getInTex(String money, String proportion) {
        if (proportion.contains("/")) {
            String[] split = proportion.split("/");
            Double mon = Double.valueOf(money);
            Double dou = Double.valueOf(split[1]);
            double v = mon / dou;
            DecimalFormat df = new DecimalFormat("0.00");
            String format = df.format(v);
            return format;
        } else {
            Double mon = Double.valueOf(money);
            Double dou = Double.valueOf(proportion);
            double v = mon * dou;
            DecimalFormat df = new DecimalFormat("0.00");
            String format = df.format(v);
            return format;
        }
    }

    public static String getRows(String name, Contract contract, String sName) {
        if (name.contains("印务")) {
            Double aDouble = Double.valueOf(contract.getPrinting());
            DecimalFormat df = new DecimalFormat("0.00");
            String format = df.format(aDouble);
            return format;
        }
        if (name.contains("底稿")) {
            Double aDouble = Double.valueOf(contract.getManuscript());
            DecimalFormat df = new DecimalFormat("0.00");
            String format = df.format(aDouble);
            return format;
        }
        if (name.contains("可研")) {
            Double aDouble = Double.valueOf(contract.getFeasibility());
            DecimalFormat df = new DecimalFormat("0.00");
            String format = df.format(aDouble);
            return format;
        }
        if (name.contains("信披")) {
            Double aDouble = Double.valueOf(contract.getLetterApproved());
            DecimalFormat df = new DecimalFormat("0.00");
            String format = df.format(aDouble);
            return format;
        }
        if (name.contains("财务数据核对")) {
            Double aDouble = Double.valueOf(contract.getFinance());
            DecimalFormat df = new DecimalFormat("0.00");
            String format = df.format(aDouble);
            return format;
        }
        if (name.contains("软件")) {
            if (sName.contains("ELS")){
                double v = Double.valueOf(contract.getEls());
                DecimalFormat df = new DecimalFormat("0.00");
                String format = df.format(v);
                return format;
            }else {
                double v = Double.valueOf(contract.getYxz());
                DecimalFormat df = new DecimalFormat("0.00");
                String format = df.format(v);
                return format;
            }
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
