package com.example.partycalculator.ui;

import android.os.Bundle;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.partycalculator.R;
import com.example.partycalculator.adapter.DebtAdapter;
import com.example.partycalculator.db.AppDatabase;
import com.example.partycalculator.db.dao.DebtDao;
import com.example.partycalculator.entity.Debt;
import com.example.partycalculator.entity.PartySingleton;
import com.example.partycalculator.utils.DebtCalculatorService;
import com.example.partycalculator.utils.Functions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DebtActivity extends PartyToolbarActivity {
    private DebtDao debtDao;

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

        Button btnAddItem = findViewById(R.id.btnSimpleView);
        btnAddItem.setOnClickListener(v -> {
            adapter.setDebtList(getSimpleList());
            adapter.notifyDataSetChanged();
        });
    }

    private List<Debt> getSimpleList() {
        List<Debt> newDebts = new ArrayList<>();
        List<Debt> debtList = debtDao.getAllDebt(Functions.getPartySysId());
        for (Debt tempDebt : debtList) {
            Debt debt = getDebt(tempDebt.getDebtorSysId(), tempDebt.getCreditorSysId(), newDebts);
            if(debt == null){
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