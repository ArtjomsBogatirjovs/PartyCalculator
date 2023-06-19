package com.example.partycalculator.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(indices = {@Index(value = {"name"}, unique = true)})
public class Party {
    @PrimaryKey(autoGenerate = true)
    private long sysId;

    @ColumnInfo
    private String name;

    @ColumnInfo
    private boolean deleted;

    public Party() {
    }

    public Party(String name, boolean deleted) {
        this.name = name;
        this.deleted = deleted;
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Party party = (Party) o;
        return sysId == party.sysId && deleted == party.deleted && name.equals(party.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sysId, name, deleted);
    }
}
