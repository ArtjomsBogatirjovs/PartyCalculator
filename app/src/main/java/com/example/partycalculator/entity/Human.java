package com.example.partycalculator.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Human {
    @PrimaryKey(autoGenerate = true)
    private long sysId;
    @ColumnInfo
    private String name;
    @ColumnInfo
    private String familyName;
    @ColumnInfo
    private long partySysId;

    public Human() {
    }

    public Human(long sysId, String name, String familyName, long partySysId) {
        this.sysId = sysId;
        this.name = name;
        this.familyName = familyName;
        this.partySysId = partySysId;
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

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public long getPartySysId() {
        return partySysId;
    }

    public void setPartySysId(long partySysId) {
        this.partySysId = partySysId;
    }
}
