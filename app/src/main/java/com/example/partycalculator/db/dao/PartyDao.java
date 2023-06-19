package com.example.partycalculator.db.dao;

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
    long insertParty(Party party);

    @Update
    void updateParty(Party party);

    @Query("select * from Party where name = :name")
    Party getByName(String name);

    @Query("SELECT * FROM Party")
    List<Party> getAllParties();
    @Query("SELECT * FROM Party where deleted = 0")
    List<Party> getAllActiveParties();
    @Query("SELECT * FROM Party where deleted = 1")
    List<Party> getAllDeletedParties();

    @Query("SELECT * FROM Party WHERE sysId = :partySysId")
    Party getPartyBySysId(long partySysId);
}
