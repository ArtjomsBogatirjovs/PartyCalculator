package com.example.partycalculator.listeners;

import com.example.partycalculator.entity.Party;

@FunctionalInterface
public interface OnItemClickListener {
    void onItemClick(Party party);
}
