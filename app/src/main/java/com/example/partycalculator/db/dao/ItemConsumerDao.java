package com.example.partycalculator.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.partycalculator.entity.ItemConsumer;

import java.util.List;
@Dao
public interface ItemConsumerDao {
    @Insert
    long insertItemConsumer(ItemConsumer itemConsumer);

    @Delete
    void deleteItemConsumer(ItemConsumer itemConsumer);

    @Query("select * from ItemConsumer where sysId = :sysId")
    ItemConsumer getItemBySysId(long sysId);

    @Query("select * from ItemConsumer where itemSysId= :sysId")
    List<ItemConsumer> getAllItemsConsumersByItem(long sysId);

    @Query("select * from ItemConsumer where humanSysId= :sysId")
    List<ItemConsumer> getAllItemsConsumersByHuman(long sysId);
}
