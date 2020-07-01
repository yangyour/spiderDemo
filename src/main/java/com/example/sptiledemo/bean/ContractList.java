package com.example.sptiledemo.bean;

public class ContractList {
    private Integer rows;
    //合同编码
    private String code;
    //公司名称
    private String name;
    //来源
    private String source;
    //标的名称
    private String conName;
    //存货分类编码
    private String fCode;
    //对应存货编码
    private String dCode;
    //标的编码
    private String bCode;
    //数量
    private String count;
    //税率 6
    private String tax;
    //折扣率 100
    private String discount;
    //含税原币单价
    private String money;
    //含税原币金额  含税原币金额 = 含税原币单价 * 数量
    private String inTex;
    //无税原币金额 无税原币金额 = 含税原币金额 / 1.06
    private String noTex;
    private String status;
    private String time;
    //终止时间
    private String endTime;
    //比例
    private String proportion;
    //确认收入
    private String income;

    public String getProportion() {
        return proportion;
    }

    public void setProportion(String proportion) {
        this.proportion = proportion;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConName() {
        return conName;
    }

    public void setConName(String conName) {
        this.conName = conName;
    }


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getfCode() {
        return fCode;
    }

    public void setfCode(String fCode) {
        this.fCode = fCode;
    }

    public String getdCode() {
        return dCode;
    }

    public void setdCode(String dCode) {
        this.dCode = dCode;
    }

    public String getbCode() {
        return bCode;
    }

    public void setbCode(String bCode) {
        this.bCode = bCode;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getInTex() {
        return inTex;
    }

    public void setInTex(String inTex) {
        this.inTex = inTex;
    }

    public String getNoTex() {
        return noTex;
    }

    public void setNoTex(String noTex) {
        this.noTex = noTex;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
