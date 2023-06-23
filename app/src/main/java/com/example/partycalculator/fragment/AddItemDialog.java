package com.example.partycalculator.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
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
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddItemDialog extends AppCompatDialogFragment {
    private EditText editTextName;
    private EditText editTextPrice;
    private EditText editTextQuantity;
    private AddItemDialogListener listener;
    private final List<Human> peopleList;
    private Item editItem;
    private List<Long> humanSysIds;
    private List<ItemConsumer> consumers;

    public AddItemDialog(List<Human> peopleList) {
        this.peopleList = peopleList;
    }

    public AddItemDialog(List<Human> peopleList, Item item, List<Long> humanSysIds, List<ItemConsumer> consumers) {
        this.peopleList = peopleList;
        this.editItem = item;
        this.humanSysIds = humanSysIds;
        this.consumers = consumers;
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

        BigDecimal tempPrice = BigDecimal.ZERO;
        if (!editTextPrice.getText().toString().isEmpty()) {
            tempPrice = new BigDecimal(editTextPrice.getText().toString().trim());
        }

        int tempQuantity = 1;
        if (!editTextQuantity.getText().toString().isEmpty()) {
            tempQuantity = Integer.parseInt(editTextQuantity.getText().toString().trim());
        }
        BigDecimal fullPrice = tempPrice.multiply(BigDecimal.valueOf(tempQuantity));

        SelectPeopleAdapter adapter = new SelectPeopleAdapter(peopleList, fullPrice);
        recyclerViewPeople.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewPeople.setAdapter(adapter);

        editTextQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void afterTextChanged(Editable s) {
                if (s != null) {
                    String pricePaidStr = s.toString().trim();
                    BigDecimal currentPrice = BigDecimal.ZERO;
                    if (!editTextPrice.getText().toString().isEmpty()) {
                        currentPrice = new BigDecimal(editTextPrice.getText().toString().trim());
                    }
                    if (!pricePaidStr.isEmpty()) {
                        adapter.setFullPrice(new BigDecimal(pricePaidStr).multiply(currentPrice));
                        adapter.notifyDataSetChanged();
                    } else {
                        adapter.setFullPrice(BigDecimal.ONE.multiply(currentPrice));
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

        editTextPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("NotifyDataSetChanged")
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
                        editTextPrice.setText(pricePaidStr);
                        editTextPrice.setSelection(pricePaidStr.length());
                    }
                    int currentQuantity = 1;
                    if (!editTextQuantity.getText().toString().isEmpty()) {
                        currentQuantity = Integer.parseInt(editTextQuantity.getText().toString().trim());
                    }
                    if (!pricePaidStr.isEmpty()) {
                        adapter.setFullPrice(new BigDecimal(pricePaidStr).multiply(new BigDecimal(currentQuantity)));
                        adapter.notifyDataSetChanged();
                    } else {
                        adapter.setFullPrice(BigDecimal.ZERO.multiply(new BigDecimal(currentQuantity)));
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

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
                .setNegativeButton("Cancel", (dialog, which) -> listener.onCancel())
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
                    if (!adapter.getItemOwner().isEmpty()) {
                        newItem.setItemOwnerSysId(peopleList.get(adapter.getItemOwner().get(0)).getSysId());
                    }

                    List<Integer> positions = adapter.getSelectedPositions();
                    BigDecimal toPay = newItem.getFullPrice().divide(BigDecimal.valueOf(positions.size()), 2, RoundingMode.HALF_UP);
                    Map<Integer, BigDecimal> prices = adapter.getSelectedPositionsPrice();
                    List<ItemConsumer> result = new ArrayList<>();
                    BigDecimal allPaid = BigDecimal.ZERO;
                    for (int pos : positions) {
                        BigDecimal pay = prices.get(pos);
                        if (pay == null) {
                            pay = BigDecimal.ZERO;
                        }
                        allPaid = allPaid.add(pay);

                        Human tempHuman = peopleList.get(pos);

                        ItemConsumer itemConsumer = new ItemConsumer();
                        itemConsumer.setPaid(pay);
                        itemConsumer.setHumanSysId(tempHuman.getSysId());
                        itemConsumer.setToPay(toPay.subtract(pay));
                        result.add(itemConsumer);

                        if (pay.compareTo(newItem.getFullPrice()) > 0) {
                            listener.onCancel();
                            Toast.makeText(getContext(), tempHuman.getName() + " paid can't be bigger than full price!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    if (allPaid.compareTo(newItem.getFullPrice()) > 0 && adapter.getItemOwner().isEmpty()) {
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
        BigDecimal tempPrice = BigDecimal.ZERO;
        if (!editTextPrice.getText().toString().isEmpty()) {
            tempPrice = new BigDecimal(editTextPrice.getText().toString().trim());
        }

        int tempQuantity = 1;
        if (!editTextQuantity.getText().toString().isEmpty()) {
            tempQuantity = Integer.parseInt(editTextQuantity.getText().toString().trim());
        }
        BigDecimal fullPrice = tempPrice.multiply(BigDecimal.valueOf(tempQuantity));

        final SelectPeopleAdapter adapter = new SelectPeopleAdapter(peopleList, fullPrice);
        recyclerViewPeople.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewPeople.setAdapter(adapter);
        editTextQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void afterTextChanged(Editable s) {
                if (s != null) {
                    String pricePaidStr = s.toString().trim();
                    BigDecimal currentPrice = BigDecimal.ZERO;
                    if (!editTextPrice.getText().toString().isEmpty()) {
                        currentPrice = new BigDecimal(editTextPrice.getText().toString().trim());
                    }
                    if (!pricePaidStr.isEmpty()) {
                        adapter.setFullPrice(new BigDecimal(pricePaidStr).multiply(currentPrice));
                        adapter.notifyDataSetChanged();
                    } else {
                        adapter.setFullPrice(BigDecimal.ONE.multiply(currentPrice));
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

        editTextPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("NotifyDataSetChanged")
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
                        editTextPrice.setText(pricePaidStr);
                        editTextPrice.setSelection(pricePaidStr.length());
                    }
                    int currentQuantity = 1;
                    if (!editTextQuantity.getText().toString().isEmpty()) {
                        currentQuantity = Integer.parseInt(editTextQuantity.getText().toString().trim());
                    }
                    if (!pricePaidStr.isEmpty()) {
                        adapter.setFullPrice(new BigDecimal(pricePaidStr).multiply(new BigDecimal(currentQuantity)));
                        adapter.notifyDataSetChanged();
                    } else {
                        adapter.setFullPrice(BigDecimal.ZERO.multiply(new BigDecimal(currentQuantity)));
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });


        Button buttonSelectAll = view.findViewById(R.id.buttonSelectAll);
        Button buttonDeselectAll = view.findViewById(R.id.buttonDeselectAll);

        adapter.selectBySysId(humanSysIds);
        adapter.setPaid(consumers);
        adapter.selectOwnerBySysId(editItem.getItemOwnerSysId());

        buttonSelectAll.setOnClickListener(v -> {
            for (int i = 0; i < adapter.getItemCount(); i++) {
                adapter.selectPerson(i);
            }
        });
        buttonDeselectAll.setOnClickListener(v -> adapter.deselectAllPeople());

        builder.setView(view)
                .setTitle("Edit")
                .setNegativeButton("Cancel", (dialog, which) -> listener.onCancel())
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
                    if (!adapter.getItemOwner().isEmpty()) {
                        editItem.setItemOwnerSysId(peopleList.get(adapter.getItemOwner().get(0)).getSysId());
                    } else {
                        editItem.setItemOwnerSysId(0);
                    }
                    List<Integer> positions = adapter.getSelectedPositions();
                    BigDecimal toPay = editItem.getFullPrice().divide(BigDecimal.valueOf(positions.size()), 2, RoundingMode.HALF_UP);
                    Map<Integer, BigDecimal> prices = adapter.getSelectedPositionsPrice();
                    List<ItemConsumer> result = new ArrayList<>();
                    BigDecimal allPaid = BigDecimal.ZERO;
                    for (int pos : positions) {
                        BigDecimal pay = prices.get(pos);
                        if (pay == null) {
                            pay = BigDecimal.ZERO;
                        }
                        allPaid = allPaid.add(pay);

                        Human tempHuman = peopleList.get(pos);

                        ItemConsumer itemConsumer = new ItemConsumer();
                        itemConsumer.setPaid(pay);
                        itemConsumer.setHumanSysId(tempHuman.getSysId());
                        itemConsumer.setToPay(toPay.subtract(pay));
                        result.add(itemConsumer);
                        if (pay.compareTo(editItem.getFullPrice()) > 0) {
                            listener.onCancel();
                            Toast.makeText(getContext(), tempHuman.getName() + " paid can't be bigger than full price!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    if (allPaid.compareTo(editItem.getFullPrice()) > 0 && adapter.getItemOwner().isEmpty()) {
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
