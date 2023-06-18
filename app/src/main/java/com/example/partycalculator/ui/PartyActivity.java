package com.example.partycalculator.ui;

import android.os.Bundle;

import com.example.partycalculator.R;
import com.example.partycalculator.entity.Party;
import com.example.partycalculator.entity.PartySingleton;

public class PartyActivity extends PartyToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party);

        //Party mainParty = AppDatabase.getDatabase(getApplication())
        //        .partyDao()
        //       .getByName(PartySingleton.getInstance().getParty().getName());
        Party mainParty = PartySingleton.getInstance().getParty();
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle(partyName);
    }
}