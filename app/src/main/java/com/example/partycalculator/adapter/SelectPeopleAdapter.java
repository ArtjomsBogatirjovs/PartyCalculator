package com.example.partycalculator.adapter;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.partycalculator.R;
import com.example.partycalculator.entity.Human;
import com.example.partycalculator.filter.DecimalDigitsInputFilter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectPeopleAdapter extends RecyclerView.Adapter<SelectPeopleAdapter.ViewHolder> {
    private final List<Human> peopleList;
    private final List<Integer> selectedPositions;
    private final Map<Integer, BigDecimal> selectedPositionsPrice;


    public SelectPeopleAdapter(List<Human> peopleList) {
        this.peopleList = peopleList;
        this.selectedPositions = new ArrayList<>();
        this.selectedPositionsPrice = new HashMap<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_person, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Human person = peopleList.get(position);
        holder.bind(person, isSelected(position));
    }

    @Override
    public int getItemCount() {
        return peopleList.size();
    }

    public List<Integer> getSelectedPositions() {
        return selectedPositions;
    }

    private boolean isSelected(int position) {
        return selectedPositions.contains(position);
    }

    public Map<Integer, BigDecimal> getSelectedPositionsPrice() {
        return selectedPositionsPrice;
    }

    public void selectPerson(int position) {
        if (!isSelected(position)) {
            selectedPositions.add(position);
            notifyItemChanged(position);
        }
    }

    public void selectBySysId(List<Long> sysIds) {
        for (int i = 0; i < peopleList.size(); i++) {
            if (sysIds.contains(peopleList.get(i).getSysId())) {
                selectPerson(i);
            }
        }
    }

    public void deselectAllPeople() {
        int selectedSize = selectedPositions.size();
        selectedPositions.clear();
        notifyItemRangeChanged(0, selectedSize);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final CheckBox checkBoxPerson;
        private final TextView textViewPersonName;
        private final EditText editTextPricePaid;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBoxPerson = itemView.findViewById(R.id.checkBoxPerson);
            textViewPersonName = itemView.findViewById(R.id.textViewPersonName);
            editTextPricePaid = itemView.findViewById(R.id.editTextPricePaid);
        }

        public void bind(Human person, boolean isSelected) {
            textViewPersonName.setText(person.getName());
            checkBoxPerson.setChecked(isSelected);

            checkBoxPerson.setOnCheckedChangeListener(null);

            checkBoxPerson.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedPositions.add(getBindingAdapterPosition());
                } else {
                    selectedPositions.remove(Integer.valueOf(getBindingAdapterPosition()));
                }
            });
            editTextPricePaid.setFilters(new InputFilter[]{new DecimalDigitsInputFilter()});
            editTextPricePaid.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s != null) {
                        String pricePaidStr = s.toString().trim();
                        if (!TextUtils.isEmpty(pricePaidStr)) {
                            BigDecimal pricePaid = new BigDecimal(pricePaidStr);
                            selectedPositionsPrice.put(getAdapterPosition(), pricePaid);
                        }
                    }
                }
            });
        }
    }
}

