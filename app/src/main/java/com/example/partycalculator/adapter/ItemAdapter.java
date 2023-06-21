package com.example.partycalculator.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.partycalculator.R;
import com.example.partycalculator.db.AppDatabase;
import com.example.partycalculator.db.dao.ItemConsumerDao;
import com.example.partycalculator.entity.Item;
import com.example.partycalculator.entity.ItemConsumer;
import com.example.partycalculator.listeners.OnDeleteClickListener;
import com.example.partycalculator.listeners.OnEditClickListener;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private final List<Item>
            itemList;
    private ItemTouchHelper itemTouchHelper;
    private final OnEditClickListener onEditClickListener;
    private final OnDeleteClickListener onDeleteClickListener;

    public ItemAdapter(List<Item> itemList, OnEditClickListener onEditClickListener, OnDeleteClickListener onDeleteClickListener) {
        this.itemList = itemList;
        this.onEditClickListener = onEditClickListener;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {
                    onDeleteClickListener.onDeleteClick(position);
                } else if (direction == ItemTouchHelper.RIGHT) {
                    onEditClickListener.onEditClick(position);
                }
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_item, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.bind(item);

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewName;
        private final TextView textViewQuantity;
        private final TextView textViewPrice;
        private final TextView textViewNumberOfPeople;
        private final TextView textViewFullPrice;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewQuantity = itemView.findViewById(R.id.textViewQuantity);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            textViewFullPrice = itemView.findViewById(R.id.textViewlFullPrice);
            textViewNumberOfPeople = itemView.findViewById(R.id.textViewNumberOfPeople);
        }

        public void bind(Item item) {
            textViewName.setText(item.getName());
            textViewQuantity.setText(String.valueOf(item.getQuantity()));
            textViewPrice.setText(String.valueOf(item.getPrice()));
            textViewFullPrice.setText(String.valueOf(item.getFullPrice()));
            AppDatabase databaseClient = AppDatabase.getDatabase(itemView.getContext());
            ItemConsumerDao itemConsumerDao = databaseClient.itemConsumerDao();
            List<ItemConsumer> consumers = itemConsumerDao.getAllItemsConsumersByItem(item.getSysId());
            if (!consumers.isEmpty()) {
                textViewNumberOfPeople.setText(String.valueOf(consumers.size()));
            } else {
                textViewNumberOfPeople.setText("0");
            }
        }
    }
}
