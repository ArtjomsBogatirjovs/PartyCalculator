package com.example.partycalculator.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;


import com.example.partycalculator.R;
import com.example.partycalculator.adapter.PartyAdapter;
import com.example.partycalculator.db.dao.PartyDao;
import com.example.partycalculator.db.AppDatabase;
import com.example.partycalculator.entity.Party;
import com.example.partycalculator.entity.PartySingleton;
import com.example.partycalculator.listeners.OnItemClickListener;
import com.example.partycalculator.listeners.OnRemoveButtonClickListener;

import java.util.List;

public class PartyListActivity extends AppCompatActivity implements OnItemClickListener, OnRemoveButtonClickListener {
    private PartyDao partyDao;
    private PartyAdapter partyAdapter;
    private List<Party> allParties;
    private List<Party> deletedParties;
    private List<Party> activeParties;
    private boolean isAllParties = false;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_list);

        AppDatabase partyDatabase = AppDatabase.getDatabase(getApplication());
        partyDao = partyDatabase.partyDao();

        updatePartyLists();

        partyAdapter = new PartyAdapter();
        partyAdapter.setOnItemClickListener(this);
        partyAdapter.setRemoveButtonClickListener(this);
        partyAdapter.setPartyList(activeParties);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_parties);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(partyAdapter);

        Button btnShowAllParties = findViewById(R.id.btnShowAllParties);
        Button btnShowDeletedParties = findViewById(R.id.btnShowDeletedParties);
        Button btnShowActiveParties = findViewById(R.id.btnShowActiveParties);

        btnShowAllParties.setOnClickListener(v -> {
            partyAdapter.setPartyList(allParties);
            partyAdapter.notifyDataSetChanged();
            isAllParties = true;
        });

        btnShowDeletedParties.setOnClickListener(v -> {
            partyAdapter.setPartyList(deletedParties);
            partyAdapter.notifyDataSetChanged();
            isAllParties = false;
        });

        btnShowActiveParties.setOnClickListener(v -> {
            partyAdapter.setPartyList(activeParties);
            partyAdapter.notifyDataSetChanged();
            isAllParties = false;
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onRemoveButtonClick(Party party) {
        if (!party.isDeleted()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure you want to delete " + party.getName() + " party?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                party.setDeleted(true);
                partyDao.updateParty(party);
                updatePartyLists();
                if (!isAllParties) {
                    partyAdapter.setPartyList(activeParties);
                } else {
                    partyAdapter.setPartyList(allParties);
                }
                partyAdapter.notifyDataSetChanged();
            });
            builder.setNegativeButton("No", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onItemClick(Party party) {
        if (party.isDeleted()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Restore Party");
            builder.setMessage("Do you want to restore the party?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                party.setDeleted(false);
                partyDao.updateParty(party);
                updatePartyLists();
                if (!isAllParties) {
                    partyAdapter.setPartyList(deletedParties);
                } else {
                    partyAdapter.setPartyList(allParties);
                }
                partyAdapter.notifyDataSetChanged();
            });
            builder.setNegativeButton("No", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            Intent intent = new Intent(this, PartyActivity.class);
            finish();
            PartySingleton.getInstance().setParty(party);
            this.startActivity(intent);
        }
    }

    private void updatePartyLists() {
        allParties = partyDao.getAllParties();
        deletedParties = partyDao.getAllDeletedParties();
        activeParties = partyDao.getAllActiveParties();
    }
}