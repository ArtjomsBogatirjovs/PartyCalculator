package com.example.partycalculator.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.partycalculator.entity.Item;

@Dao
public interface ItemDao {
    @Insert
    void insertItem(Item item);

    @Delete
    void deleteItem(Item item);

    @Query("select * from Item where sysId = :sysId")
    Item getItemBySysId(long sysId);
}
