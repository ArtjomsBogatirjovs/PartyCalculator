package com.example.partycalculator.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.partycalculator.R;
import com.example.partycalculator.adapter.SelectPeopleAdapter;
import com.example.partycalculator.db.AppDatabase;
import com.example.partycalculator.db.dao.HumanDao;
import com.example.partycalculator.entity.Human;
import com.example.partycalculator.entity.Item;
import com.example.partycalculator.entity.ItemConsumer;
import com.example.partycalculator.entity.PartySingleton;
import com.example.partycalculator.filter.DecimalDigitsInputFilter;
import com.example.partycalculator.filter.DigitsInputFilter;
import com.example.partycalculator.ui.CreatePartyActivity;
import com.example.partycalculator.utils.Functions;

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

    public AddItemDialog(List<Human> peopleList) {
        this.peopleList = peopleList;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
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

        builder.setView(view)
                .setTitle("Add Item")
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Cancel button clicked
                })
                .setPositiveButton("Add", (dialog, which) -> {
                    if (editTextName.getText().toString().isEmpty()
                            || editTextPrice.getText().toString().isEmpty()
                            //|| editTextQuantity.getText().toString().isEmpty()
                    ) {
                        Toast.makeText(getContext(), "Fields 'Name', 'Price' should be filled!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String name = editTextName.getText().toString().trim();
                    BigDecimal price = new BigDecimal(editTextPrice.getText().toString().trim());
                    int quantity = 1;
                    if(!editTextQuantity.getText().toString().isEmpty()){
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
                    for (int pos : positions) {
                        Human tempHuman = peopleList.get(pos);
                        BigDecimal pay = prices.get(pos);
                        ItemConsumer itemConsumer = new ItemConsumer();
                        itemConsumer.setPaid(pay);
                        itemConsumer.setHumanSysId(tempHuman.getSysId());
                        result.add(itemConsumer);
                    }
                    listener.onItemAdded(newItem, result);
                });

        return builder.create();
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

    public interface AddItemDialogListener {
        void onItemAdded(Item item, List<ItemConsumer> itemConsumers);
    }
}
