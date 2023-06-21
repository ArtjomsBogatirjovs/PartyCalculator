package com.example.partycalculator.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
import com.example.partycalculator.listeners.OnDeleteClickListener;
import com.example.partycalculator.listeners.OnEditClickListener;

import java.util.ArrayList;
import java.util.List;

public class ItemActivity extends PartyToolbarActivity implements AddItemDialog.AddItemDialogListener, OnDeleteClickListener, OnEditClickListener {
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

        itemAdapter = new ItemAdapter(itemList, this, this);
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
        itemList.add(itemDao.getItemBySysId(sysId));
        itemAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onItemEdited(Item item, List<ItemConsumer> list) {
        itemDao.updateItem(item);
        List<ItemConsumer> oldConsumers = itemConsumerDao.getAllItemsConsumersByItem(item.getSysId());
        for (ItemConsumer tempConsumer : oldConsumers) {
            itemConsumerDao.deleteItemConsumer(tempConsumer);
        }
        for (ItemConsumer tempConsumer : list) {
            tempConsumer.setItemSysId(item.getSysId());
            itemConsumerDao.insertItemConsumer(tempConsumer);
        }
        itemAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onCancel() {
        itemAdapter.notifyDataSetChanged();
    }

    private void showAddItemDialog(List<Human> humans) {
        AddItemDialog dialog = new AddItemDialog(humans);
        dialog.show(getSupportFragmentManager(), "AddItemDialog");
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onDeleteClick(int position) {
        Item item = itemList.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to delete " + item.getName() + " item?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            List<ItemConsumer> itemConsumerList = itemConsumerDao.getAllItemsConsumersByItem(itemList.get(position).getSysId());
            for (ItemConsumer consumer : itemConsumerList) {
                itemConsumerDao.deleteItemConsumer(consumer);
            }
            itemDao.deleteItem(itemList.get(position));
            itemList.remove(position);
            itemAdapter.notifyItemRemoved(position);
        });
        builder.setNegativeButton("No", (dialog, which) -> itemAdapter.notifyDataSetChanged());
        builder.setOnCancelListener(dialog -> onCancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onEditClick(int position) {
        List<Long> sysIds = new ArrayList<>();
        for (ItemConsumer consumer : itemConsumerDao.getAllItemsConsumersByItem(itemList.get(position).getSysId())) {
            sysIds.add(consumer.getHumanSysId());
        }
        AddItemDialog dialog = new AddItemDialog(humanDao.getAllHumanInParty(PartySingleton.getInstance().getParty().getSysId()), itemList.get(position), sysIds);
        dialog.show(getSupportFragmentManager(), "AddItemDialog");
    }
}