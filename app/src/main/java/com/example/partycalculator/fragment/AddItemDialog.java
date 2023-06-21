package com.example.partycalculator.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.partycalculator.R;
import com.example.partycalculator.adapter.SelectPeopleAdapter;
import com.example.partycalculator.entity.Human;
import com.example.partycalculator.entity.Item;
import com.example.partycalculator.entity.ItemConsumer;
import com.example.partycalculator.entity.PartySingleton;
import com.example.partycalculator.filter.DecimalDigitsInputFilter;
import com.example.partycalculator.filter.DigitsInputFilter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddItemDialog extends AppCompatDialogFragment {
    private EditText editTextName;
    private EditText editTextPrice;
    private EditText editTextQuantity;
    private AddItemDialogListener listener;
    private final List<Human> peopleList;
    private Item editItem;
    private List<Long> humanSysIds;

    public AddItemDialog(List<Human> peopleList) {
        this.peopleList = peopleList;
    }

    public AddItemDialog(List<Human> peopleList, Item item, List<Long> humanSysIds) {
        this.peopleList = peopleList;
        this.editItem = item;
        this.humanSysIds = humanSysIds;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder;
        if (editItem == null) {
            builder = getBuilderCreate();
        } else {
            builder = getBuilderEdit(editItem);
        }
        return builder.create();
    }

    @NonNull
    private AlertDialog.Builder getBuilderCreate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_item, null);

        editTextName = view.findViewById(R.id.editTextName);
        editTextPrice = view.findViewById(R.id.editTextPrice);
        editTextQuantity = view.findViewById(R.id.editTextQuantity);

        editTextPrice.setFilters(new InputFilter[]{new DecimalDigitsInputFilter()});
        editTextQuantity.setFilters(new InputFilter[]{new DigitsInputFilter()});

        RecyclerView recyclerViewPeople = view.findViewById(R.id.recyclerViewPeople);
        SelectPeopleAdapter adapter = new SelectPeopleAdapter(peopleList);
        recyclerViewPeople.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewPeople.setAdapter(adapter);

        Button buttonSelectAll = view.findViewById(R.id.buttonSelectAll);
        Button buttonDeselectAll = view.findViewById(R.id.buttonDeselectAll);

        buttonSelectAll.setOnClickListener(v -> {
            for (int i = 0; i < adapter.getItemCount(); i++) {
                adapter.selectPerson(i);
            }
        });
        buttonDeselectAll.setOnClickListener(v -> adapter.deselectAllPeople());

        builder.setView(view)
                .setTitle("Add Item")
                .setNegativeButton("Cancel", (dialog, which) -> {
                    listener.onCancel();
                })
                .setPositiveButton("Add", (dialog, which) -> {
                    if (editTextName.getText().toString().isEmpty()
                            || editTextPrice.getText().toString().isEmpty()
                    ) {
                        Toast.makeText(getContext(), "Fields 'Name', 'Price' should be filled!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String name = editTextName.getText().toString().trim();
                    BigDecimal price = new BigDecimal(editTextPrice.getText().toString().trim());
                    int quantity = 1;
                    if (!editTextQuantity.getText().toString().isEmpty()) {
                        quantity = Integer.parseInt(editTextQuantity.getText().toString().trim());
                    }

                    Item newItem = new Item();
                    newItem.setName(name);
                    newItem.setPrice(price);
                    newItem.setQuantity(quantity);
                    newItem.setFullPrice(price.multiply(BigDecimal.valueOf(quantity)));
                    newItem.setPartySysId(PartySingleton.getInstance().getParty().getSysId());

                    List<Integer> positions = adapter.getSelectedPositions();
                    Map<Integer, BigDecimal> prices = adapter.getSelectedPositionsPrice();
                    List<ItemConsumer> result = new ArrayList<>();
                    BigDecimal allPaid = BigDecimal.ZERO;
                    for (int pos : positions) {
                        allPaid = allPaid.add(prices.get(pos));
                        Human tempHuman = peopleList.get(pos);
                        BigDecimal pay = prices.get(pos);
                        ItemConsumer itemConsumer = new ItemConsumer();
                        itemConsumer.setPaid(pay);
                        itemConsumer.setHumanSysId(tempHuman.getSysId());
                        result.add(itemConsumer);
                    }
                    if (allPaid.compareTo(editItem.getFullPrice()) > 0) {
                        listener.onCancel();
                        Toast.makeText(getContext(), "Total pays can't be bigger than full price!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    listener.onItemAdded(newItem, result);
                });
        return builder;
    }

    @NonNull
    private AlertDialog.Builder getBuilderEdit(Item editItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_item, null);

        editTextName = view.findViewById(R.id.editTextName);
        editTextPrice = view.findViewById(R.id.editTextPrice);
        editTextQuantity = view.findViewById(R.id.editTextQuantity);

        editTextPrice.setFilters(new InputFilter[]{new DecimalDigitsInputFilter()});
        editTextQuantity.setFilters(new InputFilter[]{new DigitsInputFilter()});

        editTextName.setText(editItem.getName());
        editTextPrice.setText(String.valueOf(editItem.getPrice()));
        editTextQuantity.setText(String.valueOf(editItem.getQuantity()));

        RecyclerView recyclerViewPeople = view.findViewById(R.id.recyclerViewPeople);
        SelectPeopleAdapter adapter = new SelectPeopleAdapter(peopleList);
        recyclerViewPeople.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewPeople.setAdapter(adapter);

        Button buttonSelectAll = view.findViewById(R.id.buttonSelectAll);
        Button buttonDeselectAll = view.findViewById(R.id.buttonDeselectAll);

        adapter.selectBySysId(humanSysIds);

        buttonSelectAll.setOnClickListener(v -> {
            for (int i = 0; i < adapter.getItemCount(); i++) {
                adapter.selectPerson(i);
            }
        });
        buttonDeselectAll.setOnClickListener(v -> adapter.deselectAllPeople());

        builder.setView(view)
                .setTitle("Edit")
                .setNegativeButton("Cancel", (dialog, which) -> {
                    listener.onCancel();
                })
                .setPositiveButton("Save", (dialog, which) -> {
                    if (editTextName.getText().toString().isEmpty()
                            || editTextPrice.getText().toString().isEmpty()
                    ) {
                        Toast.makeText(getContext(), "Fields 'Name', 'Price' should be filled!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String name = editTextName.getText().toString().trim();
                    BigDecimal price = new BigDecimal(editTextPrice.getText().toString().trim());
                    int quantity = 1;
                    if (!editTextQuantity.getText().toString().isEmpty()) {
                        quantity = Integer.parseInt(editTextQuantity.getText().toString().trim());
                    }

                    editItem.setName(name);
                    editItem.setPrice(price);
                    editItem.setQuantity(quantity);
                    editItem.setFullPrice(price.multiply(BigDecimal.valueOf(quantity)));

                    List<Integer> positions = adapter.getSelectedPositions();
                    Map<Integer, BigDecimal> prices = adapter.getSelectedPositionsPrice();
                    List<ItemConsumer> result = new ArrayList<>();
                    BigDecimal allPaid = BigDecimal.ZERO;
                    for (int pos : positions) {
                        if (prices.get(pos) != null) {
                            allPaid = allPaid.add(prices.get(pos));
                        }
                        Human tempHuman = peopleList.get(pos);
                        BigDecimal pay = prices.get(pos);
                        ItemConsumer itemConsumer = new ItemConsumer();
                        itemConsumer.setPaid(pay);
                        itemConsumer.setHumanSysId(tempHuman.getSysId());
                        result.add(itemConsumer);
                    }
                    if (allPaid.compareTo(editItem.getFullPrice()) > 0) {
                        listener.onCancel();
                        Toast.makeText(getContext(), "Total pays can't be bigger than full price!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    listener.onItemEdited(editItem, result);
                });
        return builder;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (AddItemDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context + " must implement AddItemDialogListener");
        }
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        listener.onCancel();
    }

    public interface AddItemDialogListener {
        void onItemAdded(Item item, List<ItemConsumer> itemConsumers);

        void onItemEdited(Item item, List<ItemConsumer> list);

        void onCancel();
    }
}
