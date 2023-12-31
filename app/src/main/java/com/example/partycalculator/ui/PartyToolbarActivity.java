package com.example.partycalculator.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.example.partycalculator.R;
import com.example.partycalculator.entity.PartySingleton;

public class PartyToolbarActivity extends AppCompatActivity {
    protected Toolbar toolbar;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setupToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.party_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.action_option1:
                if(this instanceof PeopleActivity){
                    return true;
                }
                finish();
                intent = new Intent(this, PeopleActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.action_option2:
                if(this instanceof ItemActivity){
                    return true;
                }
                finish();
                intent = new Intent(this, ItemActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.action_option3:
                if(this instanceof DebtActivity){
                    return true;
                }
                finish();
                intent = new Intent(this, DebtActivity.class);
                this.startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(PartySingleton.getInstance().getParty() != null && getSupportActionBar() != null) {
            getSupportActionBar().setTitle(PartySingleton.getInstance().getParty().getName());
        }
    }

    protected void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

}