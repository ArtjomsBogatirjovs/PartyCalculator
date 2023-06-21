package com.example.partycalculator.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.partycalculator.entity.Item;

import java.util.List;

@Dao
public interface ItemDao {
    @Insert
    long insertItem(Item item);

    @Delete
    void deleteItem(Item item);
    @Update
    void updateItem(Item item);

    @Query("select * from Item where sysId = :sysId")
    Item getItemBySysId(long sysId);

    @Query("select * from Item where partySysId = :sysId")
    List<Item> getAllItemsInParty(long sysId);
}
