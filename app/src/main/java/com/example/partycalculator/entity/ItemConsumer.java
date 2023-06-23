package com.example.partycalculator.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.math.BigDecimal;

@Entity
public class ItemConsumer {
    @PrimaryKey(autoGenerate = true)
    private long sysId;
    @ColumnInfo
    private long itemSysId;
    @ColumnInfo
    private long humanSysId;
    @ColumnInfo
    private BigDecimal paid;
    @ColumnInfo
    private BigDecimal toPay;

    public ItemConsumer() {
    }

    public ItemConsumer(long sysId, long itemSysId, long humanSysId, BigDecimal paid, BigDecimal toPay) {
        this.sysId = sysId;
        this.itemSysId = itemSysId;
        this.humanSysId = humanSysId;
        this.paid = paid;
        this.toPay = toPay;
    }

    public long getSysId() {
        return sysId;
    }

    public void setSysId(long sysId) {
        this.sysId = sysId;
    }

    public BigDecimal getPaid() {
        return paid;
    }

    public void setPaid(BigDecimal paid) {
        this.paid = paid;
    }

    public long getItemSysId() {
        return itemSysId;
    }

    public void setItemSysId(long itemSysId) {
        this.itemSysId = itemSysId;
    }

    public long getHumanSysId() {
        return humanSysId;
    }

    public void setHumanSysId(long humanSysId) {
        this.humanSysId = humanSysId;
    }

    public BigDecimal getToPay() {
        return toPay;
    }

    public void setToPay(BigDecimal toPay) {
        this.toPay = toPay;
    }
}
