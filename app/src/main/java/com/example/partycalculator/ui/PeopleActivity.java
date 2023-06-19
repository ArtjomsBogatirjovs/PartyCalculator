package com.example.partycalculator.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.partycalculator.R;
import com.example.partycalculator.adapter.PeopleAdapter;
import com.example.partycalculator.db.dao.HumanDao;
import com.example.partycalculator.db.AppDatabase;
import com.example.partycalculator.entity.Human;
import com.example.partycalculator.entity.PartySingleton;

import java.util.List;

public class PeopleActivity extends PartyToolbarActivity {

    private HumanDao humanDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);

        AppDatabase partyDatabase = AppDatabase.getDatabase(getApplication());
        humanDao = partyDatabase.humanDao();
        final List<Human> peopleList = humanDao.getAllHumanInParty(PartySingleton.getInstance().getParty().getSysId());

        PeopleAdapter selectedPeopleAdapter = new PeopleAdapter(peopleList);

        RecyclerView recyclerViewPeople = findViewById(R.id.recyclerViewPeople);
        recyclerViewPeople.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPeople.setAdapter(selectedPeopleAdapter);

        Button btnAddPerson = findViewById(R.id.btnAddPerson);
        btnAddPerson.setOnClickListener(v -> {
            showCreatePersonDialog();
        });

        selectedPeopleAdapter.setOnEditClickListener(position -> {
            // Handle edit button click for a specific person
            // Implement the logic to edit the person's parameters
            Human person = peopleList.get(position);
            // Open an edit dialog/fragment or start an edit activity passing the person data
        });

        selectedPeopleAdapter.setOnDeleteClickListener(position -> {
            humanDao.deleteHuman(peopleList.get(position));
            recreate();
            //peopleList.remove(position);
            //peopleAdapter.notifyItemRemoved(position);
        });

    }

    private void showCreatePersonDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create Person");

        View view = getLayoutInflater().inflate(R.layout.dialog_create_person, null);
        builder.setView(view);

        builder.setPositiveButton("Create", (dialog, which) -> {
            EditText name = view.findViewById(R.id.edit_text_name);
            EditText surname = view.findViewById(R.id.edit_text_familyName);
            if (name != null && !name.getText().toString().isEmpty()) {
                Human human = new Human();
                human.setName(name.getText().toString());
                human.setFamilyName(surname.getText().toString());
                human.setPartySysId(PartySingleton.getInstance().getParty().getSysId());
                try {
                    AppDatabase.getDatabase(this).humanDao().insertHuman(human);
                    dialog.dismiss();
                    recreate();
                } catch (Exception e) {
                    Toast.makeText(this, "Fail creating person", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Name can't be empty!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}