package com.example.partycalculator.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.partycalculator.entity.Human;
import com.example.partycalculator.entity.Party;

import java.util.List;

@Dao
public interface HumanDao {
    @Delete
    void deleteHuman(Human human);

    @Insert
    void insertHuman(Human human);

    @Update
    void updateHuman(Human human);

    @Query("select * from Human where partySysId = :sysId")
    List<Human> getAllHumanInParty(long sysId);

    @Query("select * from Human where sysId = :sysId")
    Human getHumanBySysId(long sysId);
}
