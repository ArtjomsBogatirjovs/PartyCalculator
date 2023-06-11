package com.example.partycalculator.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.math.BigDecimal;

@Entity
public class Item {
    @PrimaryKey(autoGenerate = true)
    private long sysId;
    @ColumnInfo
    private String name;
    @ColumnInfo
    private BigDecimal price;
    @ColumnInfo
    private int quantity;
    @ColumnInfo
    private BigDecimal fullPrice;

    public Item() {
    }

    public Item(long sysId, String name, BigDecimal price, int quantity, BigDecimal fullPrice) {
        this.sysId = sysId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.fullPrice = fullPrice;
    }

    public long getSysId() {
        return sysId;
    }

    public void setSysId(long sysId) {
        this.sysId = sysId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getFullPrice() {
        return fullPrice;
    }

    public void setFullPrice(BigDecimal fullPrice) {
        this.fullPrice = fullPrice;
    }
}
