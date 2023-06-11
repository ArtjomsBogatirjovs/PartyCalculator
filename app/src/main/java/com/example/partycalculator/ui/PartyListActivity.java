package com.example.partycalculator.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;


import com.example.partycalculator.R;
import com.example.partycalculator.dao.PartyDao;
import com.example.partycalculator.db.AppDatabase;
import com.example.partycalculator.entity.Party;

import java.util.List;

public class PartyListActivity extends AppCompatActivity implements PartyAdapter.OnItemClickListener, PartyAdapter.OnRemoveButtonClickListener {
    private PartyDao partyDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_list);

        AppDatabase partyDatabase = AppDatabase.getDatabase(getApplication());
        partyDao = partyDatabase.partyDao();
        final List<Party> partyList = partyDao.getAllParties();

        PartyAdapter partyAdapter = new PartyAdapter();
        partyAdapter.setOnItemClickListener(this);
        partyAdapter.setRemoveButtonClickListener(this);
        partyAdapter.setPartyList(partyList);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_parties);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(partyAdapter);
    }

    @Override
    public void onRemoveButtonClick(Party party) {
        partyDao.deleteParty(party);
        recreate();
    }

    @Override
    public void onItemClick(Party party) {
        Intent intent = new Intent(this, PartyActivity.class);
        intent.putExtra("party_sysId", party.getSysId());
        this.startActivity(intent);
    }
}