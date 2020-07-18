package com.example.sptiledemo.controller;


import com.example.sptiledemo.bean.RunningWater;
import com.example.sptiledemo.bean.Stock;
import com.example.sptiledemo.mapper.RunningWaterMapper;
import com.example.sptiledemo.mapper.StockMapper;
import com.example.sptiledemo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RunController {

    @Autowired
    private RunningWaterMapper runningWaterMapper;

    @Autowired
    private StockMapper stockMapper;

    @GetMapping("stock")
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

    @GetMapping("runWa")
    public void test333() {
        List<RunningWater> runningWaterExcel = QbService.getRunningWaterExcel();
        for (RunningWater runningWater : runningWaterExcel) {
            runningWaterMapper.save(runningWater);
        }
    }
}
