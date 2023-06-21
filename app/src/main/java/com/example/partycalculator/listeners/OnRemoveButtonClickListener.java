package com.example.partycalculator.listeners;

import com.example.partycalculator.entity.Party;

@FunctionalInterface
public interface OnRemoveButtonClickListener {
    void onRemoveButtonClick(Party party);
}
