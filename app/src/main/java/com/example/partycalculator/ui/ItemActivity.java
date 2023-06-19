package com.example.partycalculator.ui;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.partycalculator.R;
import com.example.partycalculator.adapter.ItemAdapter;
import com.example.partycalculator.db.dao.HumanDao;
import com.example.partycalculator.db.dao.ItemConsumerDao;
import com.example.partycalculator.db.dao.ItemDao;
import com.example.partycalculator.db.AppDatabase;
import com.example.partycalculator.entity.Human;
import com.example.partycalculator.entity.Item;
import com.example.partycalculator.entity.ItemConsumer;
import com.example.partycalculator.entity.PartySingleton;
import com.example.partycalculator.fragment.AddItemDialog;

import java.util.List;

public class ItemActivity extends PartyToolbarActivity implements AddItemDialog.AddItemDialogListener {
    private ItemDao itemDao;
    private HumanDao humanDao;
    private ItemConsumerDao itemConsumerDao;
    private List<Item> itemList;
    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        AppDatabase partyDatabase = AppDatabase.getDatabase(getApplication());
        itemDao = partyDatabase.itemDao();
        humanDao = partyDatabase.humanDao();
        itemConsumerDao = partyDatabase.itemConsumerDao();
        itemList = itemDao.getAllItemsInParty(PartySingleton.getInstance().getParty().getSysId());

        RecyclerView recyclerView = findViewById(R.id.recycler_view_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemAdapter = new ItemAdapter(itemList);
        recyclerView.setAdapter(itemAdapter);

        Button btnAddItem = findViewById(R.id.btnAddItem);
        btnAddItem.setOnClickListener(v -> showAddItemDialog(humanDao.getAllHumanInParty(PartySingleton.getInstance().getParty().getSysId())));
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onItemAdded(Item item, List<ItemConsumer> list) {
        long sysId = itemDao.insertItem(item);
        for (ItemConsumer tempConsumer : list) {
            tempConsumer.setItemSysId(sysId);
            itemConsumerDao.insertItemConsumer(tempConsumer);
        }
        itemList.add(item);
        itemAdapter.notifyDataSetChanged();
    }

    private void showAddItemDialog(List<Human> humans) {
        AddItemDialog dialog = new AddItemDialog(humans);
        dialog.show(getSupportFragmentManager(), "AddItemDialog");
    }
}