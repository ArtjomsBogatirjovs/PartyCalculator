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
    private Item item;
    @ColumnInfo
    private Human human;
    @ColumnInfo
    private BigDecimal paid;

    public ItemConsumer() {
    }

    public ItemConsumer(long sysId, Item item, Human human, BigDecimal paid) {
        this.sysId = sysId;
        this.item = item;
        this.human = human;
        this.paid = paid;
    }

    public long getSysId() {
        return sysId;
    }

    public void setSysId(long sysId) {
        this.sysId = sysId;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Human getHuman() {
        return human;
    }

    public void setHuman(Human human) {
        this.human = human;
    }

    public BigDecimal getPaid() {
        return paid;
    }

    public void setPaid(BigDecimal paid) {
        this.paid = paid;
    }
}
