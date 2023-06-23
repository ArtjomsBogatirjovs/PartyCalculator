package com.example.partycalculator.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.partycalculator.R;
import com.example.partycalculator.entity.Debt;
import com.example.partycalculator.entity.Human;

import java.util.List;

public class DebtAdapter extends RecyclerView.Adapter<DebtAdapter.DebtViewHolder> {
    private List<Debt> debtList;
    private final List<Human> humans;

    public DebtAdapter(List<Debt> debtList, List<Human> humans) {
        this.debtList = debtList;
        this.humans = humans;
    }

    public void setDebtList(List<Debt> debtList) {
        this.debtList = debtList;
    }

    @NonNull
    @Override
    public DebtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_debt, parent, false);
        return new DebtViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DebtViewHolder holder, int position) {
        Debt debt = debtList.get(position);
        holder.bind(debt);
    }

    @Override
    public int getItemCount() {
        return debtList.size();
    }

    private Human getHumanBySysId(Long sysId) {
        if(sysId == null){
             return null;
        }
        for (Human human : humans) {
            if (human.getSysId() == sysId) {
                return human;
            }
        }
        return null;
    }

    public class DebtViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewDebtor;
        private TextView textViewCreditor;
        private TextView textViewDebtAmount;
        private TextView textViewAction;

        public DebtViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDebtor = itemView.findViewById(R.id.textViewDebtor);
            textViewCreditor = itemView.findViewById(R.id.textViewCreditor);
            textViewDebtAmount = itemView.findViewById(R.id.textViewDebtAmount);
            textViewAction = itemView.findViewById(R.id.textViewAction);
        }

        public void bind(Debt debt) {
            Human creditor = getHumanBySysId(debt.getCreditorSysId());
            Human debtor = getHumanBySysId(debt.getDebtorSysId());

            textViewAction.setText("Owes");
            if (creditor != null) {
                textViewCreditor.setText(creditor.getName());
            } else {
                textViewAction.setText("Must pay");
            }
            if (debtor != null) {
                textViewDebtor.setText(debtor.getName());
            }

            textViewDebtAmount.setText(debt.getDebt().toString());

        }
    }
}

