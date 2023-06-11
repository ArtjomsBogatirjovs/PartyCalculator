package com.example.partycalculator.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.partycalculator.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatButton createParty = findViewById(R.id.create_party);
        AppCompatButton openParties = findViewById(R.id.list_party);
        AppCompatButton exit = findViewById(R.id.exit);
        createParty.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreatePartyActivity.class);
            startActivity(intent);
        });
        openParties.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PartyListActivity.class);
            startActivity(intent);
        });
        exit.setOnClickListener(v -> finish());
    }

    public void openLink(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/ArtjomsBogatirjovs"));
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}