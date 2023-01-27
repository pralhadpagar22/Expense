package com.example.pralhad.dailyexpneses.model_class;

import java.sql.Timestamp;

public class
Transaction {
    public static final byte FILTER_BY_ALL = 1;
    public static final byte FILTER_BY_INCOME = 2;
    public static final byte FILTER_BY_GIVE = 3;
    public static final byte FILTER_BY_GIVE_ON_RETURN_POL = 4;
    public static final byte FILTER_BY_PAID = 5;
    public static final byte TR_TYPE_CREDITED = 0;
    public static final byte TR_TYPE_DEBITED = 1;
    public static final byte TR_TYPE_INCOME = 1;
    public static final byte TR_TYPE_GIVE = 1;

    private int trId;
    private int trType;
    private int trAmount;
    private java.sql.Timestamp trDate;

    private byte isActive;

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

    public byte getIsActive() {
        return isActive;
    }

    public void setIsActive(byte isActive) {
        this.isActive = isActive;
    }
}
