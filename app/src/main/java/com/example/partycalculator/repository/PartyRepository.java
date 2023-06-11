package com.example.partycalculator.repository;

import android.app.Application;

import com.example.partycalculator.dao.PartyDao;
import com.example.partycalculator.db.AppDatabase;
import com.example.partycalculator.entity.Party;

import java.util.List;

public class PartyRepository {
    private PartyDao mPartyDao;
    private List<Party> mAllParties;

    public PartyRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mPartyDao = db.partyDao();
        mAllParties = mPartyDao.getAllParties();
    }

    public List<Party> getAllParties() {
        return mAllParties;
    }

    public void insert(Party party) {
        AppDatabase.databaseWriteExecutor.execute(() -> mPartyDao.insertParty(party));
    }
}
