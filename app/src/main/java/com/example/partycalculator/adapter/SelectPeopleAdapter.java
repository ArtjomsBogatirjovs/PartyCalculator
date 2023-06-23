package com.example.partycalculator.adapter;

import android.annotation.SuppressLint;
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
import com.example.partycalculator.entity.ItemConsumer;
import com.example.partycalculator.filter.DecimalDigitsInputFilter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectPeopleAdapter extends RecyclerView.Adapter<SelectPeopleAdapter.ViewHolder> {
    private final List<Human> peopleList;
    private final List<Integer> selectedPositions;
    private final List<Integer> itemOwner;
    private final Map<Integer, BigDecimal> selectedPositionsPrice;
    private BigDecimal fullPrice;

    public void setFullPrice(BigDecimal fullPrice) {
        this.fullPrice = fullPrice;
    }

    public SelectPeopleAdapter(List<Human> peopleList, BigDecimal fullPrice) {
        this.peopleList = peopleList;
        this.fullPrice = fullPrice;
        this.selectedPositions = new ArrayList<>();
        this.selectedPositionsPrice = new HashMap<>();
        this.itemOwner = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_person, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Human person = peopleList.get(position);
        if (selectedPositionsPrice.get(position) != null) {
            holder.editTextPricePaid.setText(selectedPositionsPrice.get(position).toString());
        }
        holder.bind(person, isSelected(position), isOwner(position));
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

    private boolean isOwner(int position) {
        return itemOwner.contains(position);
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

    public void setOwner(int pos) {
        if (itemOwner.isEmpty()) {
            itemOwner.add(pos);
            notifyItemChanged(pos);
        }
    }

    public void selectBySysId(List<Long> sysIds) {
        for (int i = 0; i < peopleList.size(); i++) {
            if (sysIds.contains(peopleList.get(i).getSysId())) {
                selectPerson(i);
            }
        }
    }

    public void selectOwnerBySysId(long sysId) {
        for (int i = 0; i < peopleList.size(); i++) {
            if (sysId == peopleList.get(i).getSysId()) {
                setOwner(i);
            }
        }
    }

    public void setPaid(List<ItemConsumer> consumers) {
        for (ItemConsumer consumer : consumers) {
            for (int i = 0; i < peopleList.size(); i++) {
                if (consumer.getHumanSysId() == peopleList.get(i).getSysId()) {
                    selectedPositionsPrice.put(i, consumer.getPaid());
                    notifyItemChanged(i);
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void deselectAllPeople() {
        int selectedSize = selectedPositions.size();
        selectedPositions.clear();
        itemOwner.clear();
        notifyDataSetChanged();
    }

    public List<Integer> getItemOwner() {
        return itemOwner;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final CheckBox checkBoxPerson;
        private final CheckBox checkBoxOwner;
        private final TextView textViewPersonName;
        private final EditText editTextPricePaid;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBoxPerson = itemView.findViewById(R.id.checkBoxPerson);
            textViewPersonName = itemView.findViewById(R.id.textViewPersonName);
            editTextPricePaid = itemView.findViewById(R.id.editTextPricePaid);
            checkBoxOwner = itemView.findViewById(R.id.checkBoxPersonOwner);
        }

        @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
        public void bind(Human person, boolean isSelected, boolean isOwner) {
            textViewPersonName.setText(person.getName());
            if (!itemOwner.isEmpty() && !isOwner) {
                checkBoxOwner.setEnabled(false);
                editTextPricePaid.setEnabled(true);
            }
            if (itemOwner.isEmpty() || isOwner) {
                checkBoxOwner.setEnabled(true);
            }
            if (isOwner) {
                editTextPricePaid.setEnabled(false);
                if (fullPrice != null) {
                    editTextPricePaid.setText(fullPrice.toString());
                }
            }
            if (!isSelected) {
                editTextPricePaid.setEnabled(false);
                checkBoxOwner.setEnabled(false);
            }
            if(isSelected && itemOwner.isEmpty()){
                checkBoxOwner.setEnabled(true);
                editTextPricePaid.setEnabled(true);
            }

            if (selectedPositionsPrice.get(getBindingAdapterPosition()) != null && !selectedPositionsPrice.get(getBindingAdapterPosition()).toString().isEmpty()) {
                editTextPricePaid.setText(selectedPositionsPrice.get(getBindingAdapterPosition()).toString());
            }
            checkBoxOwner.setOnCheckedChangeListener(null);
            checkBoxOwner.setChecked(isOwner);
            checkBoxOwner.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    //itemOwner.add(getBindingAdapterPosition());
                    setOwner(getBindingAdapterPosition());
                } else {
                    itemOwner.remove(Integer.valueOf(getBindingAdapterPosition()));
                    editTextPricePaid.setEnabled(true);
                }
                itemView.post(SelectPeopleAdapter.this::notifyDataSetChanged);
            });
            checkBoxPerson.setOnCheckedChangeListener(null);
            checkBoxPerson.setChecked(isSelected);
            checkBoxPerson.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    editTextPricePaid.setEnabled(true);
                    //checkBoxOwner.setEnabled(true);
                    //selectedPositions.add(getBindingAdapterPosition());
                    selectPerson(getBindingAdapterPosition());
                } else {
                    editTextPricePaid.setEnabled(false);
                    selectedPositions.remove(Integer.valueOf(getBindingAdapterPosition()));
                    itemOwner.remove(Integer.valueOf(getBindingAdapterPosition()));
                }
                itemView.post(() -> notifyItemChanged(getBindingAdapterPosition()));
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

                        String twoPoints = ".*\\..*\\..*";
                        String twoNumbersAfterPoint = "\\d*\\.\\d{0,2}$";

                        Pattern pattern = Pattern.compile(twoPoints);
                        Matcher matcher = pattern.matcher(pricePaidStr);
                        boolean isTwoPoints = matcher.matches();

                        pattern = Pattern.compile(twoNumbersAfterPoint);
                        matcher = pattern.matcher(pricePaidStr);
                        boolean isMaxTwoNumbers = matcher.matches();

                        boolean isPoint = pricePaidStr.contains(".");

                        if (isTwoPoints ||
                                (isPoint && !isMaxTwoNumbers)) {
                            pricePaidStr = pricePaidStr.substring(0, pricePaidStr.length() - 1);
                            editTextPricePaid.setText(pricePaidStr);
                            editTextPricePaid.setSelection(pricePaidStr.length());
                        }

                        if (!TextUtils.isEmpty(pricePaidStr)) {
                            BigDecimal pricePaid = new BigDecimal(pricePaidStr);
                            selectedPositionsPrice.put(getBindingAdapterPosition(), pricePaid);
                        }
                    }
                }
            });
        }
    }
}

