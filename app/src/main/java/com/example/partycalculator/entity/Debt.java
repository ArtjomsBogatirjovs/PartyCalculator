package com.example.partycalculator.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.math.BigDecimal;

@Entity
public class Debt {
    @PrimaryKey(autoGenerate = true)
    private Long sysId;
    @ColumnInfo
    private long debtorSysId;
    @ColumnInfo
    private Long creditorSysId;
    @ColumnInfo
    private long partySysId;
    @ColumnInfo
    private BigDecimal debt;

    public Debt() {
    }

    public Debt(Long sysId, long debtorSysId, Long creditorSysId, long partySysId, BigDecimal debt) {
        this.sysId = sysId;
        this.debtorSysId = debtorSysId;
        this.creditorSysId = creditorSysId;
        this.partySysId = partySysId;
        this.debt = debt;
    }

    public Long getSysId() {
        return sysId;
    }

    public void setSysId(Long sysId) {
        this.sysId = sysId;
    }

    public long getDebtorSysId() {
        return debtorSysId;
    }

    public void setDebtorSysId(long debtorSysId) {
        this.debtorSysId = debtorSysId;
    }

    public Long getCreditorSysId() {
        return creditorSysId;
    }

    public void setCreditorSysId(Long creditorSysId) {
        this.creditorSysId = creditorSysId;
    }

    public long getPartySysId() {
        return partySysId;
    }

    public void setPartySysId(long partySysId) {
        this.partySysId = partySysId;
    }

    public BigDecimal getDebt() {
        return debt;
    }

    public void setDebt(BigDecimal debt) {
        this.debt = debt;
    }
}
