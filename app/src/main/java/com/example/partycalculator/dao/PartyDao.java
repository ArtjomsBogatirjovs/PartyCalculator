package com.example.partycalculator.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.partycalculator.entity.Party;

import java.util.List;

@Dao
public interface PartyDao {
    @Delete
    void deleteParty(Party party);

    @Insert
    void insertParty(Party party);

    @Update
    void updateParty(Party party);

    @Query("select * from Party where name = :name")
    List<Party> getByName(String name);

    @Query("SELECT * FROM Party")
    List<Party> getAllParties();
}
