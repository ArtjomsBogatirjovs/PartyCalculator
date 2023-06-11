package com.example.partycalculator.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.partycalculator.R;
import com.example.partycalculator.entity.Party;

import java.util.ArrayList;
import java.util.List;

public class PartyAdapter extends RecyclerView.Adapter<PartyAdapter.PartyViewHolder> {
    private List<Party> partyList = new ArrayList<>();

    private OnRemoveButtonClickListener removeButtonClickListener;
    private OnItemClickListener itemClickListener;

    public void setPartyList(List<Party> partyList) {
        this.partyList = partyList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setRemoveButtonClickListener(OnRemoveButtonClickListener removeButtonClickListener) {
        this.removeButtonClickListener = removeButtonClickListener;
    }

    @NonNull
    @Override
    public PartyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_party, parent, false);
        return new PartyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PartyViewHolder holder, int position) {
        Party party = partyList.get(position);
        holder.bind(party);
    }

    @Override
    public int getItemCount() {
        return partyList.size();
    }

    public class PartyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView partyItemView;

        public PartyViewHolder(View itemView) {
            super(itemView);
            partyItemView = itemView.findViewById(R.id.text_view_name);
            ImageButton delete = itemView.findViewById(R.id.button_remove);
            itemView.setOnClickListener(this);
            delete.setOnClickListener(this);
        }

        public void bind(Party party) {
            partyItemView.setText(party.getName());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (v.getId() == R.id.button_remove) {
                if (position != RecyclerView.NO_POSITION && removeButtonClickListener != null) {
                    Party party = partyList.get(position);
                    removeButtonClickListener.onRemoveButtonClick(party);
                }
            } else {
                if (position != RecyclerView.NO_POSITION && itemClickListener != null) {
                    Party party = partyList.get(position);
                    itemClickListener.onItemClick(party);
                }
            }
        }
    }

    public interface OnRemoveButtonClickListener {
        void onRemoveButtonClick(Party party);
    }

    public interface OnItemClickListener {
        void onItemClick(Party party);
    }
}