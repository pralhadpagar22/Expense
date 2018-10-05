package com.example.pralhad.dailyexpneses.pojoClass;

import java.sql.Timestamp;

public class Transaction {

    private int trId;
    private int trType;
    private String trPerson;
    private java.sql.Timestamp trDate;
    private int trAmount;

    public int getTrId() {
        return trId;
    }

    public void setTrId(int trId) {
        this.trId = trId;
    }

    public int getTrType() {
        return trType;
    }

    public void setTrType(int trType) {
        this.trType = trType;
    }

    public String getTrPerson() {
        return trPerson;
    }

    public void setTrPerson(String trPerson) {
        this.trPerson = trPerson;
    }

    public Timestamp getTrDate() {
        return trDate;
    }

    public void setTrDate(Timestamp trDate) {
        this.trDate = trDate;
    }

    public int getTrAmount() {
        return trAmount;
    }

    public void setTrAmount(int trAmount) {
        this.trAmount = trAmount;
    }
}
