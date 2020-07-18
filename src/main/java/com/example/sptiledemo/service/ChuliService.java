package com.example.sptiledemo.service;

import org.apache.poi.xssf.usermodel.*;

public class ChuliService {
    public static XSSFWorkbook createZeroExcel() {
        // 声明一个工作薄
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFCellStyle cellStyle = workbook.createCellStyle();
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

//        for (int i = 0; i < 31; i++) {
//            XSSFCell c11 = row2.createCell(1 + (3 * i));
//            c11.setCellValue("第" + (i + 1) + "次开票时间");
//            XSSFCell c12 = row2.createCell(2 + (3 * i));
//            c12.setCellValue("第" + (i + 1) + "次开票发票号码");
//            XSSFCell c13 = row2.createCell(3 + (3 * i));
//            c13.setCellValue("第" + (i + 1) + "次开票金额");
//        }

        return workbook;


    }
}
