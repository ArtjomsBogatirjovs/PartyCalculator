package com.example.partycalculator.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.partycalculator.R;
import com.example.partycalculator.db.AppDatabase;
import com.example.partycalculator.db.dao.PartyDao;
import com.example.partycalculator.entity.Party;
import com.example.partycalculator.entity.PartySingleton;
import com.example.partycalculator.utils.Functions;

public class CreatePartyActivity extends AppCompatActivity {
    private long partySysId;
    private PartyDao partyDao;
    public static int MAX_NAME_LENGTH = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_party);
        Button btnCreate = findViewById(R.id.create_button);
        EditText partyName = findViewById(R.id.party_name);
        btnCreate.setOnClickListener(v -> {
            String tempName = partyName.getText().toString();
            if (!validate(tempName)) return;
            CreatePartyTask createPartyTask = new CreatePartyTask(tempName);
            createPartyTask.execute();
        });
    }

    private boolean validate(String name) {
        AppDatabase databaseClient = AppDatabase.getDatabase(getApplicationContext());
        if (Functions.isEmpty(name)) {
            Toast.makeText(this, R.string.party_cannot_empty, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (name.length() > MAX_NAME_LENGTH) {
            Toast.makeText(this, R.string.party_name_length, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (databaseClient.partyDao().getByName(name) != null) {
            Toast.makeText(CreatePartyActivity.this, "Party with this name already exist!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    class CreatePartyTask extends AsyncTask<Void, Void, Void> {

        private final String partyName;
        private Party party;
        private boolean isOkay = true;

        public CreatePartyTask(String partyName) {
            this.partyName = partyName;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            AppDatabase databaseClient = AppDatabase.getDatabase(getApplicationContext());
            partyDao = databaseClient.partyDao();

            party = new Party();
            party.setName(partyName);
            party.setDeleted(false);

            try {
                partySysId = partyDao.insertParty(party);
            } catch (Exception e) {
                Toast.makeText(CreatePartyActivity.this, "Fail creating party", Toast.LENGTH_SHORT).show();
                isOkay = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (isOkay) {
                Toast.makeText(CreatePartyActivity.this, "Party Created!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CreatePartyActivity.this, PartyActivity.class);
                finish();
                PartySingleton.getInstance().setParty(partyDao.getPartyBySysId(partySysId));
                startActivity(intent);
            }
        }
    }
}