package com.example.partycalculator.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.partycalculator.R;
import com.example.partycalculator.adapter.DebtAdapter;
import com.example.partycalculator.db.AppDatabase;
import com.example.partycalculator.db.dao.DebtDao;
import com.example.partycalculator.entity.Debt;
import com.example.partycalculator.utils.DebtCalculatorService;
import com.example.partycalculator.utils.Functions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DebtActivity extends PartyToolbarActivity {
    private DebtDao debtDao;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debt);
        DebtCalculatorService debtCalculatorService = new DebtCalculatorService(this);
        debtCalculatorService.execute();

        AppDatabase partyDatabase = AppDatabase.getDatabase(getApplication());
        debtDao = partyDatabase.debtDao();
        List<Debt> debtList = debtDao.getAllDebt(Functions.getPartySysId());
        RecyclerView recyclerView = findViewById(R.id.recyclerViewDebt);
        DebtAdapter adapter = new DebtAdapter(debtList, partyDatabase.humanDao().getAllHumanInParty(Functions.getPartySysId()));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Button btnSimpleView = findViewById(R.id.btnSimpleView);
        btnSimpleView.setOnClickListener(v -> {
            adapter.setDebtList(getSimpleList());
            adapter.notifyDataSetChanged();
        });
        Button btnSimplestView = findViewById(R.id.btnSimplestView);
        btnSimplestView.setOnClickListener(v -> {
            adapter.setDebtList(getSimplestList());
            adapter.notifyDataSetChanged();
        });
    }

    private List<Debt> getSimpleList() {
        List<Debt> newDebts = new ArrayList<>();
        List<Debt> debtList = debtDao.getAllDebt(Functions.getPartySysId());
        for (Debt tempDebt : debtList) {
            Debt debt = getDebt(tempDebt.getDebtorSysId(), tempDebt.getCreditorSysId(), newDebts);
            if (debt == null) {
                debt = new Debt();
                debt.setDebt(tempDebt.getDebt());
                debt.setDebtorSysId(tempDebt.getDebtorSysId());
                debt.setPartySysId(tempDebt.getPartySysId());
                debt.setCreditorSysId(tempDebt.getCreditorSysId());
                newDebts.add(debt);
            } else {
                BigDecimal newDebt = debt.getDebt().add(tempDebt.getDebt());
                debt.setDebt(newDebt);
            }
        }
        return newDebts;
    }

    private List<Debt> getSimplestList() {
        List<Debt> newDebts = new ArrayList<>();
        List<Debt> debtList = debtDao.getAllDebt(Functions.getPartySysId());
        for (Debt tempDebt : debtList) {
            Debt debt = getDebt(tempDebt.getDebtorSysId(), tempDebt.getCreditorSysId(), newDebts);
            if (debt == null) {
                debt = new Debt();
                debt.setDebt(tempDebt.getDebt());
                debt.setDebtorSysId(tempDebt.getDebtorSysId());
                debt.setPartySysId(tempDebt.getPartySysId());
                debt.setCreditorSysId(tempDebt.getCreditorSysId());
                newDebts.add(debt);
            } else {
                BigDecimal newDebt = debt.getDebt().add(tempDebt.getDebt());
                debt.setDebt(newDebt);
            }
        }
        List<Debt> usedDebts = new ArrayList<>();
        List<Debt> result = new ArrayList<>();
        for (Debt tempDebt : newDebts) {
            if (usedDebts.contains(tempDebt)) {
                continue;
            }
            Debt oppositeDebt = getDebt(tempDebt.getCreditorSysId(), tempDebt.getDebtorSysId(), newDebts);
            if (oppositeDebt != null) {
                usedDebts.add(oppositeDebt);
                BigDecimal newDebt;
                if (tempDebt.getDebt().compareTo(oppositeDebt.getDebt()) > 0) {
                    newDebt = tempDebt.getDebt().subtract(oppositeDebt.getDebt());
                    tempDebt.setDebt(newDebt);
                    result.add(tempDebt);
                } else {
                    newDebt = oppositeDebt.getDebt().subtract(tempDebt.getDebt());
                    oppositeDebt.setDebt(newDebt);
                    result.add(oppositeDebt);
                }
            } else {
                result.add(tempDebt);
            }
        }
        return result;
    }

    private Debt getDebt(Long debtor, Long creditor, List<Debt> newDebts) {
        for (Debt debt : newDebts) {
            if (debt.getDebtorSysId() == debtor
                    && Objects.equals(debt.getCreditorSysId(), creditor)) {
                return debt;
            }
        }
        return null;
    }
}