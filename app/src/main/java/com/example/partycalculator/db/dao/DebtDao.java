package com.example.partycalculator.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.partycalculator.entity.Debt;

import java.util.List;

@Dao
public interface DebtDao {
    @Insert
    long insertDebt(Debt debt);

    @Update
    void updateDebt(Debt debt);

    @Delete
    void deleteDebt(Debt debt);

    @Query("select * from Debt where partySysId = :sysId")
    List<Debt> getAllDebt(long sysId);

    @Query("select * from Debt where debtorSysId = :deptor")
    List<Debt> getAllDebtByDebtor(long deptor);

    @Query("select * from Debt where debtorSysId = :deptor and creditorSysId = :creditor")
    List<Debt> getAllDebtByDebtorAndCreditor(long deptor, long creditor);

    @Query("select * from Debt where sysId = :sysId")
    List<Debt> getDebt(long sysId);
}
