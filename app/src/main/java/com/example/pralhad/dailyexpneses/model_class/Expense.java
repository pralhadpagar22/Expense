package com.example.pralhad.dailyexpneses.model_class;

import java.sql.Timestamp;

public class Expense {
    private int exId;
    private java.sql.Timestamp exDate;
    private String exParticular = "null";
    private int exAmount = 0;
    private int exBalance;
    private String exBill = "null";
    private int userId;
    private byte isActive;

    public int getExId() {
        return exId;
    }

    public void setExId(int exId) {
        this.exId = exId;
    }

    public Timestamp getExDate() {
        return exDate;
    }

    public void setExDate(Timestamp exDate) {
        this.exDate = exDate;
    }

    public String getExParticular() {
        return exParticular;
    }

    public void setExParticular(String exParticular) {
        this.exParticular = exParticular;
    }

    public int getExAmount() {
        return exAmount;
    }

    public void setExAmount(int exAmount) {
        this.exAmount = exAmount;
    }

    public int getExBalance() {
        return exBalance;
    }

    public void setExBalance(int exBalance) {
        this.exBalance = exBalance;
    }

    public String getExBill() {
        return exBill;
    }

    public void setExBill(String exBill) {
        this.exBill = exBill;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public byte getIsActive() {
        return isActive;
    }

    public void setIsActive(byte isActive) {
        this.isActive = isActive;
    }
}