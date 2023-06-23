package com.example.partycalculator.utils;

import android.content.Context;

import com.example.partycalculator.db.AppDatabase;
import com.example.partycalculator.db.dao.DebtDao;
import com.example.partycalculator.db.dao.HumanDao;
import com.example.partycalculator.db.dao.ItemConsumerDao;
import com.example.partycalculator.db.dao.ItemDao;
import com.example.partycalculator.entity.Debt;
import com.example.partycalculator.entity.Human;
import com.example.partycalculator.entity.Item;
import com.example.partycalculator.entity.ItemConsumer;
import com.example.partycalculator.entity.Party;

import java.math.BigDecimal;
import java.util.List;

public class DebtCalculatorService implements DebtService {

    private final Context context;
    private DebtDao debtDao;
    private ItemDao itemDao;
    private ItemConsumerDao itemConsumerDao;
    private HumanDao humanDao;
    private Party party;

    public DebtCalculatorService(Context context) {
        this.context = context;
    }

    @Override
    public void execute() {
        AppDatabase databaseClient = AppDatabase.getDatabase(context);
        party = Functions.getParty();
        debtDao = databaseClient.debtDao();
        itemDao = databaseClient.itemDao();
        itemConsumerDao = databaseClient.itemConsumerDao();
        humanDao = databaseClient.humanDao();

        deleteAllDebts();

        List<Item> items = itemDao.getAllItemsInParty(party.getSysId());
        List<Human> humans = humanDao.getAllHumanInParty(party.getSysId());

        for (Item item : items) {
            List<ItemConsumer> itemConsumers = itemConsumerDao.getAllItemsConsumersByItem(item.getSysId());
            if (isItemPaid(item)) {
                long ownerId = item.getItemOwnerSysId();
                if (ownerId != 0) {
                    for (ItemConsumer ic : itemConsumers) {
                        if (ownerId != ic.getHumanSysId()) {
                            Debt debt = new Debt();
                            debt.setPartySysId(party.getSysId());
                            debt.setCreditorSysId(ownerId);
                            debt.setDebtorSysId(ic.getHumanSysId());
                            debt.setDebt(ic.getToPay());
                            debtDao.insertDebt(debt);
                        }
                    }
                } else {
                    for (ItemConsumer ic : itemConsumers) {
                        if (ic.getToPay().compareTo(BigDecimal.ZERO) > 0) {
                            BigDecimal toPay = ic.getToPay();
                            for (ItemConsumer consumer : itemConsumers) {
                                if (ic.getSysId() == consumer.getSysId()) {
                                    continue;
                                }
                                if (consumer.getToPay().compareTo(BigDecimal.ZERO) < 0) {
                                    if (toPay.add(consumer.getToPay()).compareTo(BigDecimal.ZERO) > 0) {
                                        Debt debt = new Debt();
                                        debt.setPartySysId(party.getSysId());
                                        debt.setCreditorSysId(consumer.getHumanSysId());
                                        debt.setDebtorSysId(ic.getHumanSysId());
                                        debt.setDebt(consumer.getToPay().multiply(new BigDecimal(-1)));
                                        debtDao.insertDebt(debt);
                                        toPay = toPay.add(consumer.getToPay());
                                        consumer.setToPay(BigDecimal.ZERO);
                                    } else {
                                        Debt debt = new Debt();
                                        debt.setPartySysId(party.getSysId());
                                        debt.setCreditorSysId(consumer.getHumanSysId());
                                        debt.setDebtorSysId(ic.getHumanSysId());
                                        debt.setDebt(toPay);
                                        debtDao.insertDebt(debt);
                                        toPay = BigDecimal.ZERO;
                                        consumer.setToPay(toPay.add(consumer.getToPay()));
                                    }
                                }
                                if (toPay.compareTo(BigDecimal.ZERO) <= 0) {
                                    break;
                                }
                            }
                        }
                    }
                }
            } else {
                if (isItemWantToBuy(item)) {
                    for (ItemConsumer ic : itemConsumers) {
                            Debt debt = new Debt();
                            debt.setPartySysId(party.getSysId());
                            debt.setDebtorSysId(ic.getHumanSysId());
                            debt.setDebt(ic.getToPay());
                            debtDao.insertDebt(debt);
                    }
                }
            }
        }
    }

    private void deleteAllDebts() {
        List<Debt> debtList = debtDao.getAllDebt(party.getSysId());
        for (Debt dept : debtList) {
            debtDao.deleteDebt(dept);
        }
    }

    private boolean isItemWantToBuy(Item item) {
        List<ItemConsumer> itemConsumerList = itemConsumerDao.getAllItemsConsumersByItem(item.getSysId());
        for (ItemConsumer itemConsumer : itemConsumerList) {
            if (itemConsumer.getPaid() != null) {
                if (itemConsumer.getPaid().compareTo(BigDecimal.ZERO) != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isItemPaid(Item item) {
        List<ItemConsumer> itemConsumerList = itemConsumerDao.getAllItemsConsumersByItem(item.getSysId());
        BigDecimal paid = BigDecimal.ZERO;
        for (ItemConsumer itemConsumer : itemConsumerList) {
            if (itemConsumer.getPaid() != null) {
                paid = paid.add(itemConsumer.getPaid());
            }
        }
        return paid.compareTo(item.getFullPrice()) >= 0;
    }
}
